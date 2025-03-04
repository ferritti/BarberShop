package Business;
import DBconnection.DAO.ServiceTypeDAO;
import DBconnection.DAO.AvailableSlotDAO;

import Model.AvailableSlot;
import Model.Notification;
import Model.ServiceType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BarberController {
    private AvailableSlotDAO availableSlotDAO;
    private ServiceTypeDAO serviceTypeDAO;
    public void addServiceType(ServiceType serviceType) {}
    public void removeServiceType(String serviceName) {}
    public List<ServiceType> showServiceTypes() {}
    public void addAvSlot(AvailableSlot availableSlot) {}
    public void removeAvSlot(String bEmail, LocalDate date, LocalTime time) {}
    public List<AvailableSlot> showMyAvSlotsAtSelectedDate(String bEmail, LocalDate date) {}
    public void sendComunication(Notification notification) {}
}