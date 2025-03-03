package DBconnection.DAO;

import Model.Appointment;
import java.time.LocalDate;
import java.util.List;

public interface AvailableSlotsDAO {
    public void addAvSlot(Appointment appointment) ;
    public void removeAvSlot(Appointment appointment) ;
    public List<Appointment> getAvSlotsAtSelectedDate(LocalDate Date) ;
}
