package Functional;

import PageControllers.SignInController;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class SignInControllerTest extends ApplicationTest {

    private MFXTextField emailField;
    private MFXPasswordField passwordField;
    private Label incorrectLabel;
    private SignInController controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignIn.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Signin");
        stage.show();

        emailField = (MFXTextField) root.lookup("#emailField");
        passwordField = (MFXPasswordField) root.lookup("#passwordField");
        incorrectLabel = (Label) root.lookup("#incorrectLabel");

        controller = loader.getController();
    }

    @BeforeAll
    public static void setupDatabaseConnection() {
        DBManager manager = DBManager.getInstance(true);
        Connection connection = manager.getConnection();

        try (Statement stmt = connection.createStatement()) {
            DBTestInitializer.createUsersTable(stmt);
            String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());
            stmt.executeUpdate("INSERT INTO users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Luigi', 'Bianchi', 'luigi.bianchi@example.com', '" + hashedPassword + "', '1234567890', 'CUSTOMER')");

            String hashedBarberPassword = BCrypt.hashpw("barberpass", BCrypt.gensalt());
            stmt.executeUpdate("INSERT INTO users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Mario', 'Rossi', 'mario.rossi@example.com', '" + hashedBarberPassword + "', '0987654321', 'BARBER')");

            DBTestInitializer.createServiceTypesTable(stmt);
            DBTestInitializer.createAppointmentsTable(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore nell'inizializzazione del database di test H2");
        }
    }

    @BeforeEach
    public void resetFields() {
        Platform.runLater(() -> {
            emailField.clear();
            passwordField.clear();
            incorrectLabel.setVisible(false);
        });
    }

    @AfterAll
    public static void closeDatabaseConnection() {
        DBManager manager = DBManager.getInstance(true);
        Connection connection = manager.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE email = 'luigi.bianchi@example.com'");
            stmt.executeUpdate("DELETE FROM users WHERE email = 'mario.rossi@example.com'");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore nella pulizia del database dopo i test");
        }

        manager.close();
    }

    @Test
    public void testSuccessfulLoginAsCustomer() throws Exception {
        write(emailField, "luigi.bianchi@example.com");
        write(passwordField, "password123");

        clickOn("#signinButton");

        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Customer".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Customer", stage.getTitle());
    }

    @Test
    public void testSuccessfulLoginAsBarber() throws Exception {
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "barberpass");

        clickOn("#signinButton");

        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Barber".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Barber", stage.getTitle());
    }

    @Test
    public void testLoginFailureWrongCredentials() {
        write(emailField, "luigi.bianchi@example.com");
        write(passwordField, "wrongpassword");

        clickOn("#signinButton");

        assertTrue(incorrectLabel.isVisible());
    }

    @Test
    public void testGoToSignupView() throws Exception {
        clickOn("#createNowLabel");

        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Sign Up".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Sign Up", stage.getTitle());
    }

    @Test
    public void testForgotPassPopupWithExistingEmail() throws Exception {
        clickOn("#forgotPasswordLabel");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(".text-field").write("luigi.bianchi@example.com");

        clickOn("Send");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testForgotPassPopupWithNonExistingEmail() throws Exception {
        clickOn("#forgotPasswordLabel");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(".text-field").write("emailinesistente@example.com");

        clickOn("Send");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn("Retry");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn(".text-field").eraseText(30).write("emailinesistente2@example.com");

        clickOn("Send");

        WaitForAsyncUtils.waitForFxEvents();

        clickOn("Cancel");

        WaitForAsyncUtils.waitForFxEvents();
    }

    private void write(MFXTextField field, String text) {
        clickOn(field).write(text);
    }
}