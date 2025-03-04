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

public class ConcreteUserDAO implements UserDAO{
    private DBManager dbManager = DBManager.getInstance();
    public boolean addUser(User user) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Users (name, surname, email, pass_hash, phone, role) VALUES (?,?,?,?,?,?)"
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
                    "DELETE FROM Users WHERE email = ?\n"
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
                    "SELECT * FROM Users WHERE email = ?"
            );
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                User user;
                if(Objects.equals(rs.getString("role"), "CUSTOMER")) {
                    return user = new Customer(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("pass_hash"),
                            rs.getString("email"),
                            rs.getString("phone")
                            );
                }
                else {
                    return user = new Barber(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("pass_hash"),
                            rs.getString("email"),
                            rs.getString("phone")
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