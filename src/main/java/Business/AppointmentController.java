package Business;

import Authentication.SessionManager;
import DBconnection.DAO.AppointmentDAO;
import DBconnection.DAO.ConcreteAppointmentDAO;
import Model.Appointment;
import Model.User;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.List;

public class AppointmentController implements Initializable {

    @FXML
    private ImageView chair_icon;

    @FXML
    private ImageView news_icon;

    @FXML
    private ImageView plus_icon;

    @FXML
    private ImageView profile_icon;

    @FXML
    private TableView<Appointment> appointments_table;

    @FXML
    private TableColumn<Appointment, String> barber_col;

    @FXML
    private TableColumn<Appointment, LocalDate> date_col;

    @FXML
    private TableColumn<Appointment, Double> price_col;

    @FXML
    private TableColumn<Appointment, String> service_col;

    @FXML
    private TableColumn<Appointment, LocalTime> time_col;

    @FXML
    private TableColumn<Appointment, String> payment_col;


    private AppointmentDAO appointmentDAO = new ConcreteAppointmentDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barber_col.setCellValueFactory(new PropertyValueFactory<>("barberName"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        service_col.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        time_col.setCellValueFactory(new PropertyValueFactory<>("time"));
        payment_col.setCellValueFactory(new PropertyValueFactory<>("payment"));

        barber_col.setReorderable(false);
        date_col.setReorderable(false);
        price_col.setReorderable(false);
        service_col.setReorderable(false);
        time_col.setReorderable(false);
        payment_col.setReorderable(false);

        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointments = appointmentDAO.findByEmailOfUser(SessionManager.getInstance().getCurrentUserEmail());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        appointments_table.setItems(observableList);
    }

}