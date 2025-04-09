package Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import DBconnection.Database.DBManager;
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

    private void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DBManager.class.getDeclaredField("manager");
        instance.setAccessible(true);
        instance.set(null, null);
    }
}