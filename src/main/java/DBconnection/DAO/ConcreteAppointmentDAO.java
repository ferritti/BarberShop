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
                    "INSERT INTO Appointments (date, time, customer_email, barber_email, service_name) " +
                            "VALUES (?, ?, ?, ?, ?)");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getCustomerEmail());
            stmt.setString(4, appointment.getBarberEmail());
            stmt.setString(5, appointment.getServiceTypeName());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAppointment(Appointment appointment) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Appointments WHERE date = ? AND time = ? AND customer_email = ? AND barber_email = ?");

            stmt.setDate(1, java.sql.Date.valueOf(appointment.getDate()));
            stmt.setTime(2, java.sql.Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getCustomerEmail());
            stmt.setString(4, appointment.getBarberEmail());

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
                    "SELECT * FROM Appointments WHERE customer_email = ? OR barber_email = ?");

            stmt.setString(1, email);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime(),
                        rs.getString("customer_email"),
                        rs.getString("barber_email"),
                        rs.getString("service_type_name")
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