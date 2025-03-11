package Business;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import Authentication.SessionManager;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML
    private ImageView chair_icon;

    @FXML
    private Label email_label;

    @FXML
    private ImageView logout_icon;

    @FXML
    private Label name_label;

    @FXML
    private ImageView news_icon;

    @FXML
    private Label phone_label;

    @FXML
    private ImageView plus_icon;

    @FXML
    private ImageView profile_icon;

    @FXML
    private Label surname_label;

    public void profileAction(String name, String surname, String email, String phone) {
        name_label.setText(name);
        surname_label.setText(surname);
        email_label.setText(email);
        phone_label.setText(phone);
    }

    @FXML
    private void logoutAction() {
        try {

            SessionManager.getInstance().closeSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) logout_icon.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}