package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        } else if (code.isEmpty()) {
            User customer = new Customer(name, surname, email, password, phone);
            userDao.addUser(customer);
        } else {
            secret_code_alert.setOpacity(1);
        }
    }

    public void toLoginAction(ActionEvent actionEvent) {
        System.out.println("ciao");
    }

}
