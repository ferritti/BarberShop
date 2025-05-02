package Functional;

import Controllers.SignInController;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBtestInitializer;
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
            DBtestInitializer.createUsersTable(stmt);
            String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());
            stmt.executeUpdate("INSERT INTO users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Mario', 'Rossi', 'mario.rossi@example.com', '" + hashedPassword + "', '1234567890', 'CUSTOMER')");
            DBtestInitializer.createServiceTypesTable(stmt);
            DBtestInitializer.createAppointmentsTable(stmt);
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
        manager.close();
    }

    @Test
    public void testSuccessfulLoginAsCustomer() throws Exception {
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "password123");

        clickOn("#signinButton");

        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Customer".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Customer", stage.getTitle(),
                "Dopo un login valido, dovrebbe andare alla vista del cliente");
    }

    @Test
    public void testLoginFailureWrongCredentials() {
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "wrongpassword");

        clickOn("#signinButton");

        assertTrue(incorrectLabel.isVisible());
    }

    @Test
    public void testGoToSignupView() throws Exception {
        // Simula il click sul link o pulsante "Create now"
        clickOn("#createNowLabel"); // Assicurati che l'ID corrisponda a quello in SignIn.fxml

        WaitForAsyncUtils.waitForFxEvents();

        // Attendi cambio scena
        long start = System.currentTimeMillis();
        while (!"Sign Up".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Verifica che sia avvenuto il cambio alla schermata di registrazione
        assertEquals("Sign Up", stage.getTitle(),
                "Dopo il click su 'Create now', dovrebbe passare alla schermata di registrazione");
    }

    private void write(MFXTextField field, String text) {
        clickOn(field).write(text);
    }
}
