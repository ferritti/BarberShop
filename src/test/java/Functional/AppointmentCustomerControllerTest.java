package Functional;

import Authentication.SessionManager;
import PageControllers.AppointmentCustomerController;
import Model.Appointment;
import Model.Customer;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentCustomerControllerTest extends ApplicationTest {

    private AppointmentCustomerController controller;
    private Stage stage;
    private static boolean appointmentCreated = false;

    @Override
    public void start(Stage stage) throws Exception {
        Customer dummyCustomer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyCustomer);

        this.stage = stage;

        // Inizializzazione della GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.setTitle("Appointments");
        stage.show();

        // Attendi che JavaFX completi il rendering
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);  // per sicurezza (solo nei test)
    }

    @BeforeAll
    public static void initializeDatabaseForTests() {
        DBManager manager = DBManager.getInstance(true);
        try (Statement stmt = manager.getConnection().createStatement()) {
            DBTestInitializer.createUsersTable(stmt);
            DBTestInitializer.createServiceTypesTable(stmt);
            DBTestInitializer.createAppointmentsTable(stmt);
            DBTestInitializer.createAppointmentServicesTable(stmt);
            DBTestInitializer.createNewsTable(stmt);
            DBTestInitializer.createAvailableSlotsTable(stmt);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Errore durante l'inizializzazione del database H2");
        }

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {

            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dateStr = tomorrow.toString();

            stmt.executeUpdate("DELETE FROM Service_Types WHERE service_name = 'Taglio Capelli'");
            // Aggiungo un servizio
            stmt.executeUpdate("INSERT INTO Service_Types (service_name, price) VALUES ('Taglio Capelli', 20.00)");

            // Elimina utenti esistenti
            stmt.executeUpdate("DELETE FROM Users");
            // Creo un customer
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Mario', 'Rossi', 'm.rossi@example.com', 'securePass123', '1234567890', 'CUSTOMER')");

            // Creo un barbiere
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Luca', 'Verdi', 'l.verdi@example.com', 'securePass123', '0987654321', 'BARBER')");

            // Elimina appuntamenti esistenti
            stmt.executeUpdate("DELETE FROM Appointments");

            // Crea due appuntamenti
            stmt.executeUpdate("INSERT INTO Appointments (app_date, app_time, customer_email, barber_email, payment) " +
                    "VALUES ('" + dateStr + "', '10:00', 'm.rossi@example.com', 'l.verdi@example.com', 'CREDIT_CARD')");
            stmt.executeUpdate("INSERT INTO Appointments (app_date, app_time, customer_email, barber_email, payment) " +
                    "VALUES ('" + dateStr + "', '11:00', 'm.rossi@example.com', 'l.verdi@example.com', 'PAYPAL')");

            // Associa i servizi agli appuntamenti — prima aggiungo il servizio mancante
            stmt.executeUpdate("INSERT INTO Service_Types (service_name, price) VALUES ('Taglio Barba', 15.00)");

            stmt.executeUpdate("INSERT INTO Appointment_Services (app_date, app_time, barber_email, service_name) " +
                    "VALUES ('" + dateStr + "', '11:00', 'l.verdi@example.com', 'Taglio Capelli')");
            stmt.executeUpdate("INSERT INTO Appointment_Services (app_date, app_time, barber_email, service_name) " +
                    "VALUES ('" + dateStr + "', '11:00', 'l.verdi@example.com', 'Taglio Barba')");

            // Elimina gli slot disponibili
            stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'l.verdi@example.com'");

            appointmentCreated = true;

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore durante la preparazione del database");
        }
    }


    @AfterAll
    public static void cleanupDatabaseAfterTest() {
        if (appointmentCreated) {
            try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
                stmt.executeUpdate("DELETE FROM Appointments WHERE customer_email = 'm.rossi@example.com'");
                stmt.executeUpdate("DELETE FROM Appointment_Services WHERE barber_email = 'l.verdi@example.com'");
                stmt.executeUpdate("DELETE FROM Service_Types WHERE service_name = 'Taglio Capelli'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'm.rossi@example.com'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'l.verdi@example.com'");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Errore nella pulizia del database dopo il test");
            }
            appointmentCreated = false;
        }
        DBManager.getInstance(true).close();
    }

    @Test
    public void testDeleteAppointmentSuccess() throws Exception {
        // Verifica che ci siano due appuntamenti nel database
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total, "Il database dovrebbe contenere 2 appuntamenti");
        }

        // Attendi il caricamento degli appuntamenti nella tabella
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);  // Solo per debug temporaneo


        // Trova la riga con appuntamento alle 11:00
        Optional<Node> matchingRow = lookup(".table-row-cell")
                .queryAll()
                .stream()
                .filter(node -> node instanceof TableRow<?> tableRow &&
                        tableRow.getItem() instanceof Appointment appointment &&
                        appointment.getTime().equals(LocalTime.of(11, 0)))
                .findFirst();

        assertTrue(matchingRow.isPresent(), "La riga con l'appuntamento alle 11:00 deve essere presente");

        Node deleteButton = from(matchingRow.get()).lookup("#delete-button").query();
        clickOn(deleteButton);
        clickOn("Yes");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(200); // per sicurezza

        // Verifica che l'appuntamento sia stato eliminato
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total, "Dopo l'eliminazione, dovrebbe rimanere un solo appuntamento");
        }
    }

    @Test
    public void testGoToProfileView() throws Exception {
        clickOn("#profileButton");

        long start = System.currentTimeMillis();
        while (!"Profile".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Profile", stage.getTitle(),
                "La vista dovrebbe passare alla schermata del profilo.");
    }

    @Test
    public void testGoToNewAppointmentView() throws Exception {

        clickOn("#newAppointmentButton");

        long start = System.currentTimeMillis();
        while (!"New Appointment".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("New Appointment", stage.getTitle(),
                "La vista dovrebbe passare alla schermata per creare un nuovo appuntamento.");
    }

    @Test
    public void testGoToNewsView() throws Exception {
        // Azione: clicca sul pulsante per andare alle news
        clickOn("#newsButton");

        // Verifica: controlla che la scena cambi a "News"
        long start = System.currentTimeMillis();
        while (!"News".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "News"
        assertEquals("News", stage.getTitle(),
                "La vista dovrebbe passare alla schermata delle news.");
    }
}