package Controllers;

import Business.ProfileService;
import Helpers.SceneHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Map;
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

    private ProfileService profileService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(profileService == null) {
            profileService = new ProfileService();
        }
        Map<String, String> userData = profileService.getUserData();

        if (!userData.isEmpty()) {
            nameLabel.setText(userData.get("name"));
            surnameLabel.setText(userData.get("surname"));
            emailLabel.setText(userData.get("email"));
            phoneLabel.setText(userData.get("phone"));
        } else {
            System.err.println("No user logged in the session.");
        }
    }

    @FXML
    private void logoutAction() {
        profileService.logout();
        SceneHelper.switchScene(logoutIcon, "/View/Signin.fxml", "Signin");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneHelper.switchScene(logoutIcon, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    @FXML
    private void goToServicesView() {
        SceneHelper.switchScene(logoutIcon, "/View/Services.fxml", "Services");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneHelper.switchScene(logoutIcon, "/View/SendComunication.fxml", "Send Comunication");
    }

    @FXML
    void goToNewsView() {
        SceneHelper.switchScene(logoutIcon, "/View/NewsBarber.fxml", "News");
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }
}