package Business;

import Authentication.SessionManager;
import Model.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NewAppointmentControllerCalendar {
    @FXML private Label yearMonthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private MFXButton previousButton;
    @FXML private MFXButton nextButton;
    @FXML private MFXButton todayButton;

    private YearMonth currentYearMonth;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        updateCalendar();
    }

    private void updateCalendar() {
        // Aggiorna l'etichetta del mese e anno
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM", Locale.ENGLISH);
        yearMonthLabel.setText(currentYearMonth.format(formatter));

        // Pulisci il GridPane
        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear();
        // Pulisci anche i vincoli di riga esistenti

        // Aggiungi intestazioni per i giorni della settimana (prima riga)
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            Text text = new Text(dayNames[i]);
            StackPane cell = new StackPane(text);

            String style = "-fx-font-weight: bold; -fx-alignment: center; -fx-background-color: #651FFF; " +
                    "-fx-padding: 5px; -fx-font-size: 14px; -fx-font-family: 'Helvetica'; -fx-text-fill: white;";

            // Arrotonda i bordi solo agli angoli sinistro e destro
            if (i == 0) { // Primo giorno (Lunedì)
                style += "-fx-background-radius: 10px 0 0 0;";
            } else if (i == 6) { // Ultimo giorno (Domenica)
                style += "-fx-background-radius: 0 10px 0 0;";
            }

            cell.setStyle(style);
            text.setFill(javafx.scene.paint.Color.WHITE);

            calendarGrid.add(cell, i, 0);
        }

        // Aggiungi vincoli di riga per l'intestazione
        RowConstraints headerRowConstraints = new RowConstraints();
        headerRowConstraints.setPrefHeight(30);  // Altezza preferita per l'intestazione
        headerRowConstraints.setMinHeight(30);   // Altezza minima per garantire lo spazio
        calendarGrid.getRowConstraints().add(headerRowConstraints);

        // Determina il primo giorno del mese e quanti giorni ha il mese
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1; // 0 è lunedì, 6 è domenica
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Riempi il calendario (partendo solo dai giorni del mese corrente)
        int row = 1;
        int col = dayOfWeek;  // Partiamo dalla colonna corretta in base al primo giorno del mese
        int day = 1;
        int maxRow = 1;  // Tieni traccia del numero massimo di righe

        // Aggiungi giorni del mese corrente
        LocalDate today = LocalDate.now();

        while (day <= daysInMonth) {
            Text text = new Text(String.valueOf(day));
            StackPane cell = new StackPane(text);

            // Evidenzia la data odierna
            if (today.getYear() == currentYearMonth.getYear() &&
                    today.getMonth() == currentYearMonth.getMonth() &&
                    today.getDayOfMonth() == day) {
                cell.setStyle("-fx-background-color: #cacaca; -fx-font-weight: bold; -fx-alignment: center; -fx-padding: 10px;");
            } else {
                cell.setStyle("-fx-background-color: #e1e1e1; -fx-alignment: center; -fx-padding: 10px;");
            }

            LocalDate cellDate = currentYearMonth.atDay(day);

            if (cellDate.isBefore(today)) {
                cell.setDisable(true);
                cell.setStyle("-fx-background-color: #e1e1e1; -fx-opacity: 0.5;"); // Grigio con trasparenza
            }

            text.setOnMouseClicked(event -> {
                String content = text.getText();
                if (content.matches("\\d+")) { // Se è un numero (un giorno del mese)
                    int clickedDay = Integer.parseInt(content);
                    LocalDate selectedDate = currentYearMonth.atDay(clickedDay);
                    // Salva la data nel Singleton
                    AppointmentData.getInstance().setData(selectedDate);

                    // Passa alla schermata della lista dei barbieri
                    goToBarberSelection();
                }
            });

            calendarGrid.add(cell, col, row);
            maxRow = Math.max(maxRow, row);  // Aggiorna il numero massimo di righe

            day++;
            col++;

            // Vai alla riga successiva se necessario
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        // Aggiungi vincoli di riga per tutte le righe del calendario
        for (int i = 1; i <= maxRow; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(64);  // Altezza preferita per le righe dei giorni
            rowConstraints.setMinHeight(64);   // Altezza minima per garantire lo spazio
            calendarGrid.getRowConstraints().add(rowConstraints);
        }

        if(currentYearMonth.equals(YearMonth.now())) {
            previousButton.setStyle("-fx-border-color: #651FFF");
            previousButton.setOpacity(0.5);
            previousButton.setDisable(true);

            todayButton.setDisable(true);
            todayButton.setVisible(false);
        } else {
            previousButton.setDisable(false);
            previousButton.setOpacity(1);

            todayButton.setDisable(false);
            todayButton.setVisible(true);

            previousButton.setStyle("-fx-border-color: #651FFF");
            todayButton.setStyle(" -fx-border-color: #651FFF");
        }

        if(currentYearMonth.equals(YearMonth.now().plusMonths(6))) {
            nextButton.setStyle("-fx-border-color: #651FFF");
            nextButton.setOpacity(0.5);
            nextButton.setDisable(true);
        }
        else {
            nextButton.setOpacity(1);
            nextButton.setDisable(false);
            nextButton.setStyle("-fx-border-color: #651FFF; ");
        }
    }

    @FXML
    private void nextMonth() {
        if(currentYearMonth.isBefore(YearMonth.now().plusMonths(6))){
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        }

    }

    @FXML
    public void previousMonth() {
        if(currentYearMonth.isAfter(YearMonth.now())) {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        }
    }

    private void goToBarberSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentSlots.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) calendarGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleziona il Barbiere");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToAppointmentsView() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) calendarGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToNewsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) calendarGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @FXML
        void goToProfileView () {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileCustomer.fxml"));
                Parent root = loader.load();

                ProfileCustomerController controller = loader.getController();
                User currentUser = SessionManager.getInstance().getCurrentUser();

                if (currentUser != null) {
                    controller.profileAction(
                            currentUser.getName(),
                            currentUser.getSurname(),
                            currentUser.getEmail(),
                            currentUser.getPhone());
                }

                Stage stage = (Stage) calendarGrid.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Profile");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
