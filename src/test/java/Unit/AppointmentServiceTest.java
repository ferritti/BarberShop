package Unit;

import Payment.PaymentMethod;
import Authentication.SessionManager;
import Business.AppointmentService;
import DBconnection.DAO.AppointmentDAO;
import DBconnection.DAO.AvailableSlotDAO;
import DBconnection.DAO.NewsDAO;
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

    @BeforeEach
    void setUp() {
        appointmentDAO = mock(AppointmentDAO.class);
        availableSlotDAO = mock(AvailableSlotDAO.class);
        newsDAO = mock(NewsDAO.class);
        sessionManager = mock(SessionManager.class);
        appointmentService = new AppointmentService(appointmentDAO, availableSlotDAO, newsDAO, sessionManager);
    }

    @Test
    void getAppointments_AsBarber_ShouldReturnAppointments() {
        User barber = new Barber("John", "Doe", "barber@example.com", "password", "123456789");
        when(sessionManager.getCurrentUser()).thenReturn(barber);

        List<Appointment> appointments = Arrays.asList(
                new Appointment(PaymentMethod.CREDIT_CARD, "Haircut", "John Doe", "barber@example.com",
                        "customer1@example.com", "1234567890", LocalTime.of(10, 0), LocalDate.of(2024, 3, 25), 25.0),
                new Appointment(PaymentMethod.PAYPAL, "Shave", "John Doe", "barber@example.com",
                        "customer2@example.com", "0987654321", LocalTime.of(11, 0), LocalDate.of(2024, 3, 26), 15.0)
        );
        when(appointmentDAO.findByEmailOfUser(barber.getEmail())).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointments();
        assertEquals(2, result.size());
        assertEquals("customer1@example.com", result.get(0).getCustomerEmail());
        assertEquals("customer2@example.com", result.get(1).getCustomerEmail());
    }

    @Test
    void getAppointments_AsCustomer_ShouldReturnAppointments() {
        User customer = new Customer("Jane", "Doe", "customer@example.com", "password", "987654321");
        when(sessionManager.getCurrentUser()).thenReturn(customer);

        List<Appointment> appointments = Arrays.asList(
                new Appointment(PaymentMethod.SHOP, "Beard Trim", "John Doe", "barber@example.com",
                        "customer@example.com", "9876543210", LocalTime.of(14, 30), LocalDate.of(2024, 4, 1), 20.0)
        );
        when(appointmentDAO.findByEmailOfUser(customer.getEmail())).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointments();
        assertEquals(1, result.size());
        assertEquals("barber@example.com", result.get(0).getBarberEmail());
    }

    @Test
    void isPastAppointment_ShouldReturnTrueForPastAppointment() {
        Appointment pastAppointment = new Appointment(PaymentMethod.SHOP, "Haircut", "John Doe", "barber@example.com",
                "customer@example.com", "1234567890", LocalTime.of(9, 0), LocalDate.of(2023, 1, 1), 25.0);

        assertTrue(AppointmentService.isPastAppointment(pastAppointment));
    }

    @Test
    void addAvailableSlot_ShouldCallDAO() {
        Appointment appointment = new Appointment(PaymentMethod.PAYPAL, "Haircut", "John Doe", "barber@example.com",
                "customer@example.com", "1234567890", LocalTime.of(15, 0), LocalDate.of(2024, 4, 1), 25.0);

        appointmentService.addAvailableSlot(appointment);

        verify(availableSlotDAO, times(1)).addAvSlot(any(AvailableSlot.class));
    }

    @Test
    void deleteAppointment_ShouldReturnTrueOnSuccess() {
        Appointment appointment = new Appointment(PaymentMethod.PAYPAL, "Shave", "John Doe", "barber@example.com",
                "customer@example.com", "0987654321", LocalTime.of(16, 0), LocalDate.of(2024, 4, 1), 15.0);

        when(appointmentDAO.deleteAppointment(appointment)).thenReturn(true);

        assertTrue(appointmentService.deleteAppointment(appointment));
        verify(appointmentDAO, times(1)).deleteAppointment(appointment);
    }

    @Test
    void deleteAppointment_ShouldReturnFalseOnFailure() {
        Appointment appointment = new Appointment(PaymentMethod.PAYPAL, "Shave", "John Doe", "barber@example.com",
                "customer@example.com", "0987654321", LocalTime.of(16, 0), LocalDate.of(2024, 4, 1), 15.0);

        when(appointmentDAO.deleteAppointment(appointment)).thenReturn(false);

        assertFalse(appointmentService.deleteAppointment(appointment));
    }

    @Test
    void addNotification_ShouldCallDAO() {
        Appointment appointment = new Appointment(PaymentMethod.PAYPAL, "Beard Trim", "John Doe", "barber@example.com",
                "customer@example.com", "9876543210", LocalTime.of(12, 0), LocalDate.of(2024, 4, 1), 20.0);

        appointmentService.addNotification(appointment);

        verify(newsDAO, times(1)).addNotification(any(Notification.class));
    }

}