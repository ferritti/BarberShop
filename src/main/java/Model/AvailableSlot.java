package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableSlot {
    private String barberEmail;
    private String barberName;
    private String barberSurname;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public AvailableSlot(String barberEmail, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.barberEmail = barberEmail;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
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
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getBarberSurname() {
        return barberSurname;
    }

    public void setBarberSurname(String barberSurname) {
        this.barberSurname = barberSurname;
    }

}
