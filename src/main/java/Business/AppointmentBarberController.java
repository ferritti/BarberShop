package Business;

import Authentication.SessionManager;
import DBconnection.DAO.AppointmentDAO;
import DBconnection.DAO.ConcreteAppointmentDAO;
import Model.Appointment;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final AppointmentDAO appointmentDAO = new ConcreteAppointmentDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        customerColumn.setReorderable(false);
        dateColumn.setReorderable(false);
        priceColumn.setReorderable(false);
        serviceColumn.setReorderable(false);
        timeColumn.setReorderable(false);
        paymentColumn.setReorderable(false);

        centerTextInColumn(customerColumn);
        centerTextInColumn(dateColumn);
        centerTextInColumn(priceColumn);
        centerTextInColumn(serviceColumn);
        centerTextInColumn(timeColumn);
        centerTextInColumn(paymentColumn);

        paymentColumn.setMinWidth(95);
        dateColumn.setMinWidth(80);

        tableViewBarberAppointments.setSelectionModel(null);

        tableViewBarberAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadAppointments();
    }

    private <T> void centerTextInColumn(TableColumn<Appointment, T> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER); // Imposta l'allineamento al centro
                }
            }
        });
    }


    private void loadAppointments() {
        List<Appointment> appointments = appointmentDAO.findByEmailOfUser(SessionManager.getInstance().getCurrentUser().getEmail());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        tableViewBarberAppointments.setItems(observableList);
    }


    @FXML
    private void goToProfileAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileBarber.fxml"));
            Parent root = loader.load();

            ProfileBarberController controller = loader.getController();
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null) {
                controller.profileAction(
                        currentUser.getName(),
                        currentUser.getSurname(),
                        currentUser.getEmail(),
                        currentUser.getPhone());
            }

            Stage stage = (Stage) tableViewBarberAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToNewsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableViewBarberAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToServiceView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Service.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableViewBarberAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Service");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSendComunicationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SendComunication.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableViewBarberAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Send Comunication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}