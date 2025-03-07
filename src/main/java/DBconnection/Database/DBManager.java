package DBconnection.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    public static DBManager manager = null;
    private Connection connection = null;

    public static final String URL = "jdbc:postgresql://localhost:5432/BarberShop_DB";
    public static final String USER = "SWEuser";
    public static final String PASSWORD = "swepass";

    private DBManager() {

        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed");
        }
    }

    private static DBManager init() {
        if (manager == null) {
            manager = new DBManager();
        }
        return manager;
    }

    public static DBManager getInstance() {
        return init();
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (manager != null && manager.connection != null && !manager.connection.isClosed()) {
                manager.connection.close();
                System.out.println("Connection closed");
                manager = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
