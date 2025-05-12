package IntegrationTests;

import Services.*;
import Persistence.DAO.*;
import Model.*;
import Authentication.SessionManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private SignUpService signUpService;
    private SignInService signInService;
    private UserDAO userDAO;
    private SessionManager sessionManager;

    private User testCustomer;
    private User testBarber;

    private final String customerEmail = "customer@example.com";
    private final String barberEmail = "barber@example.com";

    @BeforeEach
    void setUp() {
        userDAO = new ConcreteUserDAO();
        sessionManager = SessionManager.getInstance();

        signUpService = new SignUpService(userDAO);
        signInService = new SignInService(userDAO, sessionManager);

        clearTestData();
    }

    @AfterEach
    void tearDown() {
        sessionManager.resetUser();
        clearTestData();
    }

    private void clearTestData() {
        userDAO.removeUserByEmail(customerEmail);
        userDAO.removeUserByEmail(barberEmail);
        testCustomer = null;
        testBarber = null;
    }

    @Test
    void testSignUpCustomer() {
        String result = signUpService.registerUser("Mario", "Rossi", customerEmail, "securePass321", "3216549870", "");
        assertEquals("success", result);
        testCustomer = userDAO.findByEmail(customerEmail);
        assertNotNull(testCustomer);
        assertTrue(testCustomer.getUserType() == User.UserType.CUSTOMER);
    }

    @Test
    void testSignUpBarber() {
        String result = signUpService.registerUser("Mario", "Rossi", barberEmail, "securePass654", "3344556677", "I-AM-A-BARBER");
        assertEquals("success", result);

        testBarber = userDAO.findByEmail(barberEmail);
        assertNotNull(testBarber);
        assertTrue(testBarber.getUserType() == User.UserType.BARBER);
    }

    @Test
    void testSignInCustomer() {
        signUpService.registerUser("Mario", "Rossi", customerEmail, "securePass321", "3216549870", "");

        boolean isAuthenticated = signInService.authenticateUser(customerEmail, "securePass321");
        assertTrue(isAuthenticated);
        assertNotNull(sessionManager.getCurrentUser());
        assertTrue(sessionManager.getCurrentUser().getUserType() == User.UserType.CUSTOMER);
    }

    @Test
    void testSignInBarber() {
        signUpService.registerUser("Mario", "Rossi", barberEmail, "securePass654", "3344556677", "I-AM-A-BARBER");

        boolean isAuthenticated = signInService.authenticateUser(barberEmail, "securePass654");
        assertTrue(isAuthenticated);
        assertNotNull(sessionManager.getCurrentUser());
        assertTrue(sessionManager.getCurrentUser().getUserType() == User.UserType.BARBER);
    }

    @Test
    void testInvalidSignInCustomer() {
        boolean isAuthenticated = signInService.authenticateUser("notExistingEmail", "wrongPassword");
        assertFalse(isAuthenticated);

        assertThrows(IllegalStateException.class, () -> {
            sessionManager.getCurrentUser();
        });
    }
}