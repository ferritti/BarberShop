package Business;

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

        SceneNavigator.centerTextInColumns(barberColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);
        SceneNavigator.setColumnsNotReorderable(barberColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);

        tableViewCustomerAppointments.setSelectionModel(null);

        addDeleteButtonToTable();

        tableViewCustomerAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        paymentColumn.setMinWidth(95);
        dateColumn.setMinWidth(80);

        deleteColumn.setMaxWidth(40);
        timeColumn.setMaxWidth(50);
        priceColumn.setMaxWidth(40);

        loadAppointments();

    }

    private void loadAppointments() {
        List<Appointment> appointments = appointmentService.getAppointments();
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        tableViewCustomerAppointments.setItems(observableList);
    }


    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png")));
            private final Button deleteButton = new Button();
            private final StackPane pane = new StackPane();

            {
                deleteIcon.setFitWidth(15);
                deleteIcon.setFitHeight(15);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                deleteButton.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());

                    if (AppointmentService.isPastAppointment(appointment)) {
                        showPastAppointmentError();
                    } else {
                        confirmAndDeleteAppointment(appointment);
                    }
                });

                // Usare StackPane per centrare il bottone nella cella
                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Appointment appointment = getTableView().getItems().get(getIndex());

                    if (AppointmentService.isPastAppointment(appointment)) {
                        deleteButton.setOpacity(0.3);
                    } else {
                        deleteButton.setOpacity(1.0);
                    }

                    setGraphic(pane);
                }
            }
        });
    }

    private void showPastAppointmentError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation not allowed");
        alert.setHeaderText(null);
        alert.setContentText("It is not possible to delete past appointments.");

        ButtonType buttonTypeOk = new ButtonType("Ok");
        alert.getButtonTypes().setAll(buttonTypeOk);

        alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        alert.getDialogPane().getStyleClass().add("custom-alert");

        alert.showAndWait().ifPresent(buttonType -> {;
            if (buttonType == buttonTypeOk) {
                alert.close();
            }
        });
    }


    private void confirmAndDeleteAppointment(Appointment appointment) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm deletion");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this appointment?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        confirmDialog.getDialogPane().getStyleClass().add("custom-alert");

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                deleteAppointment(appointment);

                AppointmentService.addAvailableSlot(appointment);
            }
        });
    }


    private void deleteAppointment(Appointment appointment) {
        boolean deleted = appointmentService.deleteAppointment(appointment);

        if (deleted) {
            tableViewCustomerAppointments.getItems().remove(appointment);
            AppointmentService.addNotification(appointment);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the appointment.");

            alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            alert.getDialogPane().getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }

    @FXML
    private void goToNewsView() {
        SceneNavigator.switchScene(tableViewCustomerAppointments, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    private void goToProfileView() {
        SceneNavigator.switchScene(tableViewCustomerAppointments, "/View/ProfileCustomer.fxml", "Profile");
    }

    @FXML
    void goToNewAppointmentView() {
        SceneNavigator.switchScene(tableViewCustomerAppointments, "/View/NewAppointmentCalendar.fxml", "New Appointment");
    }

}