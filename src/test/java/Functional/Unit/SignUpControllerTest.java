package Functional.Unit;

import Business.SignUpController;
import DBconnection.Database.DBManager;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpControllerTest extends ApplicationTest {

    private MFXTextField nameField, surnameField, emailField, phoneField, secretCodeField;
    private MFXPasswordField passwordField;
    private Label notEmptyAlert, secretCodeAlert;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

        // Inizializza i campi del form
        nameField = (MFXTextField) root.lookup("#nameField");
        surnameField = (MFXTextField) root.lookup("#surnameField");
        emailField = (MFXTextField) root.lookup("#emailField");
        passwordField = (MFXPasswordField) root.lookup("#passwordField");
        phoneField = (MFXTextField) root.lookup("#phoneField");
        secretCodeField = (MFXTextField) root.lookup("#secretCodeField");
        notEmptyAlert = (Label) root.lookup("#notEmptyAlert");
        secretCodeAlert = (Label) root.lookup("#secretCodeAlert");

        // Setta i campi nel controller
        SignUpController controller = loader.getController();
        controller.setNameField(nameField);
        controller.setSurnameField(surnameField);
        controller.setEmailField(emailField);
        controller.setPhoneField(phoneField);
        controller.setSecretCodeField(secretCodeField);
        controller.setPasswordField(passwordField);
        controller.setNotEmptyAlert(notEmptyAlert);
        controller.setSecretCodeAlert(secretCodeAlert);
    }

    @BeforeAll
    public static void setupDatabaseConnection() {
        DBManager manager = DBManager.getInstance(true);
        Connection connection = manager.getConnection();
    }

    @BeforeEach
    public void resetFields() {
        nameField.clear();
        surnameField.clear();
        emailField.clear();
        phoneField.clear();
        secretCodeField.clear();
        passwordField.clear();
        notEmptyAlert.setVisible(false);
        secretCodeAlert.setVisible(false);
    }

    @AfterAll
    public static void closeDatabaseConnection() {
        DBManager manager = DBManager.getInstance(true);
        manager.close();
    }

    @Test
    public void testSuccessfulSignup() {
        write(nameField, "John");
        write(surnameField, "Doe");
        write(emailField, "john.doe@example.com");
        write(phoneField, "1234567890");
        write(secretCodeField, "I-AM-A-BARBER");
        write(passwordField, "password123");

        performClickOn();

        assertFalse(secretCodeAlert.isVisible());
    }

    @Test
    public void testEmptyFields() {
        write(nameField, "");
        write(surnameField, "Doe");
        write(emailField, "");
        write(phoneField, "1234567890");
        write(secretCodeField, "");
        write(passwordField, "password123");

        performClickOn();

        assertTrue(notEmptyAlert.isVisible());
    }

    @Test
    public void testInvalidSecretCode() {
        write(nameField, "John");
        write(surnameField, "Doe");
        write(emailField, "john.doe@example.com");
        write(phoneField, "1234567890");
        write(secretCodeField, "wrongcode");
        write(passwordField, "password123");

        performClickOn();

        assertTrue(secretCodeAlert.isVisible());
    }

    private void performClickOn() {
        clickOn("#signUpButton");
    }

    private void write(MFXTextField field, String text) {
        clickOn(field).write(text);
    }
}
