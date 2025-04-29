package DBconnection.Database;

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
            createCreateSlotsFunction(stmt);
            createCreateSlotsTrigger(stmt);

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

    private static void createServiceTypesTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS Service_Types (
                service_name VARCHAR(100) PRIMARY KEY,
                price DECIMAL(10, 2) CHECK (price >= 0) NOT NULL
            );
        """);
    }

    private static void createAppointmentsTable(Statement stmt) throws SQLException {
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

    private static void createAvailableSlotsTable(Statement stmt) throws SQLException {
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

    private static void createNewsTable(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS News (
                title VARCHAR(255) NOT NULL,
                message TEXT NOT NULL,
                time TIME NOT NULL, 
                barber_email VARCHAR(100),
                to_customers BOOLEAN NOT NULL,
                PRIMARY KEY (title, message, time),
                FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE
            );
        """);
    }

    private static void createCreateSlotsFunction(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE OR REPLACE FUNCTION create_slots_for_new_barber_function() 
            RETURNS TRIGGER AS $$
            DECLARE
                start_date DATE;
                end_date DATE;
            BEGIN
                IF NEW.role = 'BARBER' THEN
                    start_date := CURRENT_DATE;
                    end_date := start_date + INTERVAL '1 year';
                    
                    WHILE start_date < end_date LOOP
                        IF NOT EXISTS (
                            SELECT 1
                            FROM Available_Slots
                            WHERE barber_email = NEW.email
                            AND slot_date = start_date
                            AND start_time IN ('08:00:00', '09:00:00', '10:00:00', '11:00:00', '12:00:00', '13:00:00', 
                                               '15:00:00', '16:00:00', '17:00:00', '18:00:00', '19:00:00', '20:00:00')
                        ) THEN
                            INSERT INTO Available_Slots (barber_email, slot_date, start_time)
                            VALUES (NEW.email, start_date, '08:00:00'),
                                   (NEW.email, start_date, '09:00:00'),
                                   (NEW.email, start_date, '10:00:00'),
                                   (NEW.email, start_date, '11:00:00'),
                                   (NEW.email, start_date, '12:00:00'),
                                   (NEW.email, start_date, '13:00:00');
                            INSERT INTO Available_Slots (barber_email, slot_date, start_time)
                            VALUES (NEW.email, start_date, '15:00:00'),
                                   (NEW.email, start_date, '16:00:00'),
                                   (NEW.email, start_date, '17:00:00'),
                                   (NEW.email, start_date, '18:00:00'),
                                   (NEW.email, start_date, '19:00:00'),
                                   (NEW.email, start_date, '20:00:00');
                        END IF;
                        start_date := start_date + INTERVAL '1 day';
                    END LOOP;
                END IF;
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;
        """);
    }

    private static void createCreateSlotsTrigger(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TRIGGER create_slots_for_new_barber
            AFTER INSERT ON Users
            FOR EACH ROW
            EXECUTE FUNCTION create_slots_for_new_barber_function();
        """);
    }
}
