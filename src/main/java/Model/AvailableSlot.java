package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableSlot {
    private String barberEmail;
    private LocalDate date;
    private LocalTime startTime;

    public AvailableSlot(String barberEmail, LocalDate date, LocalTime startTime) {
        this.barberEmail = barberEmail;
        this.date = date;
        this.startTime = startTime;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

}