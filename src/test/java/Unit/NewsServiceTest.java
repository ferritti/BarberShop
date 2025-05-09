package Unit;

import Authentication.SessionManager;
import Services.NewsService;
import Persistence.DAO.NewsDAO;
import Model.Notification;
import Model.Barber;
import Model.Customer;
import Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceTest {
    private NewsDAO newsDAO;
    private NewsService newsService;
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        newsDAO = mock(NewsDAO.class);
        sessionManager = mock(SessionManager.class);
        newsService = new NewsService(newsDAO, sessionManager);
    }

    @Test
    void getNewsBarber() {
        Barber barber = new Barber("Mario", "Rossi", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Notification> barberNews = Arrays.asList(
                new Notification("Title1", "Message1", barber, false),
                new Notification("Title2", "Message2", barber, false)
        );
        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(barberNews);

        List<Notification> result = newsService.getNews();
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
    }

    @Test
    void getNewsCustomer() {
        User customer = new Customer("Luigi", "Bianchi", "customer@example.com", "password", "987654321");
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        List<Notification> customerNews = Arrays.asList(
                new Notification("TitleA", "MessageA", true)
        );
        when(newsDAO.getAllCustomerNews()).thenReturn(customerNews);

        List<Notification> result = newsService.getNews();
        assertEquals(1, result.size());
        assertEquals("TitleA", result.get(0).getTitle());
    }

    @Test
    void getNewsEmpty() {
        User customer = new Customer("Luigi", "Bianchi", "customer@example.com", "password", "987654321");
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        when(newsDAO.getAllCustomerNews()).thenReturn(Collections.emptyList());

        List<Notification> result = newsService.getNews();
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteOldNewsLessThanLimit() {
        User barber = new Barber("Mario", "Rossi", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Notification> news = Arrays.asList(
                new Notification("Title1", "Message1", false),
                new Notification("Title2", "Message2", false)
        );
        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(news);

        newsService.deleteOldestNewsIfNecessary();

        verify(newsDAO, never()).deleteNotification(any());
    }

    @Test
    void deleteOldNewsMoreThanLimit() {
        User barber = new Barber("Mario", "Rossi", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Notification> news = new ArrayList<>();
        LocalTime now = LocalTime.now();
        for (int i = 0; i < 35; i++) {
            Notification notification = new Notification("Title" + i, "Message" + i, false);
            notification.setTime(now.minusMinutes(i));
            news.add(notification);
        }

        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(news);

        newsService.deleteOldestNewsIfNecessary();

        verify(newsDAO, times(5)).deleteNotification(argThat(
                notification -> news.subList(0, 5).contains(notification)
        ));

        verify(newsDAO, never()).deleteNotification(argThat(
                notification -> news.subList(5, 35).contains(notification)
        ));
    }
}
