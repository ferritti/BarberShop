package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Appointment {
    private final LocalDate date;
    private final LocalTime time;
    private final Customer customer;
    private final Barber barber;
    private final List<ServiceType> serviceTypes;
    private final PaymentMethod paymentMethod;
    public Appointment(LocalDate date, LocalTime time, Customer customer, Barber barber, List<ServiceType> serviceTypes, PaymentMethod paymentMethod) {
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.barber = barber;
        this.serviceTypes = serviceTypes;
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

    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
