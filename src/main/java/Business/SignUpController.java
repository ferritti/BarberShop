package Business;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class SignUpController {

    @FXML
    private MFXTextField emailField, nameField, phoneField, secretCodeField, surnameField;
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
}
