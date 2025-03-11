package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Customer;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField email_field;
    @FXML
    TextField password_field;
    @FXML
    Label incorrect_label;

    UserDAO userDAO = new ConcreteUserDAO();

    public void loginAction(ActionEvent actionEvent) {
        String email_text = email_field.getText();
        String pass_text = password_field.getText();

        if (userDAO.checkCredentials(email_text, pass_text)) {
                SessionManager.getInstance(email_text);
                toProfileAction();
            }
        else incorrect_label.setOpacity(1);
    }

    public void toSignupAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Signup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void toProfileAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            Parent profileRoot = loader.load();

            ProfileController profileController = loader.getController();

            User user = userDAO.findByEmail(SessionManager.getCurrentUserEmail());

            if (user != null) {
                profileController.profileAction(
                        user.getName(),
                        user.getSurname(),
                        user.getEmail(),
                        user.getPhone()
                );
            }

            Stage stage = (Stage) email_field.getScene().getWindow();

            Scene profileScene = new Scene(profileRoot);
            stage.setScene(profileScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}