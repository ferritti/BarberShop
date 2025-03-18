package Business;

import Authentication.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class ProfileBarberController {

    @FXML
    private ImageView chair_icon;

    @FXML
    private Label email_label;

    @FXML
    private ImageView logout_icon;

    @FXML
    private Label name_label;

    @FXML
    private ImageView news_icon;

    @FXML
    private Label phone_label;

    @FXML
    private ImageView plus_icon;

    @FXML
    private ImageView profile_icon;

    @FXML
    private ImageView send_news_icon;

    @FXML
    private ImageView service_icon;

    @FXML
    private Label surname_label;

    public void profileAction(String name, String surname, String email, String phone) {
        name_label.setText(name);
        surname_label.setText(surname);
        email_label.setText(email);
        phone_label.setText(phone);
    }

    @FXML
    private void logoutAction() {
        try {

            SessionManager.getInstance().closeSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Signin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAppointmentsAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) chair_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("AppointmentsBarber");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToServiceAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Service.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) chair_icon.getScene().getWindow();
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

            Stage stage = (Stage) send_news_icon.getScene().getWindow();
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

            Stage stage = (Stage) news_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}