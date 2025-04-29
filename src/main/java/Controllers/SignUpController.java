package Controllers;

import Business.SignUpService;
import Helpers.SceneHelper;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SignUpController {

    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXTextField nameField;
    @FXML
    private MFXTextField phoneField;
    @FXML
    private MFXTextField secretCodeField;
    @FXML
    private MFXTextField surnameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private Label notEmptyAlert, secretCodeAlert;

    private SignUpService signUpService = new SignUpService();

    @FXML
    public void signupAction() {
        String result = signUpService.registerUser(
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                passwordField.getText(),
                phoneField.getText(),
                secretCodeField.getText()
        );

        notEmptyAlert.setVisible("notEmptyAlert".equals(result));
        secretCodeAlert.setVisible("secretCodeAlert".equals(result));

        if ("success".equals(result)) {
            goToSigninView();
        }
    }

    @FXML
    public void goToSigninView() {
        SceneHelper.switchScene(emailField, "/View/Signin.fxml", "Signin");
    }


    //only for testing
    public void setEmailField(MFXTextField emailField) {
        this.emailField = emailField;
    }

    public void setNameField(MFXTextField nameField) {
        this.nameField = nameField;
    }

    public void setPhoneField(MFXTextField phoneField) {
        this.phoneField = phoneField;
    }

    public void setSecretCodeField(MFXTextField secretCodeField) {
        this.secretCodeField = secretCodeField;
    }

    public void setSurnameField(MFXTextField surnameField) {
        this.surnameField = surnameField;
    }

    public void setPasswordField(MFXPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public void setNotEmptyAlert(Label notEmptyAlert) {
        this.notEmptyAlert = notEmptyAlert;
    }

    public void setSecretCodeAlert(Label secretCodeAlert) {
        this.secretCodeAlert = secretCodeAlert;
    }
}