package Model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private LocalDate date;
    private LocalTime time;
    private String customerEmail;
    private String barberEmail;
    private String serviceTypeName;

    public static enum Payment {ONLINE, SHOP};
    private final Payment payment;

    public Appointment(LocalDate date, LocalTime time
            , String customerEmail, String barberEmail, String serviceTypeName, Payment payment) {
        this.date = date;
        this.time = time;
        this.customerEmail = customerEmail;
        this.barberEmail = barberEmail;
        this.serviceTypeName = serviceTypeName;
        this.payment = payment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public Payment getPayment() { return payment; }

}

