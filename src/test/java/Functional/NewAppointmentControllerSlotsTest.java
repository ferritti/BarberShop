package Functional;

import Authentication.SessionManager;
import PageControllers.NewAppointmentControllerSlots;
import Model.Customer;
import Persistence.DBConnection.DBManager;
import Persistence.DBConnection.DBTestInitializer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import java.sql.*;
import java.time.LocalDate;
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


        stage.setScene(new Scene(root));
        stage.setTitle("New Appointment");
        stage.show();

        controller = loader.getController();

        controller.setDate(LocalDate.of(2025, 5, 13));



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
            DBTestInitializer.createAvailableSlotsTable(stmt);
            DBTestInitializer.createAppointmentsTable(stmt);
            DBTestInitializer.createAppointmentServicesTable(stmt);
            DBTestInitializer.createNewsTable(stmt);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Errore durante l'inizializzazione del database H2");
        }

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
            stmt.executeUpdate("DELETE FROM Available_Slots ");
            stmt.executeUpdate("DELETE FROM Service_Types WHERE service_name = 'Taglio Capelli'");
            // Aggiungo un servizio
            stmt.executeUpdate("INSERT INTO Service_Types (service_name, price) VALUES ('Taglio Capelli', 20.00)");

            stmt.executeUpdate("DELETE FROM Service_Types WHERE service_name = 'Messaggio Cute'");
            // Aggiungo un servizio
            stmt.executeUpdate("INSERT INTO Service_Types (service_name, price) VALUES ('Messaggio Cute', 20.00)");

            stmt.executeUpdate("DELETE FROM Users");

            // Creo un customer
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Mario', 'Rossi', 'm.rossi@example.com', 'securePass123', '1234567890', 'CUSTOMER')");

            // Creo un barbiere
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Luca', 'Verdi', 'l.verdi@example.com', 'securePass123', '0987654321', 'BARBER')");
            // Creo un barbiere
            stmt.executeUpdate("INSERT INTO Users (name, surname, email, pass_hash, phone, role) " +
                    "VALUES ('Gianni', 'Nero', 'g.nero@example.com', 'securePass123', '1234509876', 'BARBER')");

            // Rimuovi lo slot se già esiste
            stmt.executeUpdate("DELETE FROM Available_Slots ");
            stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'l.verdi@example.com' AND slot_date = '2025-05-13' AND start_time = '10:00'");

            // Aggiungo uno slot disponibile
            stmt.executeUpdate("INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                    "VALUES ('l.verdi@example.com', '2025-05-13', '10:00')");

            stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'g.nero@example.com' AND slot_date = '2025-05-13' AND start_time = '10:00'");

            // Aggiungo uno slot disponibile
            stmt.executeUpdate("INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                    "VALUES ('g.nero@example.com', '2025-05-13', '10:00')");

            stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'g.nero@example.com' AND slot_date = '2025-05-13' AND start_time = '12:00'");

            // Aggiungo uno slot disponibile
            stmt.executeUpdate("INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                    "VALUES ('g.nero@example.com', '2025-05-13', '12:00')");

            // Rimuovi lo slot se già esiste
            stmt.executeUpdate("DELETE FROM Available_Slots WHERE barber_email = 'l.verdi@example.com' AND slot_date = '2025-05-13' AND start_time = '8:00'");

            // Aggiungo uno slot disponibile
            stmt.executeUpdate("INSERT INTO Available_Slots (barber_email, slot_date, start_time) " +
                    "VALUES ('l.verdi@example.com', '2025-05-13', '8:00')");

            stmt.executeUpdate("DELETE FROM Appointments WHERE barber_email = 'l.verdi@example.com'");
            stmt.executeUpdate("DELETE FROM News WHERE barber_email = 'l.verdi@example.com'");
            slotCreated = true;


            // Imposta l'utente loggato (dopo averlo creato nel DB)


        } catch (SQLException e) {
            e.printStackTrace();
            fail("Errore durante la preparazione del database");
        }

    }

    @AfterAll
    public static void cleanupDatabaseAfterTest() {
        if (slotCreated) {
            try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement()) {
                stmt.executeUpdate("DELETE FROM Available_Slots ");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'm.rossi@example.com'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'l.verdi@example.com'");
                stmt.executeUpdate("DELETE FROM Users WHERE email = 'g.nero@example.com'");
                stmt.executeUpdate("DELETE FROM Service_Types");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("Errore nella pulizia del database dopo il test");
            }
            slotCreated = false;
        }
        DBManager.getInstance(true).close();
    }

    @Test
    public void testBookAppointmentFailsWithoutServiceSelection() throws Exception {
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total);
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        Button tenAMButton = lookup("#tenAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#tenAMButton").clickOn("#tenAMButton");
        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Non dovrebbe essere stato fissato alcun appuntamento senza selezione del servizio");
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total);
        }
    }


    @Test
    public void testBookAppointmentCancelledWithNoButton() throws Exception {

        // Verifica che inizialmente non ci siano appuntamenti
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "All'inizio ci dovrebbero essere zero appuntamenti");
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Seleziona il barbiere
        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        // Seleziona il servizio
        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        clickOn("No");

        // Attiva e clicca sul bottone delle 10:00
        Button tenAMButton = lookup("#eightAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#eightAMButton").clickOn("#eightAMButton");


        Thread.sleep(500);
        clickOn("No");

        // Attendi gli eventi
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Verifica che l'appuntamento NON sia stato salvato
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Premendo 'No', l'appuntamento non dovrebbe essere salvato");
        }

        // Verifica che nessuna news sia stata creata
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Premendo 'No', non dovrebbe essere creata alcuna nuova news");
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

    @Test
    public void testBookAppointmentCancelledAfterYesWithBackButton() throws Exception {

        // Controlla che il database sia vuoto
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "All'avvio, non ci dovrebbero essere appuntamenti");
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Selezione barbiere
        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        // Selezione servizio
        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        Thread.sleep(500);
        clickOn("No");

        // Attiva il bottone delle 10:00 e clicca
        Button tenAMButton = lookup("#tenAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#tenAMButton").clickOn("#tenAMButton");

        // Conferma con "Yes"
        clickOn("Yes");

        // Quando compare la finestra dei metodi di pagamento, clicca su "Back"
        clickOn("Back");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Verifica che nessun appuntamento sia stato salvato
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Dopo 'Yes' seguito da 'Back', l'appuntamento non dovrebbe essere salvato");
        }

        // Verifica che nessuna notizia sia stata creata
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Dopo 'Yes' seguito da 'Back', non dovrebbe essere creata alcuna news");
        }
    }


    @Test
    public void testBookAppointmentWithOneServiceSuccess() throws Exception {
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total);
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        Thread.sleep(500);
        clickOn("No");

        Button tenAMButton = lookup("#tenAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#tenAMButton").clickOn("#tenAMButton");

        clickOn("Yes");
        clickOn("PayPal");
        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Available_Slots WHERE barber_email = 'l.verdi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News ")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }
    }

    @Test
    public void testBookAppointmentWithTwoServicesSuccess() throws Exception {

        // Verifica che non ci siano appuntamenti inizialmente
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Il database dovrebbe contenere 0 appuntamenti prima della selezione");
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Seleziona il barbiere
        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        // Seleziona il servizio
        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        Node caretES = lookup("#extraServiceComboBox").lookup(".caret").query();
        clickOn(caretES);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Messaggio Cute");

        clickOn("Yes");
        clickOn("OK");

        // Attiva e clicca sul bottone delle 10:00
        Button tenAMButton = lookup("#eightAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#eightAMButton").clickOn("#eightAMButton");


        // Conferma la prenotazione (popup)
        clickOn("Yes");


        // Seleziona metodo di pagamento
        clickOn("Pay at Shop");

        // Conferma finale
        clickOn("OK");

        // Attendi gli eventi
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Available_Slots WHERE barber_email = 'l.verdi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total, "Dopo la selezione, dovrebbe esserci uno slot libero");
        }

        // Verifica che l'appuntamento sia stato registrato nel database
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total, "Dopo la selezione, dovrebbe esserci un nuovo appuntamento fissato");
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News ")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total, "Dopo la selezione, dovrebbe esserci un nuovo news");
        }
    }

    @Test
    public void testBookAppointmentWithoutSecondServiceShowsWarning() throws Exception {
        // Verifica che non ci siano appuntamenti inizialmente
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Il database dovrebbe contenere 0 appuntamenti prima della selezione");
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Seleziona il barbiere
        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Luca Verdi");

        // Seleziona il primo servizio (ma NON il secondo)
        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        // Simula il clic su “Yes” (vuole selezionare un secondo servizio)
        clickOn("Yes");

        // Clicca subito su “OK” senza selezionare il secondo servizio
        clickOn("OK");

        // Attesa per eventuali Alert
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        // Verifica che NON sia stato creato nessun appuntamento
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(0, total, "Non dovrebbe essere stato creato alcun appuntamento");
        }
    }

    @Test
    public void testBookAppointmentWithSameTimeDateFailure() throws Exception {
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Gianni Nero");

        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        Thread.sleep(500);
        clickOn("No");

        Button tenAMButton = lookup("#tenAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#tenAMButton").clickOn("#tenAMButton");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Available_Slots WHERE barber_email = 'g.nero@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News ")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }
    }

    @Test
    public void testBookAppointmentWithPaymentCreditCardSuccess() throws Exception {
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(2, total);
        }

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        Node caretB = lookup("#barberComboBox").lookup(".caret").query();
        clickOn(caretB);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Gianni Nero");

        Node caretS = lookup("#serviceComboBox").lookup(".caret").query();
        clickOn(caretS);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        clickOn("Taglio Capelli");

        Thread.sleep(500);
        clickOn("No");

        Button tenAMButton = lookup("#twelveAMButton").queryAs(Button.class);
        tenAMButton.setDisable(false);
        tenAMButton.setOpacity(1);
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);
        moveTo("#twelveAMButton").clickOn("#twelveAMButton");

        clickOn("Yes");
        clickOn("Credit Card");
        clickOn("OK");

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);

        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Available_Slots WHERE barber_email = 'g.nero@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(1, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Appointments WHERE customer_email = 'm.rossi@example.com'")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(3, total);
        }
        try (Statement stmt = DBManager.getInstance(true).getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM News ")) {

            rs.next();
            int total = rs.getInt("total");
            assertEquals(3, total);
        }
    }

}
