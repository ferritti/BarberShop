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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
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
