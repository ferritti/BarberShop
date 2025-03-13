

package DBconnection.DAO;
import Model.Appointment;
import java.util.List;

public interface AppointmentDAO {
    public boolean addAppointment(Appointment appointment) ;
    public boolean deleteAppointment(Appointment appointment) ;
    public List<Appointment> findByEmailOfUser(String email) ;
}
