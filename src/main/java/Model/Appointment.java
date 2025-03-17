package Model;
import java.time.LocalDate;
import java.time.LocalTime;


public class Appointment {
    private LocalDate date;
    private LocalTime time;
    private String customerEmail;
    private String barberEmail;
    private String barberName;
    private String customerPhone;
    private String serviceTypeName;
    private double servicePrice;

    public static enum Payment {ONLINE, SHOP};
    private final Payment payment;

    public Appointment(Payment payment, String serviceTypeName, String barberName, String barberEmail, String customerEmail, String customerPhone, LocalTime time, LocalDate date, double servicePrice) {
        this.payment = payment;
        this.serviceTypeName = serviceTypeName;
        this.barberName = barberName;
        this.barberEmail = barberEmail;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.time = time;
        this.date = date;
        this.servicePrice = servicePrice;
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

    public Payment getPayment() {return payment; }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
