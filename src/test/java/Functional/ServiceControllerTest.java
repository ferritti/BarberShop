package Functional;

import Authentication.SessionManager;
import Controllers.ServicesController;
import Model.Barber;
import Model.Customer;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBtestInitializer;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import Model.ServiceType;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceControllerTest extends ApplicationTest {

    private MFXTextField textFieldName;
    private MFXTextField textFieldPrice;
    private TableView<ServiceType> serviceTable;
    private ServicesController controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // 1. Customer fittizio loggato
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
            DBtestInitializer.createUsersTable(stmt);
            DBtestInitializer.createServiceTypesTable(stmt);
            DBtestInitializer.createNewsTable(stmt);
            DBtestInitializer.createAppointmentsTable(stmt);
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
    public void clearInputFields() {
        Platform.runLater(() -> {
            textFieldName.clear();
            textFieldPrice.clear();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testAddServiceSuccess() throws Exception {
        // Simula l'inserimento di un nuovo servizio
        write(textFieldName, "Taglio Capelli");
        write(textFieldPrice, "15.50");

        clickOn("#addNewServiceButton");

        // Conferma l'operazione
        clickOn("Yes");

        // Attendi che l'inserimento sia completato
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200); // In attesa della scrittura sul DB

        // Verifica nel database che il servizio è stato aggiunto
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Service_types WHERE service_name='Taglio Capelli'")) {

            assertTrue(rs.next(), "Il servizio dovrebbe essere salvato nel database.");
            assertEquals(15.50, rs.getDouble("price"), "Il prezzo del servizio dovrebbe essere 15.50");
        }
    }

    private void write(MFXTextField textFieldName, String text) {
        clickOn(textFieldName).write(text);
    }


    @Test
    public void testAddServiceEmptyFields() {
        // Simula il tentativo di aggiungere un servizio con i campi vuoti
        clickOn("#addNewServiceButton");

        // Verifica che non ci sia nessun servizio nel database
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Service_types")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Non dovrebbe essere salvato nulla se i campi sono vuoti.");
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

        // Aggiungi un nuovo servizio
        write(textFieldName, serviceName);
        write(textFieldPrice, servicePrice);
        clickOn("#addNewServiceButton");

        clickOn("Yes");
        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200); // attesa DB

        // Trova la riga con il servizio "Servizio Test"
        Optional<Node> matchingRow = lookup(".table-row-cell")
                .queryAll()
                .stream()
                .filter(node -> node instanceof TableRow<?> tableRow &&
                        tableRow.getItem() instanceof ServiceType service &&
                        service.getServiceName().equals(serviceName))
                .findFirst();

        // Clicca il pulsante delete corretto
        Node deleteButton = from(matchingRow.get()).lookup("#delete-button").query();
        clickOn(deleteButton);
        clickOn("Yes");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200); // attesa DB

        // ✅ Verifica nel database
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Types WHERE service_name = 'Servizio Test'");
            assertFalse(rs.next(), "Il servizio dovrebbe essere stato eliminato dal database.");
        }
    }


} 
