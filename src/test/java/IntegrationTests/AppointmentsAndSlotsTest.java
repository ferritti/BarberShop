package IntegrationTests;
// finire questa classe, poi integrazione credo siano finiti. fare il test di unità di
// profile service e verificare se ne vanno fatti altri di unità che mancavano.
import Business.*;
import Payment.PaymentMethod;
import Persistence.DAO.*;
import Model.*;
import Authentication.SessionManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentsAndSlotsTest {

    private NewAppointmentSlotsService newAppointmentSlotsService;
    private AppointmentService appointmentService;
    private UserDAO userDAO;
    private AppointmentDAO appointmentDAO;
    private AvailableSlotDAO availableSlotDAO;
    private NewsDAO newsDAO;
    private SessionManager sessionManager;

    private User testCustomer;
    private User testBarber;
    private final String customerEmail = "customer@example.com";
    private final String barberEmail = "barber@example.com";

    private final String testService = "Haircut";
    private final LocalDate testDate = LocalDate.of(2025, 5, 10);
    private final LocalTime testTime = LocalTime.of(10, 0);

    @BeforeEach
    void setUp() {
        userDAO = new ConcreteUserDAO();
        appointmentDAO = new ConcreteAppointmentDAO();
        availableSlotDAO = new ConcreteAvailableSlotDAO();
        newsDAO = new ConcreteNewsDAO();
        sessionManager = SessionManager.getInstance();

        newAppointmentSlotsService = new NewAppointmentSlotsService(userDAO,
                new ConcreteServiceTypeDAO(), availableSlotDAO, appointmentDAO, newsDAO, sessionManager);
        appointmentService = new AppointmentService(appointmentDAO, availableSlotDAO, newsDAO, sessionManager);

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

        List<Appointment> app = appointmentDAO.findByEmailOfUser(customerEmail);
        if (app != null) {
            for (Appointment appointment : app) {
                appointmentDAO.deleteAppointment(appointment);
            }
        }

        availableSlotDAO.removeAvSlot(new AvailableSlot(barberEmail, testDate, testTime));
    }

    @Test
    void testBookAppointmentSuccessfully() {
        signUpTestCustomer();
        signUpTestBarber();
        signInAsCustomer();

        // Pre-condizione: slot disponibile
        List<AvailableSlot> availableSlotsBefore = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        assertTrue(availableSlotsBefore.stream().anyMatch(slot -> slot.getStartTime().equals(testTime)));

        // Prenotazione
        boolean bookingResult = newAppointmentSlotsService.bookAppointment(testBarber.getName(), testService, testDate, testTime, PaymentMethod.CREDIT_CARD);
        assertTrue(bookingResult);

        // Verifica prenotazione
        List<Appointment> appointments = appointmentService.getAppointments();
        assertEquals(1, appointments.size());
        Appointment bookedAppointment = appointments.get(0);
        assertEquals(testCustomer.getEmail(), bookedAppointment.getCustomerEmail());

        // Slot rimosso dal DB
        List<AvailableSlot> availableSlotsAfter = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        assertFalse(availableSlotsAfter.stream().anyMatch(slot -> slot.getStartTime().equals(testTime)));
    }

    @Test
    void testDeleteAppointmentAndRestoreSlot() {
        signUpTestCustomer();
        signUpTestBarber();
        signInAsCustomer();

        // Prenotazione
        boolean bookingResult = newAppointmentSlotsService.bookAppointment(testBarber.getName(), testService, testDate, testTime, PaymentMethod.CREDIT_CARD);
        assertTrue(bookingResult);

        // Controlla slot rimosso
        List<AvailableSlot> slotsAfterBooking = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        assertFalse(slotsAfterBooking.stream().anyMatch(slot -> slot.getStartTime().equals(testTime)));

        // Elimina appuntamento
        List<Appointment> appointments = appointmentService.getAppointments();
        assertEquals(1, appointments.size());
        Appointment appointmentToDelete = appointments.get(0);
        boolean deleteResult = appointmentService.deleteAppointment(appointmentToDelete);
        assertTrue(deleteResult);

        // Verifica appuntamento rimosso
        appointments = appointmentService.getAppointments();
        assertTrue(appointments.isEmpty());

        // Lo slot deve essere stato rimesso
        List<AvailableSlot> slotsAfterDelete = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        assertTrue(slotsAfterDelete.stream().anyMatch(slot -> slot.getStartTime().equals(testTime)));
    }

    private void signUpTestCustomer() {
        SignUpService signUpService = new SignUpService(userDAO);
        String result = signUpService.registerUser("Mario", "Rossi", customerEmail, "securePass321", "3216549870", "");
        assertEquals("success", result);
        testCustomer = userDAO.findByEmail(customerEmail);

        // Aggiungo uno slot per il test
        availableSlotDAO.addAvSlot(new AvailableSlot(barberEmail, testDate, testTime));
    }

    private void signUpTestBarber() {
        SignUpService signUpService = new SignUpService(userDAO);
        String result = signUpService.registerUser("Luigi", "Bianchi", barberEmail, "barberPass123", "3344556677", "I-AM-A-BARBER");
        assertEquals("success", result);
        testBarber = userDAO.findByEmail(barberEmail);
    }

    private void signInAsCustomer() {
        SignInService signInService = new SignInService(userDAO, sessionManager);
        boolean isAuthenticated = signInService.authenticateUser(customerEmail, "securePass321");
        assertTrue(isAuthenticated);
        assertNotNull(sessionManager.getCurrentUser());
    }
}
