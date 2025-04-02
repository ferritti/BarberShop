package DBconnection.DAO;


import DBconnection.Database.DBManager;
import Model.AvailableSlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ConcreteAvailableSlotDAO implements AvailableSlotDAO {

    private DBManager dbManager = DBManager.getInstance(false);

    @Override
    public boolean addAvSlot(AvailableSlot avSlot) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                            "VALUES (?, ?, ?)");


            stmt.setString(1, avSlot.getBarberEmail());
            stmt.setDate(2, java.sql.Date.valueOf(avSlot.getDate()));
            stmt.setTime(3, java.sql.Time.valueOf(avSlot.getStartTime()));

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAvSlot(AvailableSlot avSlot) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Available_Slots WHERE barber_email = ? AND slot_date = ? AND start_time = ?");

            stmt.setString(1, avSlot.getBarberEmail());
            stmt.setDate(2, java.sql.Date.valueOf(avSlot.getDate()));
            stmt.setTime(3, java.sql.Time.valueOf(avSlot.getStartTime()));

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<AvailableSlot> getAvSlotsAtSelectedDate(LocalDate date, String barberEmail) {
        List<AvailableSlot> slots = new ArrayList<>();
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Available_Slots WHERE slot_date = ? AND barber_email = ?");

            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setString(2, barberEmail);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AvailableSlot slot = new AvailableSlot(
                        rs.getString("barber_email"),
                        rs.getDate("slot_date").toLocalDate(),
                        rs.getTime("start_time").toLocalTime()
                );

                slots.add(slot);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

}
