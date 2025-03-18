package Business;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import Authentication.SessionManager;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileCustomerController {

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


    public void profileAction(String name, String surname, String email, String phone) {
        nameLabel.setText(name);
        surnameLabel.setText(surname);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
    }

    @FXML
    private void logoutAction() {
        try {
            SessionManager.getInstance().closeSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Signin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAppointmentsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToNewsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToNewAppointmentView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentCalendar.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("New Appointment");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}