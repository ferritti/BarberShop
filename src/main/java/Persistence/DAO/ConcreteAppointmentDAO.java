package Persistence.DAO;

import Model.*;
import Persistence.DBConnection.DBManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConcreteAppointmentDAO implements AppointmentDAO {

    private DBManager dbManager = DBManager.getInstance(false);

    @Override
    public boolean addAppointment(Appointment appointment) {
        Connection connection = null;
        PreparedStatement appointmentStmt = null;
        PreparedStatement serviceStmt = null;
        boolean success = false;

        try {
            connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            // Insert into Appointments table
            appointmentStmt = connection.prepareStatement(
                    "INSERT INTO Appointments (app_date, app_time, customer_email, barber_email, payment) " +
                            "VALUES (?, ?, ?, ?, ?)");

            appointmentStmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            appointmentStmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            appointmentStmt.setString(3, appointment.getCustomer().getEmail());
            appointmentStmt.setString(4, appointment.getBarber().getEmail());
            appointmentStmt.setString(5, appointment.getPaymentMethod().toString());

            int rows = appointmentStmt.executeUpdate();

            // Insert into Appointment_Services table for each service type
            if (rows > 0) {
                serviceStmt = connection.prepareStatement(
                        "INSERT INTO Appointment_Services (app_date, app_time, barber_email, service_name) " +
                                "VALUES (?, ?, ?, ?)");

                for (ServiceType serviceType : appointment.getServiceTypes()) {
                    serviceStmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
                    serviceStmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
                    serviceStmt.setString(3, appointment.getBarber().getEmail());
                    serviceStmt.setString(4, serviceType.getName());
                    serviceStmt.addBatch();
                }

                int[] serviceRows = serviceStmt.executeBatch();
                success = serviceRows.length == appointment.getServiceTypes().size();
            }

            if (success) {
                connection.commit();
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (serviceStmt != null) serviceStmt.close();
                if (appointmentStmt != null) appointmentStmt.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    // Note: Not closing connection as it's managed by DBManager
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    @Override
    public boolean deleteAppointment(Appointment appointment) {
        try {
            Connection connection = dbManager.getConnection();
            // Services will be automatically deleted due to ON DELETE CASCADE
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Appointments WHERE app_date = ? AND app_time = ? AND barber_email = ?");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getBarber().getEmail());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Appointment> findByEmailOfBarber(String email) {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();

            // First, get all appointments for this barber
            stmt = connection.prepareStatement(
                    "SELECT DISTINCT a.app_date, a.app_time, a.payment, " +
                            "c.name AS customer_name, c.surname AS customer_surname, " +
                            "c.email AS customer_email, c.pass_hash AS customer_pass, c.phone AS customer_phone, " +
                            "b.name AS barber_name, b.surname AS barber_surname, " +
                            "b.email AS barber_email, b.pass_hash AS barber_pass, b.phone AS barber_phone " +
                            "FROM Appointments a " +
                            "JOIN Users b ON a.barber_email = b.email " +
                            "JOIN Users c ON a.customer_email = c.email " +
                            "WHERE b.email = ? " +
                            "ORDER BY a.app_date, a.app_time"
            );

            stmt.setString(1, email);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate appDate = rs.getDate("app_date").toLocalDate();
                LocalTime appTime = rs.getTime("app_time").toLocalTime();
                String barberEmail = rs.getString("barber_email");

                // Create Customer object
                Customer customer = new Customer(
                        rs.getString("customer_name"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_email"),
                        rs.getString("customer_pass"),
                        rs.getString("customer_phone")
                );

                // Create Barber object
                Barber barber = new Barber(
                        rs.getString("barber_name"),
                        rs.getString("barber_surname"),
                        rs.getString("barber_email"),
                        rs.getString("barber_pass"),
                        rs.getString("barber_phone")
                );

                // Get payment method
                PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment"));

                // Now get all services for this appointment
                List<ServiceType> serviceTypes = getServicesForAppointment(connection, appDate, appTime, barberEmail);

                // Create Appointment object
                Appointment appointment = new Appointment(
                        appDate,
                        appTime,
                        customer,
                        barber,
                        serviceTypes,
                        paymentMethod
                );

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Not closing connection as it's managed by DBManager
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByEmailOfCustomer(String email) {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();

            // First, get all appointments for this customer
            stmt = connection.prepareStatement(
                    "SELECT DISTINCT a.app_date, a.app_time, a.payment, " +
                            "c.name AS customer_name, c.surname AS customer_surname, " +
                            "c.email AS customer_email, c.pass_hash AS customer_pass, c.phone AS customer_phone, " +
                            "b.name AS barber_name, b.surname AS barber_surname, " +
                            "b.email AS barber_email, b.pass_hash AS barber_pass, b.phone AS barber_phone " +
                            "FROM Appointments a " +
                            "JOIN Users c ON a.customer_email = c.email " +
                            "JOIN Users b ON a.barber_email = b.email " +
                            "WHERE c.email = ? " +
                            "ORDER BY a.app_date, a.app_time"
            );

            stmt.setString(1, email);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate appDate = rs.getDate("app_date").toLocalDate();
                LocalTime appTime = rs.getTime("app_time").toLocalTime();
                String barberEmail = rs.getString("barber_email");

                // Create Customer object
                Customer customer = new Customer(
                        rs.getString("customer_name"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_email"),
                        rs.getString("customer_pass"),
                        rs.getString("customer_phone")
                );

                // Create Barber object
                Barber barber = new Barber(
                        rs.getString("barber_name"),
                        rs.getString("barber_surname"),
                        rs.getString("barber_email"),
                        rs.getString("barber_pass"),
                        rs.getString("barber_phone")
                );

                // Get payment method
                PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment"));

                // Now get all services for this appointment
                List<ServiceType> serviceTypes = getServicesForAppointment(connection, appDate, appTime, barberEmail);

                // Create Appointment object
                Appointment appointment = new Appointment(
                        appDate,
                        appTime,
                        customer,
                        barber,
                        serviceTypes,
                        paymentMethod
                );

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Not closing connection as it's managed by DBManager
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return appointments;
    }

    private List<ServiceType> getServicesForAppointment(Connection connection, LocalDate date, LocalTime time, String barberEmail)
            throws SQLException {
        List<ServiceType> serviceTypes = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(
                    "SELECT s.service_name, s.price " +
                            "FROM Appointment_Services as_join " +
                            "JOIN Service_Types s ON as_join.service_name = s.service_name " +
                            "WHERE as_join.app_date = ? AND as_join.app_time = ? AND as_join.barber_email = ?"
            );

            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setTime(2, java.sql.Time.valueOf(time));
            stmt.setString(3, barberEmail);

            rs = stmt.executeQuery();

            while (rs.next()) {
                ServiceType serviceType = new ServiceType(
                        rs.getString("service_name"),
                        rs.getDouble("price")
                );
                serviceTypes.add(serviceType);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return serviceTypes;
    }
}