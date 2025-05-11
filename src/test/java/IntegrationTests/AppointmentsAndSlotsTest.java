package IntegrationTests;

import Authentication.SessionManager;
import Services.*;
import Model.*;
import Model.PaymentMethod;
import Persistence.DAO.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private List<ServiceType> testService = new ArrayList<>();
    private List<String> servicesName = new ArrayList<>();
    private final LocalDate testDate = LocalDate.now();
    private final LocalTime testTime = LocalTime.of(10, 0);

    @BeforeAll
    public void setup() {
        barber = new Barber("Mario", "Rossi", "mario.rossi@gmail.com", "password123", "3331234567");
        customer = new Customer("Luigi", "Verdi", "luigi.verdi@gmail.com", "password456", "3337654321");
        testService.add( new ServiceType("Balsamo", 20.0));
        testService.add( new ServiceType("Shampoo", 10.0));
        newAppointmentSlotsService = new NewAppointmentSlotsService();
        appointmentService = new AppointmentService();
        SignUpService signUpService = new SignUpService();
        signInService = new SignInService();
        concreteServiceTypeDAO = new ConcreteServiceTypeDAO();
        newsDAO = new ConcreteNewsDAO();

        signUpService.registerUser(barber.getName(), barber.getSurname(), barber.getEmail(), "password123", barber.getPhone(), "I-AM-A-BARBER");
        signUpService.registerUser(customer.getName(), customer.getSurname(), customer.getEmail(), "password456", customer.getPhone(), "");

        signInService.authenticateUser(customer.getEmail(), "password456");
        concreteServiceTypeDAO.removeServiceType(testService.get(0));
        concreteServiceTypeDAO.removeServiceType(testService.get(1));
        for (ServiceType serviceType : testService) {
            concreteServiceTypeDAO.addServiceType(serviceType);
            servicesName.add(serviceType.getName());
        }
    }

    @Test
    public void testBookAppointmentAndSlotRemoval() {
        List<AvailableSlot> availableSlots = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotExists = availableSlots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists);

        boolean booked = newAppointmentSlotsService.bookAppointment(
                barber.getName() + " " + barber.getSurname(),
                servicesName,
                testDate,
                testTime,
                PaymentMethod.CREDIT_CARD
        );
        assertTrue(booked);

        List<AvailableSlot> slotsAfter = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotRemoved = slotsAfter.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertFalse(slotRemoved);

        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        boolean appointmentExists = appointments.stream()
                .anyMatch(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime));
        assertTrue(appointmentExists);

        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barber.getEmail());
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("New Appointment")
                        && n.getMessage().contains("Luigi Verdi has booked an appointment with you"));
        assertTrue(notificationExists);
    }

    @Test
    public void testDeleteAppointmentAndSlotReintroducing() {
        List<Appointment> appointments = newAppointmentSlotsService.getAppointments();
        Appointment toDelete = appointments.stream()
                .filter(a -> a.getDate().equals(testDate) && a.getTime().equals(testTime))
                .findFirst()
                .orElse(null);

        assertNotNull(toDelete);

        boolean deleted = appointmentService.deleteAppointment(toDelete);
        assertTrue(deleted);

        AppointmentService.addNotification(toDelete);

        AppointmentService.addAvailableSlot(toDelete);

        List<AvailableSlot> slots = newAppointmentSlotsService.getAvailableSlots(barber.getEmail(), testDate);
        boolean slotExists = slots.stream().anyMatch(s -> s.getStartTime().equals(testTime));
        assertTrue(slotExists);

        List<Notification> barberNotifications = newsDAO.getAllBarberNews(barber.getEmail());
        boolean notificationExists = barberNotifications.stream()
                .anyMatch(n -> n.getTitle().equals("Slot available")
                        && n.getMessage().contains("A slot has become available on " + testDate + " at " + testTime + " with " + barber.getName()));
        assertTrue(notificationExists);
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
        concreteServiceTypeDAO.removeServiceType(testService.get(1));
        concreteServiceTypeDAO.removeServiceType(testService.get(0));

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
