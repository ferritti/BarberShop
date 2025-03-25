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

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";

    private UserDAO userDAO;
    private SignInService signinService;

    @Mock
    private SessionManager sessionManager; // Usa un mock anziché uno spy

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        sessionManager = mock(SessionManager.class); // Mock completo
        signinService = new SignInService(userDAO, sessionManager);
    }

    @Test
    void authenticateUser_validCredentials_shouldReturnTrue() {
        Customer customer = new Customer("John", "Doe", EMAIL, PASSWORD, "1234567890");

        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(true);
        when(userDAO.findByEmail(EMAIL)).thenReturn(customer);

        assertTrue(signinService.authenticateUser(EMAIL, PASSWORD));

        verify(userDAO).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO).findByEmail(EMAIL);
        verify(sessionManager).setCurrentUser(customer); // Verifica se setCurrentUser è stato invocato
    }

    @Test
    void authenticateUser_invalidCredentials_shouldReturnFalse() {
        when(userDAO.checkCredentials(EMAIL, PASSWORD)).thenReturn(false);

        assertFalse(signinService.authenticateUser(EMAIL, PASSWORD));
        verify(userDAO).checkCredentials(EMAIL, PASSWORD);
        verify(userDAO, never()).findByEmail(anyString());
        verify(sessionManager, never()).setCurrentUser(any());
    }

    @Test
    public void testIsCustomer_userIsCustomer_shouldReturnTrue() {
        // Setup
        UserDAO mockUserDAO = mock(UserDAO.class);
        SessionManager mockSessionManager = mock(SessionManager.class);
        SignInService signInService = new SignInService(mockUserDAO, mockSessionManager);

        // Creazione di un mock di Customer
        Customer mockCustomer = mock(Customer.class);

        // Mock dei metodi
        when(mockSessionManager.getCurrentUser()).thenReturn(mockCustomer);

        // Esegui il test
        boolean result = signInService.isCustomer();

        // Verifica che il risultato sia true
        assertTrue(result);
    }


    @Test
    void isCustomer_userIsNotCustomer_shouldReturnFalse() {
        Barber barber = new Barber("Mike", "Smith", "barber@example.com", "securePass", "9876543210");
        doReturn(barber).when(sessionManager).getCurrentUser();

        assertFalse(signinService.isCustomer());
    }

    @Test
    void checkEmailExists_emailExists_shouldReturnTrue() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(new Customer("John", "Doe", EMAIL, PASSWORD, "1234567890"));

        assertTrue(signinService.checkEmailExists(EMAIL));
        verify(userDAO).findByEmail(EMAIL);
    }

    @Test
    void checkEmailExists_emailDoesNotExist_shouldReturnFalse() {
        when(userDAO.findByEmail(EMAIL)).thenReturn(null);

        assertFalse(signinService.checkEmailExists(EMAIL));
        verify(userDAO).findByEmail(EMAIL);
    }
}
