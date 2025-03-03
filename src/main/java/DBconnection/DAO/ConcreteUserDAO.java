package DBconnection.DAO;

import DBconnection.Database.DBManager;
import Model.Barber;
import Model.User;
import Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ConcreteUserDAO {
    private DBManager dbManager = DBManager.getInstance();
    public boolean addUser(User user) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO USER (NAME, SURNAME, EMAIL, PASS_HASH, PHONE, ROLE) VALUES (?,?,?,?,?,?)"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getUserType().name());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean removeUser(User user) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM USER WHERE EMAIL = ?"
            );
            stmt.setString(1, user.getEmail());

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findByEmail(String email) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM USER WHERE EMAIL = ?"
            );
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                User user;
                if(Objects.equals(rs.getString("ROLE"), "CUSTOMER")) {
                    return user = new Customer(
                            rs.getString("NAME"),
                            rs.getString("SURNAME"),
                            rs.getString("PASS_HASH"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONE")
                            );
                }
                else {
                    return user = new Barber(
                            rs.getString("NAME"),
                            rs.getString("SURNAME"),
                            rs.getString("PASS_HASH"),
                            rs.getString("EMAIL"),
                            rs.getString("PHONE")
                    );
                }
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}