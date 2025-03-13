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
    public boolean addNews(Notification notification) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO News (title, message, target_type) " +
                            "VALUES (?, ?, ?)");

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getTargetType().name());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeNews(Notification notification) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM News WHERE title = ? AND message = ?");

            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getMessage());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Notification> getAllNews(User.UserType userType) {
        List<Notification> notifications = new ArrayList<>();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM News WHERE target_type = ? OR target_type = 'ALL'");

            stmt.setString(1, userType.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getString("title"),
                        rs.getString("message"),
                        Notification.TargetType.valueOf(rs.getString("target_type"))
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

}
