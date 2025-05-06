package Model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Appointment {
    private LocalDate date;
    private LocalTime time;
    private Customer customer;
    private Barber barber;
    private ServiceType serviceType;
    private final PaymentMethod paymentMethod;
    public Appointment(LocalDate date, LocalTime time, Customer customer, Barber barber, ServiceType serviceType, PaymentMethod paymentMethod) {
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.barber = barber;
        this.serviceType = serviceType;
        this.paymentMethod = paymentMethod;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
