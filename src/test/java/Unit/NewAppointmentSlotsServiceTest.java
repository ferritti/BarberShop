package Unit;

import Business.NewAppointmentSlotsService;
import Persistence.DAO.*;
import Model.*;
import Authentication.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void getBarbersDataTest() {
        HashMap<String, String> mockBarbers = new HashMap<>();
        mockBarbers.put("barber1@example.com", "Barber One");
        when(userDAO.getBarbersData()).thenReturn(mockBarbers);

        HashMap<String, String> result = service.getBarbersData();

        assertEquals(mockBarbers, result);
        verify(userDAO, times(1)).getBarbersData();
    }

    @Test
    void getServicesDataTest() {
        HashMap<String, Double> mockServices = new HashMap<>();
        mockServices.put("Haircut", 25.0);
        when(serviceTypeDAO.getServicePricesMap()).thenReturn(mockServices);

        HashMap<String, Double> result = service.getServicesData();

        assertEquals(mockServices, result);
        verify(serviceTypeDAO, times(1)).getServicePricesMap();
    }

    @Test
    void getAvailableSlotsTest() {
        LocalDate date = LocalDate.now();
        String barberEmail = "barber@example.com";
        List<AvailableSlot> mockSlots = List.of(mock(AvailableSlot.class));
        when(availableSlotDAO.getAvSlotsAtSelectedDate(date, barberEmail)).thenReturn(mockSlots);

        List<AvailableSlot> result = service.getAvailableSlots(barberEmail, date);

        assertEquals(mockSlots, result);
        verify(availableSlotDAO, times(1)).getAvSlotsAtSelectedDate(date, barberEmail);
    }

    @Test
    void getAppointmentsAsBarberTest() {
        User mockUser = mock(User.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn("barber@example.com");
        when(mockUser.getUserType()).thenReturn(User.UserType.BARBER);

        List<Appointment> mockAppointments = List.of(mock(Appointment.class));
        when(appointmentDAO.findByEmailOfBarber("barber@example.com")).thenReturn(mockAppointments);

        List<Appointment> result = service.getAppointments();

        assertEquals(mockAppointments, result);
        verify(appointmentDAO, times(1)).findByEmailOfBarber("barber@example.com");
        verify(appointmentDAO, never()).findByEmailOfCustomer(anyString());
    }

    @Test
    void getAppointmentsAsCustomerTest() {
        User mockUser = mock(User.class);
        when(sessionManager.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn("customer@example.com");
        when(mockUser.getUserType()).thenReturn(User.UserType.CUSTOMER);

        List<Appointment> mockAppointments = List.of(mock(Appointment.class));
        when(appointmentDAO.findByEmailOfCustomer("customer@example.com")).thenReturn(mockAppointments);

        List<Appointment> result = service.getAppointments();

        assertEquals(mockAppointments, result);
        verify(appointmentDAO, times(1)).findByEmailOfCustomer("customer@example.com");
        verify(appointmentDAO, never()).findByEmailOfBarber(anyString());
    }

    @Test
    void isSameDateTimeTest() {
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
    void isSameDateTimeDifferentDateTest() {
        Appointment appointment = mock(Appointment.class);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        when(appointment.getDate()).thenReturn(date.plusDays(1));
        when(appointment.getTime()).thenReturn(time.plusHours(1));

        boolean result = service.isSameDateTime(appointment, date, time);

        assertFalse(result);
        verify(appointment, times(1)).getDate();
    }

    @Test
    void isSameDateTimeDifferentTimeTest() {
        Appointment appointment = mock(Appointment.class);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        when(appointment.getDate()).thenReturn(date);
        when(appointment.getTime()).thenReturn(time.plusHours(1));

        boolean result = service.isSameDateTime(appointment, date, time);

        assertFalse(result);
        verify(appointment, times(1)).getDate();
        verify(appointment, times(1)).getTime();
    }
}