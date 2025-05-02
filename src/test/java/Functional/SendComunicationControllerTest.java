package Functional;

import Controllers.SendComunicationController;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class SendComunicationControllerTest extends ApplicationTest {

    private TextField textFieldTitle;
    private TextArea textFieldMessage;
    private SendComunicationController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SendComunication.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Send Comunication");
        stage.show();

        textFieldTitle = (TextField) root.lookup("#textFieldTitle");
        textFieldMessage = (TextArea) root.lookup("#textFieldMessage");

        controller = loader.getController();
    }

    @BeforeAll
    public static void setupDatabaseConnection() {
        DBManager manager = DBManager.getInstance(true);
        Connection connection = manager.getConnection();

        try (Statement stmt = connection.createStatement()) {
            DBTestInitializer.createUsersTable(stmt);
            DBTestInitializer.createNewsTable(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore nell'inizializzazione del database di test H2");
        }
    }

    @BeforeEach
    public void clearFieldsAndCleanNews() throws SQLException {
        Platform.runLater(() -> {
            textFieldTitle.clear();
            textFieldMessage.clear();
        });
        WaitForAsyncUtils.waitForFxEvents();

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM News");
        }
    }

    @AfterAll
    public static void closeDatabaseConnection() {
        DBManager.getInstance(true).close();
    }

    @AfterEach
    public void deleteTestComunication() throws SQLException {
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM News WHERE title = 'Nuova Prom'");
        }
    }

    @Test
    public void testSendComunicationSuccess() throws Exception {
        write(textFieldTitle, "Nuova Prom");
        write(textFieldMessage, "Prom taglio a 10€ questa settimana!");

        clickOn("#send_button");

        clickOn("Yes");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM News WHERE title='Nuova Prom'")) {

            assertTrue(rs.next());
            assertEquals("Prom taglio a 10€ questa settimana!", rs.getString("message"));
        }
    }

    @Test
    public void testSendComunicationEmptyFields() {
        clickOn("#send_button");

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total);
        } catch (SQLException e) {
            fail("Errore durante la verifica del database");
        }
    }

    private void write(TextField field, String text) {
        clickOn(field).write(text);
    }

    private void write(TextArea area, String text) {
        clickOn(area).write(text);
    }
}