package Controllers;

import Authentication.SessionManager;
import Helpers.SceneNavigator;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class ProfileBarberController implements Initializable {

    @FXML
    private Label emailLabel;

    @FXML
    private ImageView logoutIcon;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label surnameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            nameLabel.setText(currentUser.getName());
            surnameLabel.setText(currentUser.getSurname());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhone());
        } else {
            System.err.println("No user logged in the session.");
        }
    }

    @FXML
    private void logoutAction() {
        SessionManager.getInstance().resetUser();
        SceneNavigator.switchScene(logoutIcon, "/View/Signin.fxml", "Signin");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneNavigator.switchScene(logoutIcon, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    @FXML
    private void goToServicesView() {
        SceneNavigator.switchScene(logoutIcon, "/View/Services.fxml", "Services");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneNavigator.switchScene(logoutIcon, "/View/SendComunication.fxml", "Send Comunication");
    }

    @FXML
    void goToNewsView() {
        SceneNavigator.switchScene(logoutIcon, "/View/NewsBarber.fxml", "News");
    }
}