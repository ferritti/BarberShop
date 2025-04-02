package Business;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM", Locale.ENGLISH);
        yearMonthLabel.setText(currentYearMonth.format(formatter));

        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear();

        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            Text text = new Text(dayNames[i]);
            StackPane cell = new StackPane(text);

            String style = "-fx-font-weight: bold; -fx-alignment: center; -fx-background-color: #651FFF; " +
                    "-fx-padding: 5px; -fx-font-size: 14px; -fx-font-family: 'Helvetica'; -fx-text-fill: white;";

            if (i == 0) {
                style += "-fx-background-radius: 10px 0 0 0;";
            } else if (i == 6) {
                style += "-fx-background-radius: 0 10px 0 0;";
            }

            cell.setStyle(style);
            text.setFill(javafx.scene.paint.Color.WHITE);

            calendarGrid.add(cell, i, 0);
        }

        RowConstraints headerRowConstraints = new RowConstraints();
        headerRowConstraints.setPrefHeight(30);
        headerRowConstraints.setMinHeight(30);
        calendarGrid.getRowConstraints().add(headerRowConstraints);

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int row = 1;
        int col = dayOfWeek;
        int day = 1;
        int maxRow = 1;

        LocalDate today = LocalDate.now();

        while (day <= daysInMonth) {
            Text text = new Text(String.valueOf(day));
            StackPane cell = new StackPane(text);

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

                    goToSlots(selectedDate);
                }
            });

            calendarGrid.add(cell, col, row);
            maxRow = Math.max(maxRow, row);
            day++;
            col++;

            if (col > 6) {
                col = 0;
                row++;
            }
        }

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

    @FXML
    void goToAppointmentsView() {
        SceneNavigator.switchScene(calendarGrid, "/View/AppointmentCustomer.fxml", "Appointments");
    }

    @FXML
    void goToNewsView() {
        SceneNavigator.switchScene(calendarGrid, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    void goToProfileView () {
        SceneNavigator.switchScene(calendarGrid, "/View/ProfileCustomer.fxml", "Profile");
    }

    private void goToSlots(LocalDate selectedDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentSlots.fxml"));
            Parent root = loader.load();

            NewAppointmentControllerSlots controller = loader.getController();
            controller.setDate(selectedDate);

            Stage stage = (Stage) calendarGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Slots");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
