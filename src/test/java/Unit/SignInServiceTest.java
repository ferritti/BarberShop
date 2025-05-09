package Unit;

import Authentication.SessionManager;
import Services.SignInService;
import Persistence.DAO.UserDAO;
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
    private SignInService signInService;

    @Mock
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        sessionManager = mock(SessionManager.class);
        signInService = new SignInService(userDAO, sessionManager);
    }

    @Test
    void authenticateValidCredentials() {
        Customer customer = new Customer("Mario", "Rossi", EMAIL, PASSWORD, "1234567890");

        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(true);
        when(userDAO.findByEmail(EMAIL)).thenReturn(customer);

        assertTrue(signInService.authenticateUser(EMAIL, PASSWORD));
        verify(userDAO, times(1)).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO, times(1)).findByEmail(EMAIL);
        verify(sessionManager).setCurrentUser(customer);
    }

    @Test
    void authenticateInvalidCredentials() {
        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(false);

        assertFalse(signInService.authenticateUser(EMAIL, PASSWORD));
        verify(userDAO, times(1)).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO, never()).findByEmail(anyString());
        verify(sessionManager, never()).setCurrentUser(any());
    }

    @Test
    void isCustomerWhenUserIsCustomer() {
        Customer mockCustomer = mock(Customer.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockCustomer);

        assertTrue(signInService.isCustomer());
    }

    @Test
    void isCustomerWhenUserIsNotCustomer() {
        Barber mockBarber = mock(Barber.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockBarber);

        assertFalse(signInService.isCustomer());
    }

    @Test
    void emailExistsShouldReturnTrue() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(new Customer("Mario", "Rossi", EMAIL, PASSWORD, "1234567890"));

        assertTrue(signInService.checkEmailExists(EMAIL));
        verify(userDAO, times(1)).findByEmail(EMAIL);
    }

    @Test
    void emailExistsShouldReturnFalse() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(null);

        assertFalse(signInService.checkEmailExists(EMAIL));
        verify(userDAO, times(1)).findByEmail(EMAIL);
    }
}
