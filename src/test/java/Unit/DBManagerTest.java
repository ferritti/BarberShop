package Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import DBconnection.Database.DBManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBManagerTest {
    private Connection mockConnection;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        mockConnection = mock(Connection.class);
        resetSingleton();
        DBManager dbManager = DBManager.getInstance(false);
        Field connectionField = DBManager.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(dbManager, mockConnection);
    }

    @AfterEach
    void tearDown() throws NoSuchFieldException, IllegalAccessException, SQLException {
        DBManager manager = getCurrentInstance();
        if (manager != null && manager.getConnection() != null && !manager.getConnection().isClosed()) {
            manager.close();
        }
        resetSingleton();
    }

    @Test
    void testSingletonInstance() {
        DBManager instance1 = DBManager.getInstance(false);
        DBManager instance2 = DBManager.getInstance(false);
        assertSame(instance1, instance2);
    }

    @Test
    void testGetConnection() {
        DBManager dbManager = DBManager.getInstance(false);
        assertNotNull(dbManager.getConnection());
    }

    @Test
    void testCloseConnection() throws SQLException {
        DBManager dbManager = DBManager.getInstance(false);
        when(mockConnection.isClosed()).thenReturn(false);
        dbManager.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testH2SingletonInstance() throws NoSuchFieldException, IllegalAccessException {
        resetSingleton();
        DBManager instance1 = DBManager.getInstance(true);
        DBManager instance2 = DBManager.getInstance(true);
        assertSame(instance1, instance2);
    }

    @Test
    void testH2ConnectionNotNullAndOpen() throws SQLException, NoSuchFieldException, IllegalAccessException {
        resetSingleton();
        DBManager dbManager = DBManager.getInstance(true);
        Connection connection = dbManager.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    void testH2CloseConnection() throws SQLException, NoSuchFieldException, IllegalAccessException {
        resetSingleton();
        DBManager dbManager = DBManager.getInstance(true);
        Connection connection = dbManager.getConnection();
        assertFalse(connection.isClosed());
        dbManager.close();
        assertTrue(connection.isClosed());
    }

    private void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DBManager.class.getDeclaredField("manager");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private DBManager getCurrentInstance() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DBManager.class.getDeclaredField("manager");
        instance.setAccessible(true);
        return (DBManager) instance.get(null);
    }
}