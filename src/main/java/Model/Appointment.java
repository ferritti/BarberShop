package Model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private LocalDate date;
    private LocalTime time;
    private String customerEmail;
    private String barberEmail;
    private ServiceType serviceType;

    public Appointment(LocalDate date, LocalTime time
            , String customerEmail, String barberEmail, ServiceType serviceType) {
        this.date = date;
        this.time = time;
        this.customerEmail = customerEmail;
        this.barberEmail = barberEmail;
        this.serviceType = serviceType;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
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

    public String getCustomer() {
        return customerEmail;
    }

    public String getBarber() {
        return barberEmail;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
