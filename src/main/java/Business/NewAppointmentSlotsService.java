package Business;

import Authentication.SessionManager;
import DBconnection.DAO.*;
import Model.Appointment;
import Model.AvailableSlot;
import Model.Notification;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public NewAppointmentSlotsService(UserDAO userDAO, ServiceTypeDAO serviceTypeDAO, AvailableSlotDAO availableSlotDAO, AppointmentDAO appointmentDAO, NewsDAO newsDAO) {
        this.userDAO = userDAO;
        this.serviceTypeDAO = serviceTypeDAO;
        this.availableSlotDAO = availableSlotDAO;
        this.appointmentDAO = appointmentDAO;
        this.newsDAO = newsDAO;
    }

    public HashMap<String, String> getBarbersData() {
        return userDAO.getBarbersData();
    }

    public HashMap<String, Double> getServicesData() {
        return serviceTypeDAO.getServices();
    }

    public List<AvailableSlot> getAvailableSlots(String barberEmail, LocalDate date) {
        return availableSlotDAO.getAvSlotsAtSelectedDate(date, barberEmail);
    }

    public List<Appointment> getAppointments() {
        return appointmentDAO.findByEmailOfUser(sessionManager.getCurrentUser().getEmail());
    }

    public boolean removeAvSlot(AvailableSlot availableSlot) {
        return availableSlotDAO.removeAvSlot(availableSlot);
    }

    public boolean addAppointment(Appointment appointment) {
        return appointmentDAO.addAppointment(appointment);
    }

    public boolean addNotification(String barberEmail) {
        Notification notification =
                new Notification("New Appointment",
                        SessionManager.getInstance().getCurrentUser().getName() + " "
                                + SessionManager.getInstance().getCurrentUser().getSurname()
                                + " has booked an appointment with you", barberEmail, false);
        return newsDAO.addNotification(notification);
    }

    public boolean isSameDateTime(Appointment appointment, LocalDate date, LocalTime time) {
        return appointment.getDate().equals(date) && appointment.getTime().equals(time);
    }
}