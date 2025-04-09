package Controllers;

import Business.SendComunicationService;
import Helpers.AlertHelper;
import Helpers.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
            AlertHelper.showError("Error", "Both fields must be filled out.");
            return;
        }

        confirmAndSendComunication(title, message);

        textFieldTitle.clear();
        textFieldMessage.clear();

    }

    private void confirmAndSendComunication(String title, String message) {
        boolean confirmed = AlertHelper.showConfirmation("Confirm Send Communication", "Are you sure you want to send this communication?");

        if (confirmed) {
            if (sendComunicationService.addComunication(title, message)) {
                AlertHelper.showInformation("Success", "Communication sent successfully!");
            } else {
                AlertHelper.showError("Error", "Error while sending the communication.");
            }
        }
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