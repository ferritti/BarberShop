package Business;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
}