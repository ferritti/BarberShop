package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class SignupController {

    @FXML
    TextField name_field;
    @FXML
    TextField surname_field;
    @FXML
    TextField email_field_reg;
    @FXML
    PasswordField password_field_reg;
    @FXML
    TextField phone_field;
    @FXML
    TextField secretcode_field;

    @FXML
    Label not_empty_alert;

    @FXML
    Label secret_code_alert;

    UserDAO userDao = new ConcreteUserDAO();


    public void signupAction(ActionEvent actionEvent) {
        String name = name_field.getText();
        String surname = surname_field.getText();
        String email = email_field_reg.getText();
        String password = password_field_reg.getText();
        String phone = phone_field.getText();
        String code = secretcode_field.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            not_empty_alert.setOpacity(1);
        } else if (!code.isEmpty() && AuthenticationService.checkBarberCode(code)) {
            User barber = new Barber(name, surname, email, password, phone);
            userDao.addUser(barber);
            toLoginAction(actionEvent);
        } else if (code.isEmpty()) {
            User customer = new Customer(name, surname, email, password, phone);
            userDao.addUser(customer);
            toLoginAction(actionEvent);
        } else {
            secret_code_alert.setOpacity(1);
        }
    }

    public void toLoginAction(ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
