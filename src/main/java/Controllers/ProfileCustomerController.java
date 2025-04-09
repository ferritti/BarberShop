package Controllers;

import Helpers.SceneNavigator;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import Authentication.SessionManager;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;

import java.net.URL;

public class ProfileCustomerController implements Initializable{

    @FXML
    private Label emailLabel;

    @FXML
    private Label surnameLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private ImageView logoutIcon;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName());
            surnameLabel.setText(currentUser.getSurname());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhone());
        } else {
            System.err.println("Nessun utente loggato nella sessione.");
        }
    }

    @FXML
    private void logoutAction() {
        SessionManager.getInstance().resetUser();
        SceneNavigator.switchScene(logoutIcon, "/View/Signin.fxml", "Signin");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneNavigator.switchScene(logoutIcon, "/View/AppointmentsCustomer.fxml", "Appointments");
    }

    @FXML
    private void goToNewsView() {
        SceneNavigator.switchScene(logoutIcon, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    void goToNewAppointmentView() {
        SceneNavigator.switchScene(logoutIcon, "/View/NewAppointmentCalendar.fxml", "Nuovo Appuntamento");
    }
}