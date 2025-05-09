package Functional;

import Authentication.SessionManager;
import PageControllers.SendComunicationController;
import Model.Customer;
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
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Customer customer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(customer);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SendComunication.fxml"));
        Parent root = loader.load();

        this.stage = stage;
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
            DBTestInitializer.createServiceTypesTable(stmt);
            DBTestInitializer.createNewsTable(stmt);
            DBTestInitializer.createAppointmentsTable(stmt);
            DBTestInitializer.createAvailableSlotsTable(stmt);
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

    @Test
    public void testNavigationToAppointmentsView() {
        clickOn("#appointmentsButton");
        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Appointments".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
        assertEquals("Appointments", stage.getTitle());
    }

    @Test
    public void testNavigationToServicesView() {
        clickOn("#serviceButton");
        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Services".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
        assertEquals("Services", stage.getTitle());
    }


    @Test
    public void testNavigationToNewsView() {
        clickOn("#newsButton");
        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"News".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
        assertEquals("News", stage.getTitle());
    }

    @Test
    public void testNavigationToProfileView() {
        clickOn("#profileButton");
        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Profile".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
        assertEquals("Profile", stage.getTitle());
    }


    private void write(TextField field, String text) {
        clickOn(field).write(text);
    }

    private void write(TextArea area, String text) {
        clickOn(area).write(text);
    }
}