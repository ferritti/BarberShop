package Functional;

import Controllers.SignInController;
import DBconnection.Database.DBManager;
import DBconnection.Database.DBtestInitializer;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public static void initDb() {
        DBManager manager = DBManager.getInstance(true);
        try (Connection connection = manager.getConnection();
             Statement stmt = connection.createStatement()) {
            DBtestInitializer.createUsersTable(stmt);

            try (PreparedStatement ps = connection.prepareStatement("""
                     INSERT INTO Users (name, surname, email, pass_hash, phone, role)
                     VALUES (?, ?, ?, ?, ?, ?)
                 """)) {
                ps.setString(1, "Mario");
                ps.setString(2, "Rossi");
                ps.setString(3, "mario.rossi@example.com");
                ps.setString(4, "password123"); // usa hash reale se necessario
                ps.setString(5, "1234567890");
                ps.setString(6, "CUSTOMER");
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore durante l'inizializzazione del database di test");
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
    public static void closeDb() {
        DBManager.getInstance(true).close();
    }

    @Test
    public void testSuccessfulLoginAsCustomer() throws Exception {
        write(emailField, "mario.rossi@example.com");
        write(passwordField, "password123");

        performClick();

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

        performClick();

        assertTrue(incorrectLabel.isVisible(),
                "Se la password è sbagliata, l'etichetta di errore deve essere visibile");
    }

    private void performClick() {
        clickOn("#signinButton");
    }

    private void write(MFXTextField field, String text) {
        clickOn(field).write(text);
    }
}
