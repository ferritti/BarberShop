package DBconnection.DAO;

import DBconnection.Database.DBManager;
import Model.Notification;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConcreteNewsDAO implements NewsDAO {
    private DBManager dbManager = DBManager.getInstance();

    @Override
    public boolean addNotification(Notification notification) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO News (title, message, barber_email, to_customers, time) " +
                            "VALUES (?, ?, ?, ?, ?)");

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getBarberEmail());
            stmt.setBoolean(4, notification.isToCustomers());
            stmt.setTime(5, java.sql.Time.valueOf(notification.getTime()));

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Notification> getAllBarberNews(String barberEmail) {
        List<Notification> notifications = new ArrayList<>();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM News WHERE barber_email = ? ");

            stmt.setString(1, barberEmail);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getTime("time").toLocalTime()
                );
                notifications.add(notification);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public List<Notification> getAllCustomerNews() {
        List<Notification> notifications = new ArrayList<>();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM News WHERE to_customers = true");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getTime("time").toLocalTime()
                );
                notifications.add(notification);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public boolean deleteNotification(Notification notification) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM News WHERE title = ? AND message = ? AND time = ?");

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getMessage());
            stmt.setTime(3, java.sql.Time.valueOf(notification.getTime()));

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}