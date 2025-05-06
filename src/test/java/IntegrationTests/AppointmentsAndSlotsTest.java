package IntegrationTests;

import Authentication.SessionManager;
import Business.*;
import Model.*;
import Model.PaymentMethod;
import Persistence.DAO.*;
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

    private final String barberEmail = "barber1234@barbershop.com";
    private final String customerEmail = "customer5678@customer.com";
    private final String customerPassword = "customer123";

    private String testBarberName;
    private ServiceType testService;
    private LocalDate testDate;
    private LocalTime testTime;

    @BeforeAll
    public void setup() {
        newAppointmentSlotsService = new NewAppointmentSlotsService();
        appointmentService = new AppointmentService();
        SignUpService signUpService = new SignUpService();
        signInService = new SignInService();
        concreteServiceTypeDAO = new ConcreteServiceTypeDAO();
        newsDAO = new ConcreteNewsDAO();

        // iscrizione barbiere
        String barberPassword = "barber123";
        String barberPhoneNumber = "1122334455";
        signUpService.registerUser("Giuseppe", "Barbiere", barberEmail, barberPassword, barberPhoneNumber, "I-AM-A-BARBER");

        // iscrizione cliente
        String customerPhoneNumber = "9988776655";
        signUpService.registerUser("Andrea", "Cliente", customerEmail, customerPassword, customerPhoneNumber, "");

        // login cliente
        signInService.authenticateUser(customerEmail, customerPassword);

        testBarberName = "Giuseppe Barbiere";
        testDate = LocalDate.now().plusDays(1);
        testTime = LocalTime.of(11, 0, 0);
        String serviceName = "Barba Uomo";
        double servicePrice = 20.0;
        testService = new ServiceType(serviceName, servicePrice);

        // aggiunta servizio
        concreteServiceTypeDAO.addServiceType(testService);
    }

    @Test
    public void testBookAppointmentAndSlotRemoval() {
        // verifica presenza slot
        AvailableSlot slot = new AvailableSlot(barberEmail, testDate, testTime);
        List<AvailableSlot> availableSlots = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        boolean slotExists = availableSlots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists);

        // prenota appuntamento
        boolean booked = newAppointmentSlotsService.bookAppointment(
                testBarberName,
                testService.getServiceName(),
                testDate,
                testTime,
                PaymentMethod.CREDIT_CARD
        );
        assertTrue(booked, "L'appuntamento deve essere prenotato");

        // verifica slot rimosso
        List<AvailableSlot> slotsAfter = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        boolean slotRemoved = slotsAfter.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertFalse(slotRemoved, "Lo slot deve essere stato rimosso dopo la prenotazione");

        // verifica appuntamento esistente
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        boolean appointmentExists = appointments.stream()
                .anyMatch(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime));
        assertTrue(appointmentExists, "L'appuntamento deve esistere nel DB");

        // verifica che sia stata creata la notifica di prenotazione per il barbiere
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barberEmail);
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("New Appointment")
                        && n.getMessage().contains("Andrea Cliente has booked an appointment with you"));
        assertTrue(notificationExists, "Deve essere stata creata una notifica di appuntamento per il barbiere");
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
        List<AvailableSlot> slots = newAppointmentSlotsService.getAvailableSlots(barberEmail, testDate);
        boolean slotExists = slots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists, "Lo slot deve essere di nuovo disponibile dopo cancellazione");

        // verifica che sia stata creata la notifica di slot libero per il barbiere
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barberEmail);
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("Slot available")
                        && n.getMessage().contains("A slot has become available on " + testDate + " at " + testTime + " with Giuseppe"));
        assertTrue(notificationExists, "Deve essere stata creata una notifica di slot libero per il barbiere");
    }

    @AfterAll
    public void cleanUp() {
        signInService.authenticateUser(customerEmail, customerPassword);

        // cancella appuntamenti residui
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        appointments.stream()
                .filter(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime))
                .forEach(a -> appointmentService.deleteAppointment(a));

        // elimina slot se rimasto
        AvailableSlot slot = new AvailableSlot(barberEmail, testDate, testTime);
        new ConcreteAvailableSlotDAO().removeAvSlot(slot);

        // elimina servizio creato
        concreteServiceTypeDAO.removeServiceType(testService);

        // elimina utenti creati
        ConcreteUserDAO userDao = new ConcreteUserDAO();
        userDao.removeUserByEmail(barberEmail);
        userDao.removeUserByEmail(customerEmail);

        // elimina notifiche generate dai test
        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barberEmail);
        barberNotifications.stream()
                .filter(n -> n.getTitle().equals("New Appointment") && n.getMessage().contains("Andrea Cliente has booked an appointment with you"))
                .forEach(newsDAO::deleteNotification);

        barberNotifications.stream()
                .filter(n -> n.getTitle().equals("Slot available") && n.getMessage().contains("A slot has become available on " + testDate + " at " + testTime + " with " + testBarberName))
                .forEach(newsDAO::deleteNotification);

        SessionManager.getInstance().closeSession();
    }
}