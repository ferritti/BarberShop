package Model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private LocalDate date;
    private LocalTime time;
    private Customer customer;
    private Barber barber;
    private ServiceType serviceType;

    public Appointment(LocalDate date, LocalTime time
            , Customer customer, Barber barber, ServiceType serviceType) {
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.barber = barber;
        this.serviceType = serviceType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getCustomerEmail() {
        return customer.getEmail();
    }

    public String getBarberEmail() {
        return barber.getEmail();
    }

    public String getServiceName() {
        return serviceType.getServiceName();
    }
}

