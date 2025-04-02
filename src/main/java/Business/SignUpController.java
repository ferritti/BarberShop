package Business;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SignUpController {
    @FXML
    private MFXButton signUpButton;
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

    public void signupAction(ActionEvent actionEvent) {
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

    public void goToSigninView() {
        SceneNavigator.switchScene(emailField, "/View/Signin.fxml", "Signin");
    }


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