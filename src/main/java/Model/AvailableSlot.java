package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableSlot {
    private Barber barber;
    private LocalDate date;
    private LocalTime startTime;

    public AvailableSlot(Barber barber, LocalDate date, LocalTime startTime) {
        this.barber = barber;
        this.date = date;
        this.startTime = startTime;
    }

    public Barber getBarber() {
        return barber;
    }
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}