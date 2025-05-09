package IntegrationTests;

import Services.*;
import Persistence.DAO.*;
import Model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CommunicationTest {

    private SendComunicationService sendComunicationService;
    private ConcreteNewsDAO newsDAO;

    private final String testTitle = "Test Notification Title";
    private final String testMessage = "This is a test notification for customers.";

    @BeforeEach
    void setUp() {
        sendComunicationService = new SendComunicationService();
        newsDAO = new ConcreteNewsDAO();
    }

    @Test
    void testAddMultipleAndDeleteComunications() {
        String testTitle1 = "First Notification";
        String testMessage1 = "First test message.";
        String testTitle2 = "Second Notification";
        String testMessage2 = "Second test message.";

        boolean result1 = sendComunicationService.addComunication(testTitle1, testMessage1);
        boolean result2 = sendComunicationService.addComunication(testTitle2, testMessage2);

        assertTrue(result1);
        assertTrue(result2);

        Notification notification1 = newsDAO.getAllCustomerNews().stream()
                .filter(n -> n.getTitle().equals(testTitle1) && n.getMessage().equals(testMessage1))
                .findFirst()
                .orElse(null);
        Notification notification2 = newsDAO.getAllCustomerNews().stream()
                .filter(n -> n.getTitle().equals(testTitle2) && n.getMessage().equals(testMessage2))
                .findFirst()
                .orElse(null);

        assertNotNull(notification1);
        assertNotNull(notification2);

        newsDAO.deleteNotification(notification1);
        newsDAO.deleteNotification(notification2);

        Notification deletedNotification1 = newsDAO.getAllCustomerNews().stream()
                .filter(n -> n.getTitle().equals(testTitle1) && n.getMessage().equals(testMessage1))
                .findFirst()
                .orElse(null);
        Notification deletedNotification2 = newsDAO.getAllCustomerNews().stream()
                .filter(n -> n.getTitle().equals(testTitle2) && n.getMessage().equals(testMessage2))
                .findFirst()
                .orElse(null);

        assertNull(deletedNotification1);
        assertNull(deletedNotification2);
    }
}
