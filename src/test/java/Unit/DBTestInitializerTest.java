package Unit;

import Persistence.DBConnection.DBTestInitializer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DBTestInitializerTest {

    @Test
    void testCreateUsersTable() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        DBTestInitializer.createUsersTable(mockStmt);

        verify(mockStmt).execute(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue().trim().replaceAll("\\s+", " ");
        String expectedSql = "CREATE TABLE IF NOT EXISTS Users ( name VARCHAR(100) NOT NULL, surname VARCHAR(100) NOT NULL, email VARCHAR(100) PRIMARY KEY, pass_hash VARCHAR(255) NOT NULL, phone VARCHAR(20) UNIQUE, role VARCHAR(10) CHECK (role IN ('CUSTOMER', 'BARBER')) NOT NULL );";

        assertEquals(expectedSql.trim().replaceAll("\\s+", " "), capturedSql);

        // Verifica che le altre query non vengano eseguite
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Service_Types"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Appointments"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Available_Slots"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE News"));
    }

    @Test
    void testCreateServiceTypesTable() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        DBTestInitializer.createServiceTypesTable(mockStmt);

        verify(mockStmt).execute(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue().trim().replaceAll("\\s+", " ");
        String expectedSql = "CREATE TABLE IF NOT EXISTS Service_Types ( service_name VARCHAR(100) PRIMARY KEY, price DECIMAL(10, 2) CHECK (price >= 0) NOT NULL );";

        assertEquals(expectedSql.trim().replaceAll("\\s+", " "), capturedSql);

        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Users"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Appointments"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Available_Slots"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE News"));
    }

    @Test
    void testCreateAppointmentsTable() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        DBTestInitializer.createAppointmentsTable(mockStmt);

        verify(mockStmt).execute(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue().trim().replaceAll("\\s+", " ");
        String expectedSql = "CREATE TABLE IF NOT EXISTS Appointments ( app_date DATE NOT NULL, app_time TIME(0) NOT NULL, customer_email VARCHAR(100) NOT NULL, customer_phone VARCHAR(100) NOT NULL, barber_email VARCHAR(100) NOT NULL, barber_name VARCHAR(100) NOT NULL, service_name VARCHAR(100) NOT NULL, price DECIMAL(10, 2) CHECK (price >= 0) NOT NULL, payment VARCHAR(20) CHECK (payment IN ('PAYPAL', 'CREDIT_CARD', 'SHOP')) NOT NULL, PRIMARY KEY (app_date, app_time, barber_email), FOREIGN KEY (customer_email) REFERENCES Users(email) ON DELETE CASCADE, FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE, FOREIGN KEY (service_name) REFERENCES Service_Types(service_name) ON DELETE CASCADE );";

        assertEquals(expectedSql.trim().replaceAll("\\s+", " "), capturedSql);

        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Users"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Service_Types"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Available_Slots"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE News"));
    }

    @Test
    void testCreateAvailableSlotsTable() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        DBTestInitializer.createAvailableSlotsTable(mockStmt);

        verify(mockStmt).execute(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue().trim().replaceAll("\\s+", " ");
        String expectedSql = "CREATE TABLE IF NOT EXISTS Available_Slots ( barber_email VARCHAR(100) NOT NULL, slot_date DATE NOT NULL, start_time TIME(0) NOT NULL, PRIMARY KEY (barber_email, slot_date, start_time), FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE );";

        assertEquals(expectedSql.trim().replaceAll("\\s+", " "), capturedSql);

        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Users"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Service_Types"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Appointments"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE News"));
    }

    @Test
    void testCreateNewsTable() throws SQLException {
        Statement mockStmt = mock(Statement.class);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);

        DBTestInitializer.createNewsTable(mockStmt);

        verify(mockStmt).execute(sqlCaptor.capture());
        String capturedSql = sqlCaptor.getValue().trim().replaceAll("\\s+", " ");
        String expectedSql = "CREATE TABLE News ( title VARCHAR(255) NOT NULL, message TEXT NOT NULL, time TIME NOT NULL, date DATE NOT NULL, barber_email VARCHAR(100), to_customers BOOLEAN NOT NULL, PRIMARY KEY (title, message, time), FOREIGN KEY (barber_email) REFERENCES Users(email) ON DELETE CASCADE );";

        assertEquals(expectedSql.trim().replaceAll("\\s+", " "), capturedSql);

        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Users"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Service_Types"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Appointments"));
        verify(mockStmt, never()).execute(contains("CREATE TABLE IF NOT EXISTS Available_Slots"));
    }
}