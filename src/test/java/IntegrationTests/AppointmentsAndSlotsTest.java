package IntegrationTests;

import Authentication.SessionManager;
import Business.*;
import Model.*;
import Model.PaymentMethod;
import Persistence.DAO.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointmentsAndSlotsTest {

    private NewAppointmentSlotsService newAppointmentSlotsService;
    private AppointmentService appointmentService;
    private SignInService signInService;
    private ConcreteServiceTypeDAO concreteServiceTypeDAO;
    private ConcreteNewsDAO newsDAO;
    private Barber barber;
    private Customer customer;
    private ServiceType testService;
    private final LocalDate testDate = LocalDate.now();
    private final LocalTime testTime = LocalTime.of(10, 0);

    @BeforeAll
    public void setup() {
        barber = new Barber("Mario", "Rossi", "mario.rossi@gmail.com", "password123", "3331234567");
        customer = new Customer("Luigi", "Verdi", "luigi.verdi@gmail.com", "password456", "3337654321");
        testService = new ServiceType("Taglio", 20.0);
        newAppointmentSlotsService = new NewAppointmentSlotsService();
        appointmentService = new AppointmentService();
        SignUpService signUpService = new SignUpService();
        signInService = new SignInService();
        concreteServiceTypeDAO = new ConcreteServiceTypeDAO();
        newsDAO = new ConcreteNewsDAO();

        signUpService.registerUser(barber.getName(), barber.getSurname(), barber.getEmail(), "password123", barber.getPhone(), "I-AM-A-BARBER");

        signUpService.registerUser(customer.getName(), customer.getSurname(), customer.getEmail(), "password456", customer.getPhone(), "");

        signInService.authenticateUser(customer.getEmail(), "password456");

        concreteServiceTypeDAO.addServiceType(testService);
    }

    @Test
    public void testBookAppointmentAndSlotRemoval() {
        // verifica presenza slot
        AvailableSlot slot = new AvailableSlot(barber, testDate, testTime);
        List<AvailableSlot> availableSlots = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotExists = availableSlots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists);

        // prenota appuntamento
        boolean booked = newAppointmentSlotsService.bookAppointment(
                customer.getEmail(),
                barber.getEmail(),
                testDate,
                testTime,
                PaymentMethod.CREDIT_CARD
        );
        assertTrue(booked, "L'appuntamento deve essere prenotato");

        // verifica slot rimosso
        List<AvailableSlot> slotsAfter = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotRemoved = slotsAfter.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertFalse(slotRemoved, "Lo slot deve essere stato rimosso dopo la prenotazione");

        // verifica appuntamento esistente
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        boolean appointmentExists = appointments.stream()
                .anyMatch(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime));
        assertTrue(appointmentExists);

        // verifica che sia stata creata la notifica di prenotazione per il barbiere
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barber.getEmail());
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("New Appointment")
                        && n.getMessage().contains("Luigi Verdi has booked an appointment with you"));
        assertTrue(notificationExists);
    }

    @Test
    public void testDeleteAppointmentAndSlotReaddition() {
        // recupero appuntamento
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        Appointment toDelete = appointments.stream()
                .filter(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime))
                .findFirst()
                .orElse(null);

        assertNotNull(toDelete, "Appuntamento da cancellare deve esistere");

        // cancella appuntamento
        boolean deleted = appointmentService.deleteAppointment(toDelete);
        assertTrue(deleted, "Appuntamento deve essere cancellato");

        // invia notifica
        AppointmentService.addNotification(toDelete);

        // riaggiunge slot
        AppointmentService.addAvailableSlot(toDelete);

        // verifica slot di nuovo disponibile
        List<AvailableSlot> slots = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotExists = slots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists, "Lo slot deve essere di nuovo disponibile dopo cancellazione");

        // verifica che sia stata creata la notifica di slot libero per il barbiere
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barber.getEmail());
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("Slot available")
                        && n.getMessage().contains("A slot has become available on " + testDate + " at " + testTime + " with Giuseppe"));
        assertTrue(notificationExists, "Deve essere stata creata una notifica di slot libero per il barbiere");
    }

    @AfterAll
    public void cleanUp() {
        signInService.authenticateUser(customer.getEmail(), "password456");

        // cancella appuntamenti residui
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        appointments.stream()
                .filter(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime))
                .forEach(a -> appointmentService.deleteAppointment(a));

        // elimina slot se rimasto
        AvailableSlot slot = new AvailableSlot(barber, testDate, testTime);
        new ConcreteAvailableSlotDAO().removeAvSlot(slot);

        // elimina servizio creato
        concreteServiceTypeDAO.removeServiceType(testService);

        // elimina utenti creati
        ConcreteUserDAO userDao = new ConcreteUserDAO();
        userDao.removeUserByEmail(barber.getEmail());
        userDao.removeUserByEmail(customer.getEmail());

        // elimina notifiche generate dai test
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barber.getEmail());
        barberNotifications.stream()
                .filter(n -> n.getTitle().equals("New Appointment") && n.getMessage().contains("Luigi Verdi has booked an appointment with you"))
                .forEach(newsDAO::deleteNotification);

        barberNotifications.stream()
                .filter(n -> n.getTitle().equals("Slot available") && n.getMessage().contains("A slot has become available on " + testDate + " at " + testTime + " with " + barber.getName()))
                .forEach(newsDAO::deleteNotification);

        SessionManager.getInstance().closeSession();
    }
}