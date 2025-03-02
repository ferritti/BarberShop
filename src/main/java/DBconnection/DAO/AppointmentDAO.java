package DBconnection.DAO;
import Model.Appointment;
import java.sql.SQLException;
import java.util.List;

public interface AppointmentDAO {
    public void addAppointment(Appointment appointment) throws SQLException;
    public void removeAppointment(Appointment appointment) throws SQLException;
    public List<Appointment> findByEmailOfUser(String email) throws SQLException;
}