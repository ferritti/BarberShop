package Business;

import Authentication.SessionManager;
import DBconnection.DAO.AppointmentDAO;
import DBconnection.DAO.AvailableSlotDAO;
import DBconnection.DAO.ConcreteAppointmentDAO;
import DBconnection.DAO.NewsDAO;
import DBconnection.DAO.ConcreteAvailableSlotDAO;
import DBconnection.DAO.ConcreteNewsDAO;
import Model.Appointment;
import Model.AvailableSlot;
import Model.Notification;
import Model.User;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    private static AvailableSlotDAO availableSlotDAO;
    private static NewsDAO newsDAO;
    private SessionManager sessionManager = SessionManager.getInstance();

    public AppointmentService() {
        this.appointmentDAO = new ConcreteAppointmentDAO();
        availableSlotDAO = new ConcreteAvailableSlotDAO();
        newsDAO = new ConcreteNewsDAO();
    }

    public AppointmentService(AppointmentDAO appointmentDAO, AvailableSlotDAO availableSlotDAO, NewsDAO newsDAO, SessionManager sessionManager) {
        this.appointmentDAO = appointmentDAO;
        AppointmentService.availableSlotDAO = availableSlotDAO;
        AppointmentService.newsDAO = newsDAO;
        this.sessionManager = sessionManager;
    }

    public List<Appointment> getAppointments() {
        if(sessionManager.getCurrentUser().getUserType() == User.UserType.BARBER) {
            return appointmentDAO.findByEmailOfUser(sessionManager.getCurrentUser().getEmail());
        } else {
            return appointmentDAO.findByEmailOfUser(sessionManager.getCurrentUser().getEmail());
        }
    }

    public static boolean isPastAppointment(Appointment appointment) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.isBefore(now);
    }

    public static void addAvailableSlot(Appointment appointment) {
        AvailableSlot availableSlot = new AvailableSlot(appointment.getBarberEmail(), appointment.getDate(), appointment.getTime());
        availableSlotDAO.addAvSlot(availableSlot);
    }

    public boolean deleteAppointment(Appointment appointment) {
        return appointmentDAO.deleteAppointment(appointment);
    }

    public static void addNotification(Appointment appointment) {
        Notification notification = new Notification("Slot available",
                "A slot has become available on " + appointment.getDate() + " at " + appointment.getTime() + " with " + appointment.getBarberName(),
                appointment.getBarberEmail(),true);
        newsDAO.addNotification(notification);
    }

}
