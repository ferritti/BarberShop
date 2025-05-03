package Functional;

import Authentication.SessionManager;
import Controllers.NewAppointmentControllerSlots;
import Model.Appointment;
import Model.AvailableSlot;
import Model.Customer;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.*;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NewAppointmentControllerSlotsTest extends ApplicationTest {

    private NewAppointmentControllerSlots controller;
    private Stage stage;
    private static boolean slotCreated = false;

    @Override
    public void start(Stage stage) throws Exception {
        // Simula l'utente "dummy" che sarà il cliente
        Customer dummyCustomer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyCustomer);

        this.stage = stage;

        // Inizializzazione della GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentSlots.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.setTitle("New Appointment");
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
            DBTestInitializer.createAvailableSlotsTable(stmt);
            DBTestInitializer.createNewsTable(stmt);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Errore durante l'inizializzazione del database H2");
        }

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            // Aggiungo un servizio
            stmt.executeUpdate("INSERT INTO Service_Types (service_name, price) VALUES ('Taglio Capelli', 20.00)");

            // Creo un customer
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Mario', 'Rossi', 'm.rossi@example.com', 'securePass123', '1234567890', 'CUSTOMER')");

            // Creo un barbiere
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Luca', 'Verdi', 'l.verdi@example.com', 'securePass123', '0987654321', 'BARBER')");

            // Aggiungo uno slot disponibile
            stmt.executeUpdate("INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                    "VALUES ('l.verdi@example.com', '2025-05-10', '10:00')");

            // Imposta l'utente loggato (dopo averlo creato nel DB)
            slotCreated = true;

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore durante la preparazione del database");
        }

    }

    @AfterAll
    public static void cleanupDatabaseAfterTest() {
        if (slotCreated) {
            try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
                stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'l.verdi@example.com' AND slot_date = '2025-05-10'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'm.rossi@example.com'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'l.verdi@example.com'");
                stmt.executeUpdate("DELETE FROM Service_Types WHERE service_name = 'Taglio Capelli'");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Errore nella pulizia del database dopo il test");
            }
            slotCreated = false;
        }
        DBManager.getInstance(true).close();
    }

//    @Test
//    public void testBookAppointmentWithPaymentSuccess() throws Exception {
//        // Verifica che non ci siano appuntamenti inizialmente
//
//        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {
//
//            rs.next();
//            int total = rs.getInt("total");
//            assertEquals(0, total, "Il database dovrebbe contenere 0 appuntamenti prima della selezione");
//        }
//
//        // Attende che la UI sia caricata
//        WaitForAsyncUtils.waitForFxEvents();
//        Thread.sleep(500);
//
//
//
//        moveBy(337, -325).clickOn();
//        WaitForAsyncUtils.waitForFxEvents();
//        Thread.sleep(500);
//        // Seleziona "l.verdi@example.com" dalla lista dei barbieri
//        clickOn("Luca Verdi");
//        Button tenAMButton = lookup("#tenAMButton").queryAs(Button.class);
//        tenAMButton.setDisable(false);
//        tenAMButton.setOpacity(1);
//        WaitForAsyncUtils.waitForFxEvents();
//        clickOn("#tenAMButton");
//
//
//
//
//        // Seleziona il servizio dalla ComboBox (in alto a destra)
//        ComboBox<?> serviceBox = lookup("#serviceComboBox").query();
//        Node arrowService = serviceBox.lookup(".arrow-button");
//        clickOn(arrowService);
//        clickOn("Taglio Capelli"); // Cambia con il nome del servizio corretto se diverso
//
//        // Seleziona il pulsante per le 10:00
//
//
//        // Conferma la prenotazione (simulando il popup)
//        clickOn("Yes");
//
//        // Scegli il metodo di pagamento
//        clickOn("PayPal");
//
//        // Conferma finale
//        clickOn("OK");
//
//        // Attendere che gli eventi vengano processati
//        WaitForAsyncUtils.waitForFxEvents();
//        Thread.sleep(500);
//
//        // Verifica che ora ci sia un appuntamento registrato
//        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {
//
//            rs.next();
//            int total = rs.getInt("total");
//            assertEquals(1, total, "Dopo la selezione, dovrebbe esserci un nuovo appuntamento fissato");
//        }
//    }



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

    @Test
    public void testGoToAppointmentsView() throws Exception {
        clickOn("#appointmentsButton");

        long start = System.currentTimeMillis();
        while (!"Appointments".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Appointments", stage.getTitle(),
                "La vista dovrebbe passare alla schermata del Appointments.");
    }

    @Test
    public void testGoToCalendarView() throws Exception {
        clickOn("#backButton");

        long start = System.currentTimeMillis();
        while (!"Calendar".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Calendar", stage.getTitle(),
                "La vista dovrebbe passare alla schermata del Calendar.");
    }
}
