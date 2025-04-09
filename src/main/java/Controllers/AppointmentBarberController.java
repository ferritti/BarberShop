package Controllers;

import Business.AppointmentService;
import Helpers.SceneNavigator;
import Model.Appointment;
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
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        SceneNavigator.centerTextInColumns(customerColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);
        SceneNavigator.setColumnsNotReorderable(customerColumn, dateColumn, priceColumn, serviceColumn, timeColumn, paymentColumn);

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
        SceneNavigator.switchScene(tableViewBarberAppointments, "/View/ProfileBarber.fxml", "Profile");
    }

    @FXML
    private void goToNewsView() {
        SceneNavigator.switchScene(tableViewBarberAppointments, "/View/NewsBarber.fxml", "News");
    }

    @FXML
    private void goToServiceView() {
        SceneNavigator.switchScene(tableViewBarberAppointments, "/View/Services.fxml", "Service");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneNavigator.switchScene(tableViewBarberAppointments, "/View/SendComunication.fxml", "Send Comunication");
    }
}