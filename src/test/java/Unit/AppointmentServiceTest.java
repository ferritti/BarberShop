package Unit;

import Model.PaymentMethod;
import Authentication.SessionManager;
import Services.AppointmentService;
import Persistence.DAO.AppointmentDAO;
import Persistence.DAO.AvailableSlotDAO;
import Persistence.DAO.NewsDAO;
import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    private AppointmentDAO appointmentDAO;
    private AvailableSlotDAO availableSlotDAO;
    private NewsDAO newsDAO;
    private SessionManager sessionManager;
    private AppointmentService appointmentService;
    private Barber barber;
    private Customer customer;
    private Customer customer2;
    private ServiceType serviceType;


    @BeforeEach
    void setUp() {
        appointmentDAO = mock(AppointmentDAO.class);
        availableSlotDAO = mock(AvailableSlotDAO.class);
        newsDAO = mock(NewsDAO.class);
        sessionManager = mock(SessionManager.class);
        appointmentService = new AppointmentService(appointmentDAO, availableSlotDAO, newsDAO, sessionManager);
        barber = mock(Barber.class);
        when(barber.getEmail()).thenReturn("barber@example.com");
        when(barber.getUserType()).thenReturn(User.UserType.BARBER);
        customer = mock(Customer.class);
        when(customer.getEmail()).thenReturn("customer@example.com");
        when(customer.getUserType()).thenReturn(User.UserType.CUSTOMER);
        serviceType = mock(ServiceType.class);
        customer2 = mock(Customer.class);
        when(customer2.getEmail()).thenReturn("cliente2@example.com");
        when(customer2.getUserType()).thenReturn(User.UserType.CUSTOMER);
        when(serviceType.getName()).thenReturn("Haircut");
        when(serviceType.getPrice()).thenReturn(25.0);
    }

    @Test
    void getAppointmentsBarber() {
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Appointment> appointments = Arrays.asList(
                new Appointment(LocalDate.of(2024, 3, 25), LocalTime.of(10, 0), customer, barber, serviceType, PaymentMethod.CREDIT_CARD),
                new Appointment( LocalDate.of(2024, 3, 26), LocalTime.of(11, 0), customer2, barber, serviceType, PaymentMethod.PAYPAL)
        );
        when(appointmentDAO.findByEmailOfBarber(barber.getEmail())).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointments();
        assertEquals(2, result.size());
        assertEquals("customer@example.com", result.get(0).getCustomer().getEmail());
        assertEquals("cliente2@example.com", result.get(1).getCustomer().getEmail());
    }

    @Test
    void getAppointmentsCustomer() {
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        List<Appointment> appointments = Arrays.asList(
                new Appointment( LocalDate.of(2024, 4, 1), LocalTime.of(14, 30), customer, barber, serviceType, PaymentMethod.SHOP)
        );
        when(appointmentDAO.findByEmailOfCustomer(customer.getEmail())).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointments();
        assertEquals(1, result.size());
        assertEquals("barber@example.com", result.get(0).getBarber().getEmail());
    }

    @Test
    void isPastAppointmentTrue() {
        Appointment pastAppointment = new Appointment(LocalDate.of(2023, 1, 1), LocalTime.of(9, 0), customer, barber, serviceType, PaymentMethod.SHOP);
        assertTrue(AppointmentService.isPastAppointment(pastAppointment));
    }

    @Test
    void addAvailableSlotCallsDao() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 4, 1), LocalTime.of(15, 0), customer, barber, serviceType, PaymentMethod.PAYPAL);
        appointmentService.addAvailableSlot(appointment);

        verify(availableSlotDAO, times(1)).addAvSlot(any(AvailableSlot.class));
    }

    @Test
    void deleteAppointmentSuccess() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 4, 1), LocalTime.of(16, 0), customer, barber, serviceType, PaymentMethod.PAYPAL);

        when(appointmentDAO.deleteAppointment(appointment)).thenReturn(true);

        assertTrue(appointmentService.deleteAppointment(appointment));
        verify(appointmentDAO, times(1)).deleteAppointment(appointment);
    }

    @Test
    void deleteAppointmentFailure() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 4, 1),LocalTime.of(16, 0), customer, barber, serviceType, PaymentMethod.PAYPAL);

        when(appointmentDAO.deleteAppointment(appointment)).thenReturn(false);

        assertFalse(appointmentService.deleteAppointment(appointment));
    }

    @Test
    void addNotificationCallsDao() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 4, 1), LocalTime.of(12, 0), customer, barber, serviceType, PaymentMethod.PAYPAL);

        appointmentService.addNotification(appointment);

        verify(newsDAO, times(1)).addNotification(any(Notification.class));
    }

}