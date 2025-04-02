package DBconnection.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    public static DBManager manager = null;
    private Connection connection = null;

    // Parametri di connessione per la produzione
    public static final String PROD_URL = "jdbc:postgresql://localhost:5432/BarberShop_DB";
    public static final String PROD_USER = "SWEuser";
    public static final String PROD_PASSWORD = "swepass";

    // Parametri di connessione per il database H2 (usato nei test)
    public static final String TEST_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    public static final String TEST_USER = "sa";
    public static final String TEST_PASSWORD = "";

    private DBManager(boolean isTest) {
        try {
            // Scegli la connessione in base al flag isTest
            String url = isTest ? TEST_URL : PROD_URL;
            String user = isTest ? TEST_USER : PROD_USER;
            String password = isTest ? TEST_PASSWORD : PROD_PASSWORD;

            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established to " + (isTest ? "H2 Test DB" : "PostgreSQL DB"));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed");
        }
    }

    private static DBManager init(boolean isTest) {
        if (manager == null) {
            manager = new DBManager(isTest);
        }
        return manager;
    }

    public static DBManager getInstance(boolean isTest) {
        return init(isTest);
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
