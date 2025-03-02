package DBconnection.DAO;

import Model.Appointment;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface AvailableSlotsDAO {
    public void addAvSlot(Appointment appointment) throws SQLException;
    public void removeAvSlot(Appointment appointment) throws SQLException;
    public List<Appointment> getAvSlotsAtSelectedDate(LocalDate Date) throws SQLException;
}
