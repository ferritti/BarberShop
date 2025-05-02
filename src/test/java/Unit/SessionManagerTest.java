package Unit;

import Authentication.SessionManager;
import Model.Barber;
import Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {
    private SessionManager sessionManager;
    private Barber barberUser;
    private Customer customerUser;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        resetSingleton();
        sessionManager = SessionManager.getInstance();
        barberUser = new Barber("Mario", "Rossi", "mario@rossi.com", "password", "123456789");
        customerUser = new Customer("Luca", "Bianchi", "luca@bianchi.com", "password", "987654321");
    }

    @Test
    void singletonInstanceShouldBeSame() {
        SessionManager instance1 = SessionManager.getInstance();
        SessionManager instance2 = SessionManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void setAndGetCurrentUserAsBarber() {
        sessionManager.setCurrentUser(barberUser);
        assertEquals(barberUser, sessionManager.getCurrentUser());
    }

    @Test
    void setAndGetCurrentUserAsCustomer() {
        sessionManager.setCurrentUser(customerUser);
        assertEquals(customerUser, sessionManager.getCurrentUser());
    }

    @Test
    void getCurrentUserWithoutSettingShouldThrow() {
        Exception exception = assertThrows(IllegalStateException.class, sessionManager::getCurrentUser);
        assertEquals("User not logged in", exception.getMessage());
    }

    @Test
    void setCurrentUserWithNullShouldThrow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> sessionManager.setCurrentUser(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void resetUserShouldClearCurrentUser() {
        sessionManager.setCurrentUser(barberUser);
        sessionManager.resetUser();
        assertThrows(IllegalStateException.class, sessionManager::getCurrentUser);
    }

    @Test
    void closeSessionShouldResetInstance() throws NoSuchFieldException, IllegalAccessException {
        sessionManager.setCurrentUser(barberUser);
        sessionManager.closeSession();

        SessionManager newInstance = SessionManager.getInstance();
        assertNotSame(sessionManager, newInstance);

        assertThrows(IllegalStateException.class, newInstance::getCurrentUser);
    }

    private void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instanceField = SessionManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }
}