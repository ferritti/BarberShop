package DBconnection.DAO;

import Model.AvailableSlot;

import java.time.LocalDate;
import java.util.List;

public interface AvailableSlotDAO {
    public boolean addAvSlot(AvailableSlot avSlot) ;
    public boolean removeAvSlot(AvailableSlot avSlot) ;
    public List<AvailableSlot> getAvSlotsAtSelectedDate(LocalDate Date, String barberEmail) ;
}
