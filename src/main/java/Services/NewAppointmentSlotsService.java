package Services;

import Authentication.SessionManager;
import Model.*;
import Persistence.DAO.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewAppointmentSlotsService {
    private UserDAO userDAO;
    private ServiceTypeDAO serviceTypeDAO;
    private AvailableSlotDAO availableSlotDAO;
    private AppointmentDAO appointmentDAO;
    private NewsDAO newsDAO;
    private SessionManager sessionManager = SessionManager.getInstance();

    public NewAppointmentSlotsService() {
        this.userDAO = new ConcreteUserDAO();
        this.serviceTypeDAO = new ConcreteServiceTypeDAO();
        this.availableSlotDAO = new ConcreteAvailableSlotDAO();
        this.appointmentDAO = new ConcreteAppointmentDAO();
        this.newsDAO = new ConcreteNewsDAO();
    }

    public NewAppointmentSlotsService(UserDAO userDAO, ServiceTypeDAO serviceTypeDAO, AvailableSlotDAO availableSlotDAO, AppointmentDAO appointmentDAO, NewsDAO newsDAO, SessionManager sessionManager) {
        this.userDAO = userDAO;
        this.serviceTypeDAO = serviceTypeDAO;
        this.availableSlotDAO = availableSlotDAO;
        this.appointmentDAO = appointmentDAO;
        this.newsDAO = newsDAO;
        this.sessionManager = sessionManager;
    }

    public HashMap<String, String> getBarbersData() {
        return userDAO.getBarbersData();
    }

    public HashMap<String, Double> getServicesData() {
        return serviceTypeDAO.getServicePricesMap();
    }

    public List<AvailableSlot> getAvailableSlots(String barberEmail, LocalDate date) {
        return availableSlotDAO.getAvSlotsAtSelectedDate(date, barberEmail);
    }

    public boolean bookAppointment(String barberFullName, List<String> services, LocalDate date, LocalTime time, PaymentMethod paymentMethod) {
        String barberEmail = getBarbersData().get(barberFullName);
        Barber barberUser = (Barber) userDAO.findByEmail(barberEmail);
        Customer currentUser = (Customer) sessionManager.getCurrentUser();
        List<ServiceType> serviceType = new ArrayList<>();


        for(String service : services) {
            serviceType.add(new ServiceType(service, getServicesData().get(service)));
        }
        Appointment appointment = new Appointment(
                date,
                time,
                currentUser,
                barberUser,
                serviceType,
                paymentMethod
        );

        Notification notification = new Notification(
                "New Appointment",
                currentUser.getName() + " " + currentUser.getSurname() + " has booked an appointment with you",
                barberUser,
                false
        );
        AvailableSlot selectedSlot = new AvailableSlot(barberUser, date, time);
        boolean slotRemoved = availableSlotDAO.removeAvSlot(selectedSlot);
        boolean appointmentAdded = appointmentDAO.addAppointment(appointment);
        boolean notificationAdded = newsDAO.addNotification(notification);

        return slotRemoved && appointmentAdded && notificationAdded;
    }

    public boolean isSameDateTime(Appointment appointment, LocalDate date, LocalTime time) {
        return appointment.getDate().equals(date) && appointment.getTime().equals(time);
    }
}