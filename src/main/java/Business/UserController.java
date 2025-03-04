package Business;
import DBconnection.DAO.UserDAO;
import DBconnection.DAO.AppointmentDAO;
import Model.Appointment;
import Model.User;
import java.time.LocalDate;
import java.util.List;

public abstract class UserController {
    AppointmentDAO appointmentDAO;
    public void addAppointment(Appointment appointment) {}
    public void removeAppointment(String cEmail, LocalDate date) {}
    public List<Appointment> showMyAppointments(String uEmail) {}
}
