package Persistence.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBtestInitializer {

    public static void initializeTestDatabase() {
        DBManager manager = DBManager.getInstance(true);
        Connection connection = manager.getConnection();

        try (Statement stmt = connection.createStatement()) {
            createUsersTable(stmt);
            createServiceTypesTable(stmt);
            createAppointmentsTable(stmt);
            createAvailableSlotsTable(stmt);
            createNewsTable(stmt);

            System.out.println("Database H2 inizializzato correttamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nell'inizializzazione del database di test H2");
        }
    }

    public static void createUsersTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Users (
                name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                email VARCHAR(100) PRIMARY KEY,
                pass_hash VARCHAR(255) NOT NULL,
                phone VARCHAR(20) UNIQUE,
                role VARCHAR(10) CHECK (role IN ('CUSTOMER', 'BARBER')) NOT NULL
            );
        """);
    }

    public static void createServiceTypesTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Service_Types (
                service_name VARCHAR(100) PRIMARY KEY,
                price DECIMAL(10, 2) CHECK (price >= 0) NOT NULL
            );
        """);
    }

    public static void createAppointmentsTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Appointments (
                app_date DATE NOT NULL,
                app_time TIME(0) NOT NULL,  
                customer_email VARCHAR(100) NOT NULL,
                customer_phone VARCHAR(100) NOT NULL,
                barber_email VARCHAR(100) NOT NULL,
                barber_name VARCHAR(100) NOT NULL,
                service_name VARCHAR(100) NOT NULL,
                price DECIMAL(10, 2) CHECK (price >= 0) NOT NULL,
                payment VARCHAR(20) CHECK (payment IN ('PAYPAL', 'CREDIT_CARD', 'SHOP')) NOT NULL,
                PRIMARY KEY (app_date, app_time, barber_email),
                FOREIGN KEY (customer_email) REFERENCES Users(email) ON DELETE CASCADE,
                FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE,
                FOREIGN KEY (service_name) REFERENCES Service_Types(service_name) ON DELETE CASCADE
            );
        """);
    }

    public static void createAvailableSlotsTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Available_Slots (
                barber_email VARCHAR(100) NOT NULL,
                slot_date DATE NOT NULL,
                start_time TIME(0) NOT NULL, 
                PRIMARY KEY (barber_email, slot_date, start_time),
                FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE
            );
        """);
    }

    public static void createNewsTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE News (
                title VARCHAR(255) NOT NULL,
                message TEXT NOT NULL,
                time TIME NOT NULL,
            	date DATE NOT NULL,
                barber_email VARCHAR(100),
                to_customers BOOLEAN NOT NULL,
                PRIMARY KEY (title, message, time),
                FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE
            );
        """);
    }
}
