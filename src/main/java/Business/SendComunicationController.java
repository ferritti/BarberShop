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

    private final SendComunicationService sendComunicationService = new SendComunicationService();

    @FXML
    private void sendAction() {
        String title = textFieldTitle.getText().trim();
        String message = textFieldMessage.getText().trim();

        if (sendComunicationService.areEmptyFields(title, message)) {
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
                if(sendComunicationService.addComunication(title, message)){
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
    private void goToServicesView() {
       SceneNavigator.switchScene(textFieldMessage, "/View/Services.fxml", "Services");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneNavigator.switchScene(textFieldMessage, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    @FXML
    private void goToNewsView() {
        SceneNavigator.switchScene(textFieldMessage, "/View/NewsBarber.fxml", "News");
    }

    @FXML
    private void goToProfileView() {
        SceneNavigator.switchScene(textFieldMessage, "/View/ProfileBarber.fxml", "Profile");
    }
}