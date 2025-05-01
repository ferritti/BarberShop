package IntegrationTests;

import Authentication.SessionManager;
import Business.ProfileService;
import Model.Customer;
import Model.User;
import Persistence.DAO.ConcreteUserDAO;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    private ConcreteUserDAO userDAO;
    private ProfileService profileService;
    private static final String TEST_EMAIL = "test.integration@example.com";

    @BeforeEach
    public void setUp() {
        userDAO = new ConcreteUserDAO();
        profileService = new ProfileService();
        userDAO.removeUserByEmail(TEST_EMAIL);
    }

    @AfterEach
    public void tearDown() {
        SessionManager.getInstance().closeSession();
        userDAO.removeUserByEmail(TEST_EMAIL);
    }

    @Test
    public void testGetUserData() {
        Customer testUser = new Customer("Test", "Integration", TEST_EMAIL, "password123", "3331234567");
        userDAO.addUser(testUser);
        User fromDb = userDAO.findByEmail(TEST_EMAIL);
        SessionManager.getInstance().setCurrentUser(fromDb);

        Map<String, String> data = profileService.getUserData();
        assertEquals("Test", data.get("name"));
        assertEquals("Integration", data.get("surname"));
        assertEquals(TEST_EMAIL, data.get("email"));
        assertEquals("3331234567", data.get("phone"));
    }

    @Test
    public void testLogout() {
        Customer testUser = new Customer("Test", "Integration", TEST_EMAIL, "password123", "3331234567");
        userDAO.addUser(testUser);
        User fromDb = userDAO.findByEmail(TEST_EMAIL);
        SessionManager.getInstance().setCurrentUser(fromDb);

        profileService.logout();

        assertThrows(IllegalStateException.class, () -> {
            SessionManager.getInstance().getCurrentUser();
        });

        assertThrows(IllegalStateException.class, () -> {
            profileService.getUserData();
        });
    }
}
