package Business;

import Authentication.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileBarberController {

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AppointmentsBarber");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToServicesView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Services.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
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

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Send Comunication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToNewsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}