package Functional;

import Authentication.SessionManager;
import PageControllers.ServiceTypesController;
import Model.Barber;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import Model.ServiceType;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTypesControllerTest extends ApplicationTest {

    private MFXTextField textFieldName;
    private MFXTextField textFieldPrice;
    private TableView<ServiceType> serviceTable;
    private ServiceTypesController controller;
    private Stage stage;

    private boolean serviceAdded = false;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        Barber dummyCustomer = new Barber("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyCustomer);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Services.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Services");
        stage.show();

        textFieldName = (MFXTextField) root.lookup("#textFieldName");
        textFieldPrice = (MFXTextField) root.lookup("#textFieldPrice");
        serviceTable = (TableView<ServiceType>) root.lookup("#serviceTable");

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
        } catch (Exception e) {
            e.printStackTrace();
            fail("Errore durante l'inizializzazione del database H2");
        }
    }

    @AfterAll
    public static void closeDatabaseConnection() {
        DBManager.getInstance(true).close();
    }

    @BeforeEach
    public void clearInputFieldsAndDatabase() {
        Platform.runLater(() -> {
            textFieldName.clear();
            textFieldPrice.clear();
        });
        WaitForAsyncUtils.waitForFxEvents();

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM Service_types");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore nella pulizia della tabella Service_types.");
        }
    }

    @Test
    public void testAddServiceSuccess() throws Exception {
        write(textFieldName, "Taglio Capelli");
        write(textFieldPrice, "15.50");

        clickOn("#addNewServiceButton");
        clickOn("Yes");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Service_types WHERE service_name='Taglio Capelli'")) {

            assertTrue(rs.next());
            assertEquals(15.50, rs.getDouble("price"));
            serviceAdded = true;
        }
    }

    @AfterEach
    public void cleanupServiceAfterAddTest() {
        if (serviceAdded) {
            try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
                stmt.executeUpdate("DELETE FROM Service_types WHERE service_name = 'Taglio Capelli'");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Errore nella pulizia del database dopo l'inserimento del servizio.");
            }
            serviceAdded = false;
        }
    }

    private void write(MFXTextField textFieldName, String text) {
        clickOn(textFieldName).write(text);
    }

    @Test
    public void testAddServiceEmptyFields() {
        clickOn("#addNewServiceButton");

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Service_types")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total);
        } catch (SQLException e) {
            fail("Errore durante la verifica del database");
        }
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
    public void testNavigationToSendComunicationView() {
        clickOn("#sendComunicationButton");
        WaitForAsyncUtils.waitForFxEvents();

        long start = System.currentTimeMillis();
        while (!"Send Comunication".equals(stage.getTitle()) && System.currentTimeMillis() - start < 5000) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }
        assertEquals("Send Comunication", stage.getTitle());
    }

    @Test
    public void testDeleteAnyService() throws Exception {
        String serviceName = "Servizio Test";
        String servicePrice = "20.00";

        write(textFieldName, serviceName);
        write(textFieldPrice, servicePrice);
        clickOn("#addNewServiceButton");

        clickOn("Yes");
        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200);

        Optional<Node> matchingRow = lookup(".table-row-cell")
                .queryAll()
                .stream()
                .filter(node -> node instanceof TableRow<?> tableRow &&
                        tableRow.getItem() instanceof ServiceType service &&
                        service.getName().equals(serviceName))
                .findFirst();

        Node deleteButton = from(matchingRow.get()).lookup("#delete-button").query();
        clickOn(deleteButton);
        clickOn("Yes");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Types WHERE service_name = 'Servizio Test'");
            assertFalse(rs.next());
        }
    }
}