package Business;

import java.time.LocalDate;

public class AppointmentData {
    private static AppointmentData instance;  // Istanza unica
    private LocalDate data;
    private String barbiere;

    private AppointmentData() {}  // Costruttore privato

    public static AppointmentData getInstance() {
        if (instance == null) {
            instance = new AppointmentData();
        }
        return instance;
    }

    public void setData(LocalDate data) { this.data = data; }
    public void setBarber(String barbiere) { this.barbiere = barbiere; }
    public LocalDate getData() { return data; }
    public String getBarber() { return barbiere; }

    // Metodo per eliminare l'istanza
    public static void resetInstance() {
        instance = null;
    }
}
