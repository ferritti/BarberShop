package DBconnection.DAO;

import DBconnection.Database.DBManager;
import Model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class ConcreteAppointmentDAO implements AppointmentDAO {

    private DBManager dbManager = DBManager.getInstance();

    @Override
    public boolean addAppointment(Appointment appointment) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Appointments (app_date, app_time, customer_email, customerName, barber_email, barberName, service_name, payment) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getCustomerEmail());
            stmt.setString(4, appointment.getCustomerPhone());
            stmt.setString(4, appointment.getBarberEmail());
            stmt.setString(5, appointment.getBarberName());
            stmt.setString(5, appointment.getServiceTypeName());
            stmt.setString(6, appointment.getPayment().name());

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
                    "DELETE FROM Appointments WHERE app_date = ? AND customer_email = ?");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setString(2, appointment.getCustomerEmail());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Appointment> findByEmailOfUser(String email) {
        List<Appointment> appointments = new ArrayList<>();
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Appointments JOIN Users ON Appointments.customer_email = Users.email WHERE Appointments.customer_email = ? OR Appointments.barber_email = ?");

            stmt.setString(1, email);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getString("payment").equals("ONLINE") ? Appointment.Payment.ONLINE : Appointment.Payment.SHOP,
                        rs.getString("service_name"),
                        rs.getString("barber_name"),
                        rs.getString("barber_email"),
                        rs.getString("customer_email"),
                        rs.getString("phone"),
                        rs.getTime("app_time").toLocalTime(),
                        rs.getDate("app_date").toLocalDate(),
                        rs.getDouble("price")
                );
                appointments.add(appointment);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}

