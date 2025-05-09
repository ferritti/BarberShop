package PageControllers;

import Services.ProfileService;
import Helpers.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;

import java.util.Map;
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
    public void logoutAction() {
        profileService.logout();
        SceneHelper.switchScene(logoutIcon, "/View/Signin.fxml", "Signin");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneHelper.switchScene(logoutIcon, "/View/AppointmentsCustomer.fxml", "Appointments");
    }

    @FXML
    private void goToNewsView() {
        SceneHelper.switchScene(logoutIcon, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    public void goToNewAppointmentView() {
        SceneHelper.switchScene(logoutIcon, "/View/NewAppointmentCalendar.fxml", "Nuovo Appuntamento");
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }
}