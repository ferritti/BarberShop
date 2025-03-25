package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class SignUpController {

    @FXML
    private MFXTextField emailField, nameField, phoneField, secretCodeField, surnameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private Label notEmptyAlert, secretCodeAlert;

    private final SignUpService signUpService;

    public SignUpController() {
        UserDAO userDAO = new ConcreteUserDAO();
        this.signUpService = new SignUpService(userDAO);
    }

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService; // Per i test
    }

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Signin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
