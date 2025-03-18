package Business;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class NewAppointmentController1 {
    @FXML private Label yearMonthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button todayButton;

    private YearMonth currentYearMonth;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        updateCalendar();
    }
    private void updateCalendar() {
        // Aggiorna l'etichetta del mese e anno
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
        yearMonthLabel.setText(currentYearMonth.format(formatter));

        // Pulisci il GridPane
        calendarGrid.getChildren().clear();

        // Aggiungi intestazioni per i giorni della settimana (prima riga)
        String[] dayNames = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};
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

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(10);
        calendarGrid.getRowConstraints().add(0, rowConstraints);

        // Determina il primo giorno del mese e quanti giorni ha il mese
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1; // 0 è lunedì, 6 è domenica
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Riempi il calendario (partendo solo dai giorni del mese corrente)
        int row = 1;
        int col = dayOfWeek;  // Partiamo dalla colonna corretta in base al primo giorno del mese
        int day = 1;

        // Aggiungi giorni del mese corrente
        LocalDate today = LocalDate.now();

        while (day <= daysInMonth) {
            Text text = new Text(String.valueOf(day));
            StackPane cell = new StackPane(text);

            // Evidenzia la data odierna
            if (today.getYear() == currentYearMonth.getYear() &&
                    today.getMonth() == currentYearMonth.getMonth() &&
                    today.getDayOfMonth() == day) {
                cell.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center; -fx-padding: 10px;");
            } else {
                cell.setStyle("-fx-background-color: #f5e2c1; -fx-alignment: center; -fx-padding: 10px;");
            }

            text.setOnMouseClicked(event -> {
                String content = text.getText();
                if (content.matches("\\d+")) { // Se è un numero (un giorno del mese)
                    int clickedDay = Integer.parseInt(content);
                    LocalDate selectedDate = currentYearMonth.atDay(clickedDay);
                    // Salva la data nel Singleton
                    AppointmentData.getInstance().setData(selectedDate);
                    System.out.println("Data selezionata: " + selectedDate);

                    // Passa alla schermata della lista dei barbieri
                    goToBarberSelection();
                }
            });

            calendarGrid.add(cell, col, row);

            day++;
            col++;

            // Vai alla riga successiva se necessario
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        if(currentYearMonth.equals(YearMonth.now()))
        {
            previousButton.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-background-color: gray;");
            todayButton.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-background-color: gray;");

        }

        else
        {
            previousButton.setStyle("-fx-background-color: #d7a857; -fx-background-radius: 30;");
            todayButton.setStyle("-fx-background-color: #d7a857; -fx-background-radius: 30;");
        }

        if(currentYearMonth.equals(YearMonth.now().plusMonths(6)))
            nextButton.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-background-color: gray;");
        else
            nextButton.setStyle("-fx-background-color: #d7a857; -fx-background-radius: 30;");

    }


    @FXML
    private void nextMonth() {
        if(currentYearMonth.isBefore(YearMonth.now().plusMonths(6))){
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        }

    }

    @FXML
    public void previousMonth(ActionEvent actionEvent) {
        if(currentYearMonth.isAfter(YearMonth.now())) {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        }


    }

    private void goToBarberSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointment2.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) calendarGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleziona il Barbiere");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
