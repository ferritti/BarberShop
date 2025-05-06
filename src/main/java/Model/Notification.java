package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {
    private String title;
    private String message;
    private Barber barber;
    private LocalTime time;
    private LocalDate date;
    private boolean toCustomers;

    //per inviare al barber notifica di appuntamento prenotato e inviare al barbiere e i customers appuntamento cancellato
    public Notification(String title, String message, Barber barber, boolean toCustomers) {
        this.title = title;
        this.message = message;
        time = LocalTime.now().withNano(0);
        date = LocalDate.now();
        this.barber = barber;
        this.toCustomers = toCustomers;
    }

    public Notification(String title, String message, boolean toCustomers) {
        this.title = title;
        this.message = message;
        time = LocalTime.now().withNano(0);
        date = LocalDate.now();
        this.barber = null;
        this.toCustomers = toCustomers;
    }

    public Notification(String title, String message,LocalTime time, LocalDate date) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Barber getBarber() {
        return barber;
    }

    public boolean isToCustomers() {
        return toCustomers;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }
}

