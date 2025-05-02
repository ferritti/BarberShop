package Model;
import Payment.PaymentMethod;

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
    private final PaymentMethod paymentMethod;

    public Appointment(PaymentMethod paymentMethod, String serviceTypeName, String barberName, String barberEmail, String customerEmail, String customerPhone, LocalTime time, LocalDate date, double servicePrice) {
        this.paymentMethod = paymentMethod;
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

    public LocalTime getTime() {
        return time;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public PaymentMethod getPayment() {return paymentMethod; }

    public String getBarberName() {
        return barberName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
