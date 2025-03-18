package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.NewsDAO;
import Model.Notification;
import Model.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SendComunicationController {

    @FXML
    private ImageView chair_icon;

    @FXML
    private ImageView news_icon;

    @FXML
    private ImageView profile_icon;

    @FXML
    private MFXButton send_button;

    @FXML
    private ImageView send_news_icon;

    @FXML
    private ImageView service_icon;

    @FXML
    private MFXTextField text_field_message;

    @FXML
    private MFXTextField text_field_title;

    NewsDAO newsDAO = new ConcreteNewsDAO();

    @FXML
    private void sendAction() {
        String title = text_field_title.getText().trim();
        String message = text_field_message.getText().trim();

        if (title.isEmpty() || message.isEmpty()) {
            sendAlert("Error", "Both fields must be filled out.");
            return;
        }

        confirmAndSendComunication(title, message);

        text_field_title.clear();
        text_field_message.clear();

    }

    private void confirmAndSendComunication(String title, String message) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Send Communication");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to send this communication?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                Notification notification = new Notification(title, message, Notification.TargetType.CUSTOMER);
                if(newsDAO.addNews(notification)){
                    sendAlert("Success", "Communication sent successfully!");
                } else {
                    sendAlert("Error", "Error while sending the communication.");
            }
        }
        });
    }

    private void sendAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToServiceView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Service.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) service_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Service");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAppointmentsView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Appointments.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) chair_icon.getScene().getWindow();
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

    @FXML
    private void goToProfileView() {
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

            Stage stage = (Stage) profile_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}