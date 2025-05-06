package Controllers;

import Business.AppointmentService;
import Helpers.SceneHelper;
import Model.Appointment;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentBarberController implements Initializable {

    @FXML
    private TableView<Appointment> tableViewBarberAppointments;

    @FXML
    private TableColumn<Appointment, String> customerColumn;

    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;

    @FXML
    private TableColumn<Appointment, String> paymentColumn;

    @FXML
    private TableColumn<Appointment, Double> priceColumn;

    @FXML
    private TableColumn<Appointment, String> serviceColumn;

    @FXML
    private TableColumn<Appointment, LocalTime> timeColumn;

    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getServiceType().getPrice()).asObject()
        );

        serviceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getServiceType().getName())
        );

        paymentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPaymentMethod().toString())
        );

        customerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer().getName())
        );

        SceneHelper.centerTextInColumns(customerColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);
        SceneHelper.setColumnsNotReorderable(customerColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);

        paymentColumn.setMinWidth(95);
        dateColumn.setMinWidth(80);

        tableViewBarberAppointments.setSelectionModel(null);

        tableViewBarberAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadAppointments();
    }


    private void loadAppointments() {
        List<Appointment> appointments = appointmentService.getAppointments();
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        tableViewBarberAppointments.setItems(observableList);
    }


    @FXML
    private void goToProfileView() {
        SceneHelper.switchScene(tableViewBarberAppointments, "/View/ProfileBarber.fxml", "Profile");
    }

    @FXML
    private void goToNewsView() {
        SceneHelper.switchScene(tableViewBarberAppointments, "/View/NewsBarber.fxml", "News");
    }

    @FXML
    private void goToServiceView() {
        SceneHelper.switchScene(tableViewBarberAppointments, "/View/Services.fxml", "Service");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneHelper.switchScene(tableViewBarberAppointments, "/View/SendComunication.fxml", "Send Comunication");
    }
}