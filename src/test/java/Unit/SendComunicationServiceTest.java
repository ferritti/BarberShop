package Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Business.SendComunicationService;
import Persistence.DAO.NewsDAO;
import Model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SendComunicationServiceTest {

    private NewsDAO newsDAO;
    private SendComunicationService sendComunicationService;

    @BeforeEach
    void setUp() {
        newsDAO = mock(NewsDAO.class);
        sendComunicationService = new SendComunicationService(newsDAO);
    }

    @Test
    void testAreEmptyFields_WhenFieldsAreEmpty_ShouldReturnTrue() {
        assertTrue(sendComunicationService.areEmptyFields("", "message"));
        assertTrue(sendComunicationService.areEmptyFields("title", ""));
        assertTrue(sendComunicationService.areEmptyFields("", ""));
    }

    @Test
    void testAreEmptyFields_WhenFieldsAreNotEmpty_ShouldReturnFalse() {
        assertFalse(sendComunicationService.areEmptyFields("Title", "Message"));
    }

    @Test
    void testAddComunication_SuccessfulNotificationAddition() {
        when(newsDAO.addNotification(any(Notification.class))).thenReturn(true);

        boolean result = sendComunicationService.addComunication("New Title", "New Message");

        assertTrue(result);
        verify(newsDAO, times(1)).addNotification(any(Notification.class));
    }

    @Test
    void testAddComunication_FailedNotificationAddition() {
        when(newsDAO.addNotification(any(Notification.class))).thenReturn(false);

        boolean result = sendComunicationService.addComunication("New Title", "New Message");

        assertFalse(result);
        verify(newsDAO, times(1)).addNotification(any(Notification.class));
    }
}