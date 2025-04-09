package Controllers;

import Business.SignInService;
import Helpers.SceneNavigator;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
            javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Password Recovery");
            dialog.setHeaderText("Enter your email to recover your password");

            javafx.scene.control.ButtonType confirmButtonType = new javafx.scene.control.ButtonType("Send", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, javafx.scene.control.ButtonType.CANCEL);

            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            javafx.scene.control.TextField emailTextField = new javafx.scene.control.TextField();
            emailTextField.setPromptText("Email");

            grid.add(new javafx.scene.control.Label("Email:"), 0, 0);
            grid.add(emailTextField, 1, 0);

            dialog.getDialogPane().setContent(grid);
            javafx.application.Platform.runLater(emailTextField::requestFocus);

            dialog.setResultConverter(dialogButton -> dialogButton == confirmButtonType ? emailTextField.getText() : null);
            dialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            dialog.getDialogPane().getStyleClass().add("custom-alert");
            java.util.Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String email = result.get();

                if (!signinService.checkEmailExists(email)) {
                    javafx.scene.control.Alert retryAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    retryAlert.setTitle("Error");
                    retryAlert.setHeaderText(null);
                    retryAlert.setContentText("The email is not registered in our system.");

                    javafx.scene.control.ButtonType retryButton = new javafx.scene.control.ButtonType("Retry", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
                    retryAlert.getButtonTypes().setAll(retryButton, javafx.scene.control.ButtonType.CANCEL);
                    retryAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
                    retryAlert.getDialogPane().getStyleClass().add("custom-alert");

                    java.util.Optional<javafx.scene.control.ButtonType> retryResult = retryAlert.showAndWait();

                    if (retryResult.isPresent() && retryResult.get() == retryButton) {
                        continue;
                    }
                } else {
                    javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Email Sent");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("A password recovery email has been sent to: " + email);
                    successAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
                    successAlert.getDialogPane().getStyleClass().add("custom-alert");
                    successAlert.showAndWait();
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