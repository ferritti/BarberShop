package Unit;

import Authentication.SessionManager;
import Business.NewsService;
import DBconnection.DAO.NewsDAO;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void getNews_AsBarber_ShouldReturnBarberNews() {
        User barber = new Barber("John", "Doe", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Notification> barberNews = Arrays.asList(
                new Notification("Title1", "Message1", null),
                new Notification("Title2", "Message2", null)
        );
        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(barberNews);

        List<Notification> result = newsService.getNews();
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
    }

    @Test
    void getNews_AsCustomer_ShouldReturnCustomerNews() {
        User customer = new Customer("Jane", "Doe", "customer@example.com", "password", "987654321");
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        List<Notification> customerNews = Arrays.asList(
                new Notification("TitleA", "MessageA", null)
        );
        when(newsDAO.getAllCustomerNews()).thenReturn(customerNews);

        List<Notification> result = newsService.getNews();
        assertEquals(1, result.size());
        assertEquals("TitleA", result.get(0).getTitle());
    }

    @Test
    void getNews_WithNoNotifications_ShouldReturnEmptyList() {
        User customer = new Customer("Jane", "Doe", "customer@example.com", "password", "987654321");
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        when(newsDAO.getAllCustomerNews()).thenReturn(Collections.emptyList());

        List<Notification> result = newsService.getNews();
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteOldestNewsIfNecessary_WhenNewsAreLessThanLimit_ShouldDoNothing() {
        User barber = new Barber("John", "Doe", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Notification> news = Arrays.asList(
                new Notification("Title1", "Message1", null),
                new Notification("Title2", "Message2", null)
        );
        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(news);

        newsService.deleteOldestNewsIfNecessary();

        verify(newsDAO, never()).deleteNotification(any());
    }

    @Test
    void deleteOldestNewsIfNecessary_WhenNewsAreMoreThanLimit_ShouldDeleteOldestNews() {
        // Preparazione dati
        User barber = new Barber("John", "Doe", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        // Creo una lista di 35 notifiche
        List<Notification> news = new ArrayList<>();
        LocalTime now = LocalTime.now();
        for (int i = 0; i < 35; i++) {
            Notification notification = new Notification("Title" + i, "Message" + i, null);
            // Imposto tempi leggermente diversi partendo dall'ora corrente
            notification.setTime(now.minusMinutes(i));
            news.add(notification);
        }

        when(newsDAO.getAllBarberNews(barber.getEmail())).thenReturn(news);

        // Esecuzione del metodo da testare
        newsService.deleteOldestNewsIfNecessary();

        // Verifiche
        verify(newsDAO, times(5)).deleteNotification(argThat(
                notification -> news.subList(0, 5).contains(notification)
        ));

        verify(newsDAO, never()).deleteNotification(argThat(
                notification -> news.subList(5, 35).contains(notification)
        ));
    }
}