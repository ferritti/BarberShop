package Unit;

import Authentication.SessionManager;
import Business.SignInService;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignInServiceTest {

    private static final String EMAIL = "test@mail.com";
    private static final String PASSWORD = "password123";

    private UserDAO userDAO;
    private SignInService signinService;

    @Mock
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        sessionManager = mock(SessionManager.class);
        signinService = new SignInService(userDAO, sessionManager);
    }

    @Test
    void authenticateUser_validCredentials_shouldReturnTrue() {
        Customer customer = new Customer("Mario", "Rossi", EMAIL, PASSWORD, "1234567890");

        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(true);
        when(userDAO.findByEmail(EMAIL)).thenReturn(customer);

        assertTrue(signinService.authenticateUser(EMAIL, PASSWORD));
        verify(userDAO, times(1)).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO, times(1)).findByEmail(EMAIL);
        verify(sessionManager).setCurrentUser(customer);
    }

    @Test
    void authenticateUser_invalidCredentials_shouldReturnFalse() {
        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(false);

        assertFalse(signinService.authenticateUser(EMAIL, PASSWORD));
        verify(userDAO, times(1)).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO, never()).findByEmail(anyString());
        verify(sessionManager, never()).setCurrentUser(any());
    }

    @Test
    void testIsCustomer_userIsCustomer_shouldReturnTrue() {
        Customer mockCustomer = mock(Customer.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockCustomer);

        assertTrue(signinService.isCustomer());
    }

    @Test
    void isCustomer_userIsNotCustomer_shouldReturnFalse() {
        Barber mockBarber = mock(Barber.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockBarber);

        assertFalse(signinService.isCustomer());
    }

    @Test
    void checkEmailExists_emailExists_shouldReturnTrue() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(new Customer("Mario", "Rossi", EMAIL, PASSWORD, "1234567890"));

        assertTrue(signinService.checkEmailExists(EMAIL));
        verify(userDAO, times(1)).findByEmail(EMAIL);
    }

    @Test
    void checkEmailExists_emailDoesNotExist_shouldReturnFalse() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(null);

        assertFalse(signinService.checkEmailExists(EMAIL));
        verify(userDAO, times(1)).findByEmail(EMAIL);
    }
}

