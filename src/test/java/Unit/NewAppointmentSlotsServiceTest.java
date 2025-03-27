package Unit;

import Business.NewAppointmentSlotsService;
import DBconnection.DAO.*;
import Model.*;
import Authentication.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewAppointmentSlotsServiceTest {
    private UserDAO userDAO;
    private ServiceTypeDAO serviceTypeDAO;
    private AvailableSlotDAO availableSlotDAO;
    private AppointmentDAO appointmentDAO;
    private NewsDAO newsDAO;
    private SessionManager sessionManager;
    private NewAppointmentSlotsService service;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        serviceTypeDAO = mock(ServiceTypeDAO.class);
        availableSlotDAO = mock(AvailableSlotDAO.class);
        appointmentDAO = mock(AppointmentDAO.class);
        newsDAO = mock(NewsDAO.class);
        sessionManager = mock(SessionManager.class);
        service = new NewAppointmentSlotsService(userDAO, serviceTypeDAO, availableSlotDAO, appointmentDAO, newsDAO, sessionManager);
    }

    @Test
    void testGetBarbersData() {
        HashMap<String, String> mockBarbers = new HashMap<>();
        mockBarbers.put("barber1@example.com", "Barber One");
        when(userDAO.getBarbersData()).thenReturn(mockBarbers);

        HashMap<String, String> result = service.getBarbersData();

        assertEquals(mockBarbers, result);
        verify(userDAO, times(1)).getBarbersData();
    }

    @Test
    void testGetServicesData() {
        HashMap<String, Double> mockServices = new HashMap<>();
        mockServices.put("Haircut", 25.0);
        when(serviceTypeDAO.getServices()).thenReturn(mockServices);

        HashMap<String, Double> result = service.getServicesData();

        assertEquals(mockServices, result);
        verify(serviceTypeDAO, times(1)).getServices();
    }

    @Test
    void testGetAvailableSlots() {
        LocalDate date = LocalDate.now();
        String barberEmail = "barber@example.com";
        List<AvailableSlot> mockSlots = List.of(mock(AvailableSlot.class));
        when(availableSlotDAO.getAvSlotsAtSelectedDate(date, barberEmail)).thenReturn(mockSlots);

        List<AvailableSlot> result = service.getAvailableSlots(barberEmail, date);

        assertEquals(mockSlots, result);
        verify(availableSlotDAO, times(1)).getAvSlotsAtSelectedDate(date, barberEmail);
    }

    @Test
    void testGetAppointments() {
        // Utilizziamo un utente mock con un'email
        Model.User mockUser = mock(Model.User.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn("user@example.com");

        List<Appointment> mockAppointments = List.of(mock(Appointment.class));
        when(appointmentDAO.findByEmailOfUser("user@example.com")).thenReturn(mockAppointments);

        List<Appointment> result = service.getAppointments();

        assertEquals(mockAppointments, result);
        verify(appointmentDAO, times(1)).findByEmailOfUser("user@example.com");
    }

    @Test
    void testRemoveAvSlot() {
        AvailableSlot slot = mock(AvailableSlot.class);
        when(availableSlotDAO.removeAvSlot(slot)).thenReturn(true);

        boolean result = service.removeAvSlot(slot);

        assertTrue(result);
        verify(availableSlotDAO, times(1)).removeAvSlot(slot);
    }

    @Test
    void testAddAppointment() {
        Appointment appointment = mock(Appointment.class);
        when(appointmentDAO.addAppointment(appointment)).thenReturn(true);

        boolean result = service.addAppointment(appointment);

        assertTrue(result);
        verify(appointmentDAO, times(1)).addAppointment(appointment);
    }

    @Test
    void testIsSameDateTime() {
        Appointment appointment = mock(Appointment.class);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        when(appointment.getDate()).thenReturn(date);
        when(appointment.getTime()).thenReturn(time);

        boolean result = service.isSameDateTime(appointment, date, time);

        assertTrue(result);
        verify(appointment, times(1)).getDate();
        verify(appointment, times(1)).getTime();
    }

    @Test
    void testIsSameDateTime_WhenDateIsNotSame_ReturnFalse() {
        // Mock dell'appuntamento
        Appointment appointment = mock(Appointment.class);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Simuliamo un appuntamento con una data e/o ora diversa
        when(appointment.getDate()).thenReturn(date.plusDays(1));
        when(appointment.getTime()).thenReturn(time.plusHours(1)); // Ora diversa

        // Esegui il test
        boolean result = service.isSameDateTime(appointment, date, time);

        // Il risultato deve essere falso perché la data e/o l'ora non coincidono
        assertFalse(result);
        verify(appointment, times(1)).getDate();
    }

    @Test
    void testIsSameDateTime_WhenTimeIsNotSame_ReturnFalse() {
        // Mock dell'appuntamento
        Appointment appointment = mock(Appointment.class);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Simuliamo un appuntamento con una data e/o ora diversa
        when(appointment.getDate()).thenReturn(date);
        when(appointment.getTime()).thenReturn(time.plusHours(1)); // Ora diversa

        // Esegui il test
        boolean result = service.isSameDateTime(appointment, date, time);

        // Il risultato deve essere falso perché la data e/o l'ora non coincidono
        assertFalse(result);
        verify(appointment, times(1)).getDate();
        verify(appointment, times(1)).getTime();

    }

//    @Test
//    void testAddNotification() {
//        // Mock statico di SessionManager
//        try (MockedStatic<SessionManager> mockedSessionManager = mockStatic(SessionManager.class)) {
//            // Simuliamo il comportamento di SessionManager
//            User currentUser = mock(User.class);
//            mockedSessionManager.when(SessionManager::getInstance).thenReturn(sessionManager);
//            when(sessionManager.getCurrentUser()).thenReturn(currentUser);
//            when(currentUser.getName()).thenReturn("John");
//            when(currentUser.getSurname()).thenReturn("Doe");
//
//            // Mock della notifica
//            String barberEmail = "barber@example.com";
//            Notification notification = new Notification(
//                    "New Appointment",
//                    "John Doe has booked an appointment with you",
//                    barberEmail,
//                    false
//            );
//
//            // Simuliamo che newsDAO aggiunga la notifica correttamente
//            when(newsDAO.addNotification(notification)).thenReturn(true);
//
//            // Eseguiamo il test
//            boolean result = service.addNotification(barberEmail);
//
//            // Verifica che il risultato sia true
//            assertTrue(result);
//
//            // Verifica che il metodo addNotification di newsDAO venga chiamato una volta con il parametro corretto
//            verify(newsDAO, times(1)).addNotification(eq(notification));
//        }
//    }
//
//
//    @Test
//    void testAddNotification_Failure() {
//        // Mock dell'utente nella sessione
//        Customer mockCustomer = new Customer("Mario", "Rossi", "mario.rossi@email.com", "password", "123456789");
//        when(sessionManager.getInstance()).thenReturn(sessionManager);
//        when(sessionManager.getCurrentUser()).thenReturn(mockCustomer);
//
//        // Mock dell'operazione di salvataggio fallita
//        when(newsDAO.addNotification(any(Notification.class))).thenReturn(false);
//
//        // Esegui il test
//        boolean result = service.addNotification("barber@email.com");
//
//        // Verifica il risultato
//        assertFalse(result);
//        verify(newsDAO, times(1)).addNotification(any(Notification.class));
//    }

}