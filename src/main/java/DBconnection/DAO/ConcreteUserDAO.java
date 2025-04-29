package DBconnection.DAO;

import DBconnection.Database.DBManager;
import Model.Barber;
import Model.User;
import Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import Security.HashingPassword;

public class ConcreteUserDAO implements UserDAO{

    private DBManager dbManager = DBManager.getInstance(false);

    public boolean addUser(User user) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Users (name, surname, email, pass_hash, phone, role) VALUES (?,?,?,?,?,?)"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getHashedPass());
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
                            rs.getString("email"),
                            rs.getString("pass_hash"),
                            rs.getString("phone")
                            );
                }
                else {
                    return user = new Barber(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getString("pass_hash"),
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

    public boolean checkCredentials(String email, String pass) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT pass_hash FROM Users WHERE email = ?"
            );
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String pass_hash = rs.getString("pass_hash");
                return HashingPassword.checkPassword(pass, pass_hash);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public HashMap<String, String> getBarbersData() {
        HashMap<String, String> barbersData = new HashMap<>();

        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT name, surname, email FROM Users WHERE role = 'BARBER'");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("name") + " " + rs.getString("surname");
                String email = rs.getString("email");
                barbersData.put(fullName, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barbersData;
    }
}