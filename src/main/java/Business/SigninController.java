package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Customer;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;

public class SigninController {
    @FXML
    private MFXTextField emailField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private Label incorrectLabel;

    @FXML
    private Label forgot_pass_label;

    UserDAO userDAO = new ConcreteUserDAO();

    public void signinAction(ActionEvent actionEvent) {
        String emailText = emailField.getText();
        String passText = passwordField.getText();


        if (userDAO.checkCredentials(emailText, passText)) {
            SessionManager.getInstance().setCurrentUser(userDAO.findByEmail(emailText));
            if(SessionManager.getInstance().getCurrentUser() instanceof Customer)
                toAppointmentCustomerView(actionEvent);
            else
                toAppointmentBarberView(actionEvent);
        }
        else incorrectLabel.setVisible(true);
    }

    @FXML
    public void forgotPassPopUp() {
        while (true) {
            javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Password Recovery");
            dialog.setHeaderText("Enter your email to recover your password");

            javafx.scene.control.ButtonType confirmButtonType = new javafx.scene.control.ButtonType("Send", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, javafx.scene.control.ButtonType.CANCEL);

            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            javafx.scene.control.TextField emailTextField = new javafx.scene.control.TextField();
            emailTextField.setPromptText("Email");

            grid.add(new javafx.scene.control.Label("Email:"), 0, 0);
            grid.add(emailTextField, 1, 0);

            dialog.getDialogPane().setContent(grid);
            javafx.application.Platform.runLater(emailTextField::requestFocus);

            dialog.setResultConverter(dialogButton -> dialogButton == confirmButtonType ? emailTextField.getText() : null);

            java.util.Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String email = result.get();

                if (!checkEmailExists(email)) {
                    javafx.scene.control.Alert retryAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    retryAlert.setTitle("Error");
                    retryAlert.setHeaderText(null);
                    retryAlert.setContentText("The email is not registered in our system.");

                    javafx.scene.control.ButtonType retryButton = new javafx.scene.control.ButtonType("Retry", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
                    retryAlert.getButtonTypes().setAll(retryButton, javafx.scene.control.ButtonType.CANCEL);

                    java.util.Optional<javafx.scene.control.ButtonType> retryResult = retryAlert.showAndWait();

                    if (retryResult.isPresent() && retryResult.get() == retryButton) {
                        continue;
                    }
                } else {
                    javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Email Sent");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("A password recovery email has been sent to: " + email);
                    successAlert.showAndWait();
                }
            }
            break;
        }
    }

    private boolean checkEmailExists(String email) {
        return userDAO.findByEmail(email) != null;
    }


    public void toSignupView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Signup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toAppointmentCustomerView(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toAppointmentBarberView(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}