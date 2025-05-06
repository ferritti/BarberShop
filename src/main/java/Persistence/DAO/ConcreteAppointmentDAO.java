package Persistence.DAO;

import Model.*;
import Persistence.DBConnection.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class ConcreteAppointmentDAO implements AppointmentDAO {

    private DBManager dbManager = DBManager.getInstance(false);

    @Override
    public boolean addAppointment(Appointment appointment) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Appointments (app_date, app_time, customer_email, barber_email, service_name, payment) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getCustomer().getEmail());
            stmt.setString(4, appointment.getBarber().getEmail());
            stmt.setString(5, appointment.getServiceType().getServiceName());
            stmt.setString(6, appointment.getPaymentMethod().toString());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAppointment(Appointment appointment) {
        try {
            Connection connection = dbManager.getConnection();
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

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT a.app_date, a.app_time, a.payment, " +
                            "c.name AS customer_name, c.surname AS customer_surname, c.email AS customer_email, c.pass_hash AS customer_pass, c.phone AS customer_phone, " +
                            "b.name AS barber_name, b.surname AS barber_surname, b.email AS barber_email, b.pass_hash AS barber_pass, b.phone AS barber_phone, " +
                            "s.service_name, s.price " +
                            "FROM Appointments a " +
                            "JOIN Users b ON a.barber_email = b.email " +
                            "JOIN Users c ON a.customer_email = c.email " +
                            "JOIN Service_Types s ON a.service_name = s.service_name " +
                            "WHERE b.email = ? " +
                            "ORDER BY a.app_date, a.app_time"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(rs.getString("customer_name"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_email"),
                        rs.getString("customer_pass"),
                        rs.getString("customer_phone"));

                Barber barber = new Barber(rs.getString("barber_name"),
                        rs.getString("barber_surname"),
                        rs.getString("barber_email"),
                        rs.getString("barber_pass"),
                        rs.getString("barber_phone"));
                ServiceType serviceType = new ServiceType(rs.getString("service_name"), rs.getDouble("price"));
                Appointment appointment = new Appointment(
                        rs.getDate("app_date").toLocalDate(),
                        rs.getTime("app_time").toLocalTime(),
                        customer,
                        barber,
                        serviceType,
                        PaymentMethod.valueOf(rs.getString("payment"))
                );

                appointments.add(appointment);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByEmailOfCustomer(String email) {
        List<Appointment> appointments = new ArrayList<>();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT a.app_date, a.app_time, a.payment, " +
                            "c.name AS customer_name, c.surname AS customer_surname, c.email AS customer_email, c.pass_hash AS customer_pass, c.phone AS customer_phone, " +
                            "b.name AS barber_name, b.surname AS barber_surname, b.email AS barber_email, b.pass_hash AS barber_pass, b.phone AS barber_phone, " +
                            "s.service_name, s.price " +
                            "FROM Appointments a " +
                            "JOIN Users c ON a.customer_email = c.email " +
                            "JOIN Users b ON a.barber_email = b.email " +
                            "JOIN Service_Types s ON a.service_name = s.service_name " +
                            "WHERE c.email = ? " +
                            "ORDER BY a.app_date, a.app_time"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Customer
                Customer customer = new Customer(
                        rs.getString("customer_name"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_email"),
                        rs.getString("customer_pass"),
                        rs.getString("customer_phone")
                );

                // Barber
                Barber barber = new Barber(
                        rs.getString("barber_name"),
                        rs.getString("barber_surname"),
                        rs.getString("barber_email"),
                        rs.getString("barber_pass"),
                        rs.getString("barber_phone")
                );

                // ServiceType
                ServiceType serviceType = new ServiceType(
                        rs.getString("service_name"),
                        rs.getDouble("price")
                );

                // Appointment
                Appointment appointment = new Appointment(
                        rs.getDate("app_date").toLocalDate(),
                        rs.getTime("app_time").toLocalTime(),
                        customer,
                        barber,
                        serviceType,
                        PaymentMethod.valueOf(rs.getString("payment"))
                );

                appointments.add(appointment);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointments;
    }

}