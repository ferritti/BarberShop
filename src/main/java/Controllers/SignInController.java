package Controllers;

import Business.SignInService;
import Helpers.AlertHelper;
import Helpers.SceneNavigator;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class SignInController {
    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private Label incorrectLabel;

    private final SignInService signinService = new SignInService();


    public SignInController() {}

    @FXML
    public void signinAction(ActionEvent actionEvent) {
        String emailText = emailField.getText();
        String passText = passwordField.getText();

        if (signinService.authenticateUser(emailText, passText)) {
            if (signinService.isCustomer()) {
                goToAppointmentsCustomerView();
            } else {
                goToAppointmentsBarberView();
            }
        } else {
            incorrectLabel.setVisible(true);
        }
    }

    @FXML
    public void forgotPassPopUp() {
        while (true) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Password Recovery");
            dialog.setHeaderText("Enter your email to recover your password");

            ButtonType confirmButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField emailTextField = new TextField();
            emailTextField.setPromptText("Email");

            grid.add(new Label("Email:"), 0, 0);
            grid.add(emailTextField, 1, 0);

            dialog.getDialogPane().setContent(grid);
            Platform.runLater(emailTextField::requestFocus);

            dialog.setResultConverter(dialogButton -> dialogButton == confirmButtonType ? emailTextField.getText() : null);

            dialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            dialog.getDialogPane().getStyleClass().add("custom-alert");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String email = result.get();
                if (!signinService.checkEmailExists(email)) {
                    boolean retry = AlertHelper.showError("Error", "The email is not registered in our system.", "Retry", "Cancel");
                    if (retry) {
                        continue;
                    }
                } else {
                    AlertHelper.showInformation("Email Sent", "A password recovery email has been sent to: " + email);
                }
            }
            break;
        }
    }


    @FXML
    public void goToSignupView() {
        SceneNavigator.switchScene(emailField, "/View/Signup.fxml", "Sign Up");
    }

    @FXML
    public void goToAppointmentsCustomerView(){
        SceneNavigator.switchScene(emailField, "/View/AppointmentsCustomer.fxml", "Customer");
    }

    @FXML
    public void goToAppointmentsBarberView(){
       SceneNavigator.switchScene(emailField, "/View/AppointmentsBarber.fxml", "Barber");
    }
}