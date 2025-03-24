package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.NewsDAO;
import Model.Notification;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SendComunicationController {

    @FXML
    private TextArea textFieldMessage;

    @FXML
    private TextField textFieldTitle;

    NewsDAO newsDAO = new ConcreteNewsDAO();

    @FXML
    private void sendAction() {
        String title = textFieldTitle.getText().trim();
        String message = textFieldMessage.getText().trim();

        if (title.isEmpty() || message.isEmpty()) {
            sendAlert("Error", "Both fields must be filled out.");
            return;
        }

        confirmAndSendComunication(title, message);

        textFieldTitle.clear();
        textFieldMessage.clear();

    }

    private void confirmAndSendComunication(String title, String message) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Send Communication");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to send this communication?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmDialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        confirmDialog.getDialogPane().getStyleClass().add("custom-alert");

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                Notification notification = new Notification(title, message, true);
                if(newsDAO.addNotification(notification)){
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
        alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait();
    }

    @FXML
    private void goToServiceView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Services.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) textFieldMessage.getScene().getWindow();
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) textFieldMessage.getScene().getWindow();
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

            Stage stage = (Stage) textFieldMessage.getScene().getWindow();
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

            Stage stage = (Stage) textFieldMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}