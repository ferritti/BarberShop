package Controllers;

import Helpers.SceneNavigator;
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
        calendarGrid.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/styles/CalendarStyle.css").toExternalForm());
            }
        });

        currentYearMonth = YearMonth.now();
        updateCalendar();
    }

    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM", Locale.ENGLISH);
        yearMonthLabel.setText(currentYearMonth.format(formatter));

        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear();

        createCalendarHeader();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int row = 1;
        int col = dayOfWeek;
        LocalDate today = LocalDate.now();
        int maxRow = 1;

        for (int day = 1; day <= daysInMonth; day++) {
            StackPane cell = createDayCell(day, today);
            calendarGrid.add(cell, col, row);
            maxRow = Math.max(maxRow, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        for (int i = 1; i <= maxRow; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(64);
            rowConstraints.setMinHeight(64);
            calendarGrid.getRowConstraints().add(rowConstraints);
        }

        updateNavigationButtons();
    }

    private void createCalendarHeader() {
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < dayNames.length; i++) {
            Text dayText = new Text(dayNames[i]);
            StackPane cell = new StackPane(dayText);
            cell.getStyleClass().add("calendar-header");
            if (i == 0) {
                cell.getStyleClass().add("left-radius");
            } else if (i == 6) {
                cell.getStyleClass().add("right-radius");
            }
            dayText.setFill(javafx.scene.paint.Color.WHITE);
            calendarGrid.add(cell, i, 0);
        }
        RowConstraints headerRowConstraints = new RowConstraints();
        headerRowConstraints.setPrefHeight(30);
        headerRowConstraints.setMinHeight(30);
        calendarGrid.getRowConstraints().add(headerRowConstraints);
    }

    private StackPane createDayCell(int day, LocalDate today) {
        Text dayText = new Text(String.valueOf(day));
        StackPane cell = new StackPane(dayText);
        LocalDate cellDate = currentYearMonth.atDay(day);

        cell.getStyleClass().add("day-cell");
        if (cellDate.equals(today)) {
            cell.getStyleClass().add("today");
        } else {
            cell.getStyleClass().add("normal");
        }
        if (cellDate.isBefore(today)) {
            cell.setDisable(true);
            cell.getStyleClass().add("disabled");
        }

        dayText.setOnMouseClicked(event -> {
            if (dayText.getText().matches("\\d+")) {
                LocalDate selectedDate = currentYearMonth.atDay(Integer.parseInt(dayText.getText()));
                goToSlots(selectedDate);
            }
        });
        return cell;
    }

    private void updateNavigationButtons() {
        YearMonth now = YearMonth.now();

        previousButton.getStyleClass().add("nav-button");
        nextButton.getStyleClass().add("nav-button");
        todayButton.getStyleClass().add("nav-button");

        if (currentYearMonth.equals(now)) {
            previousButton.setDisable(true);
            previousButton.setOpacity(0.5);
            todayButton.setDisable(true);
            todayButton.setVisible(false);
        } else {
            previousButton.setDisable(false);
            previousButton.setOpacity(1);
            todayButton.setDisable(false);
            todayButton.setVisible(true);
        }

        if (currentYearMonth.equals(now.plusMonths(6))) {
            nextButton.setDisable(true);
            nextButton.setOpacity(0.5);
        } else {
            nextButton.setDisable(false);
            nextButton.setOpacity(1);
        }
    }

    @FXML
    private void nextMonth() {
        if (currentYearMonth.isBefore(YearMonth.now().plusMonths(6))) {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        }
    }

    @FXML
    public void previousMonth() {
        if (currentYearMonth.isAfter(YearMonth.now())) {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        }
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

    @FXML
    void goToAppointmentsView() {
        SceneNavigator.switchScene(calendarGrid, "/View/AppointmentsCustomer.fxml", "Appointments");
    }

    @FXML
    void goToNewsView() {
        SceneNavigator.switchScene(calendarGrid, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    void goToProfileView() {
        SceneNavigator.switchScene(calendarGrid, "/View/ProfileCustomer.fxml", "Profile");
    }
}

