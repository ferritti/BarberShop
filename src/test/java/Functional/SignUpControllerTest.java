package Functional;

import PageControllers.SignUpController;
import Persistence.DBConnection.*;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
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
import org.testfx.util.WaitForAsyncUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpControllerTest extends ApplicationTest {

    private MFXTextField nameField, surnameField, emailField, phoneField, secretCodeField;
    private MFXPasswordField passwordField;
    private Label notEmptyAlert, secretCodeAlert;
    private SignUpController controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

        nameField = (MFXTextField) root.lookup("#nameField");
        surnameField = (MFXTextField) root.lookup("#surnameField");
        emailField = (MFXTextField) root.lookup("#emailField");
        passwordField = (MFXPasswordField) root.lookup("#passwordField");
        phoneField = (MFXTextField) root.lookup("#phoneField");
        secretCodeField = (MFXTextField) root.lookup("#secretCodeField");
        notEmptyAlert = (Label) root.lookup("#notEmptyAlert");
        secretCodeAlert = (Label) root.lookup("#secretCodeAlert");

        controller = loader.getController();
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

        try (Statement stmt = connection.createStatement()) {
            DBTestInitializer.createUsersTable(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @BeforeEach
    public void resetFields() {
        Platform.runLater(() -> {
            nameField.clear();
            surnameField.clear();
            emailField.clear();
            phoneField.clear();
            secretCodeField.clear();
            passwordField.clear();
            notEmptyAlert.setVisible(false);
            secretCodeAlert.setVisible(false);
        });
    }

    @AfterAll
    public static void closeDatabaseConnection() {
        DBManager.getInstance(true).close();
    }

    @Test
    public void testSuccessfulSignupNavigatesToSignin() throws Exception {
        write(nameField, "Mario");
        write(surnameField, "Rossi");
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "password123");
        write(phoneField, "1234567890");
        write(secretCodeField, "I-AM-A-BARBER");


        performClickOn();

        WaitForAsyncUtils.waitForFxEvents();

        long startTime = System.currentTimeMillis();
        while (!"Signin".equals(stage.getTitle()) && System.currentTimeMillis() - startTime < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Signin", stage.getTitle());
    }

    @Test
    public void testEmptyFields() {
        write(nameField, "");
        write(surnameField, "Rossi");
        write(emailField, "");
        write(passwordField, "password123");
        write(phoneField, "1234567890");
        write(secretCodeField, "");


        performClickOn();

        assertTrue(notEmptyAlert.isVisible());
    }

    @Test
    public void testInvalidSecretCode() {
        write(nameField, "Mario");
        write(surnameField, "Rossi");
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "password123");
        write(phoneField, "1234567890");
        write(secretCodeField, "wrongcode");


        performClickOn();

        assertTrue(secretCodeAlert.isVisible());
    }

    @Test
    public void testGoToSigninView() throws Exception {
        clickOn("#signinLabel");

        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Signin".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Signin", stage.getTitle());
    }

    private void performClickOn() {
        clickOn("#signUpButton");
    }

    private void write(MFXTextField field, String text) {
        clickOn(field).write(text);
    }
}
