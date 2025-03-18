package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import Model.User;
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

public class SignupController {

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField nameField;

    @FXML
    private Label notEmptyAlert;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXTextField phoneField;

    @FXML
    private Label secretCodeAlert;

    @FXML
    private MFXTextField secretCodeField;

    @FXML
    private MFXTextField surnameField;

    UserDAO userDao = new ConcreteUserDAO();

    private final String barberCode = "I-AM-A-BARBER";

    public boolean checkBarberCode(String code) {
        return barberCode.equals(code);
    }

    public void signupAction(ActionEvent actionEvent) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String code = secretCodeField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            notEmptyAlert.setVisible(true);
        } else if (!code.isEmpty() && checkBarberCode(code)) {
            User barber = new Barber(name, surname, email, password, phone);
            userDao.addUser(barber);
            goToSigninView();
        } else if (code.isEmpty()) {
            User customer = new Customer(name, surname, email, password, phone);
            userDao.addUser(customer);
            goToSigninView();
        } else {
            secretCodeAlert.setVisible(true);
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
