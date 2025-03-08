package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Customer;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    TextField email_field;
    @FXML
    TextField password_field;
    @FXML
    Label incorrect_label;

    UserDAO userDAO = new ConcreteUserDAO();


    String email = "admin";
    String pass = "admin";



    public void loginAction(ActionEvent actionEvent) {
        String email_text = email_field.getText();
        String pass_text = password_field.getText();

            if (userDAO.checkCredentials(email_text, pass_text)) {
                incorrect_label.setText("Login Successful");
            }
                incorrect_label.setOpacity(1);
    }

    public void toSignupAction(ActionEvent actionEvent) {

    }
}
