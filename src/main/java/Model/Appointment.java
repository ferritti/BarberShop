package Model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int idAppointment;
    private LocalDate date;
    private LocalTime time;
    private Customer customer;
    private Barber barber;
    private ServiceType serviceType;

    public Appointment(int idAppointment, LocalDate date, LocalTime time
            , Customer customer, Barber barber, ServiceType serviceType) {
        this.idAppointment = idAppointment;
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.barber = barber;
        this.serviceType = serviceType;
    }

    public void setIdAppointment(int idAppointment) {
        this.idAppointment = idAppointment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public int getIdAppointment() {
        return idAppointment;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Barber getBarber() {
        return barber;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
