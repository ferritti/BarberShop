package Controllers;

import Business.AppointmentService;
import Helpers.AlertHelper;
import Helpers.SceneHelper;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;


public class AppointmentCustomerController implements Initializable {

    @FXML
    private TableView<Appointment> tableViewCustomerAppointments;
    @FXML
    private TableColumn<Appointment, String> barberColumn;
    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Appointment, Double> priceColumn;
    @FXML
    private TableColumn<Appointment, String> serviceColumn;
    @FXML
    private TableColumn<Appointment, LocalTime> timeColumn;
    @FXML
    private TableColumn<Appointment, String> paymentColumn;
    @FXML
    private TableColumn<Appointment, Void> deleteColumn;

    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barberColumn.setCellValueFactory(new PropertyValueFactory<>("barberName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        paymentColumn.setMinWidth(95);
        dateColumn.setMinWidth(80);
        deleteColumn.setMaxWidth(40);
        timeColumn.setMaxWidth(50);
        priceColumn.setMaxWidth(40);

        SceneHelper.centerTextInColumns(barberColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);
        SceneHelper.setColumnsNotReorderable(barberColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);

        addDeleteButtonToTable();

        tableViewCustomerAppointments.setSelectionModel(null);
        tableViewCustomerAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadAppointments();
    }

    public void loadAppointments() {
        List<Appointment> appointments = appointmentService.getAppointments();
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        tableViewCustomerAppointments.setItems(observableList);
    }


    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();
            private final StackPane pane = new StackPane(deleteButton);

            {
                pane.setAlignment(Pos.CENTER);
                setDeleteButtonAction(deleteButton, this);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                int index = getIndex();
                if (empty || index < 0 || index >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Appointment appointment = getTableView().getItems().get(index);
                    deleteButton.setOpacity(AppointmentService.isPastAppointment(appointment) ? 0.3 : 1.0);
                    setGraphic(pane);
                }
            }
        });
    }

    private Button createDeleteButton() {
        Button button = new Button();
        button.setId("delete-button");
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png")));
        deleteIcon.setFitWidth(15);
        deleteIcon.setFitHeight(15);
        button.setGraphic(deleteIcon);

        button.getStyleClass().add("delete-button");
        button.setStyle("-fx-background-color: transparent;");

        return button;
    }

    private void setDeleteButtonAction(Button deleteButton, TableCell<Appointment, Void> cell) {
        deleteButton.setOnAction(event -> {
            int index = cell.getIndex();
            if (index < 0 || index >= cell.getTableView().getItems().size()) {
                return;
            }
            Appointment appointment = cell.getTableView().getItems().get(index);
            if (AppointmentService.isPastAppointment(appointment)) {
                showPastAppointmentError();
            } else {
                confirmAndDeleteAppointment(appointment);
            }
        });
    }

    private void showPastAppointmentError() {
        AlertHelper.showError("Operation not allowed", "It is not possible to delete past appointments.");
    }

    private void confirmAndDeleteAppointment(Appointment appointment) {
        boolean confirmed = AlertHelper.showConfirmation("Confirm deletion", "Are you sure you want to delete this appointment?");
        if (confirmed) {
            deleteAppointment(appointment);
            AppointmentService.addAvailableSlot(appointment);
        }
    }

    private void deleteAppointment(Appointment appointment) {
        boolean deleted = appointmentService.deleteAppointment(appointment);
        if (deleted) {
            tableViewCustomerAppointments.getItems().remove(appointment);
            AppointmentService.addNotification(appointment);
        } else {
            AlertHelper.showError("Error", "Error while deleting the appointment.");
        }
    }

    @FXML
    private void goToNewsView() {
        SceneHelper.switchScene(tableViewCustomerAppointments, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    private void goToProfileView() {
        SceneHelper.switchScene(tableViewCustomerAppointments, "/View/ProfileCustomer.fxml", "Profile");
    }

    @FXML
    private void goToNewAppointmentView() {
        SceneHelper.switchScene(tableViewCustomerAppointments, "/View/NewAppointmentCalendar.fxml", "New Appointment");
    }

}