package Functional;

import Authentication.SessionManager;
import Controllers.AppointmentBarberController;
import Model.Barber;
import Model.Customer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentBarberControllerTest extends ApplicationTest {

    private AppointmentBarberController controller;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Barber dummyBarber = new Barber("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyBarber);

        // Inizializzazione della GUI senza interazione con il database
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        stage.setScene(new Scene(root));
        stage.setTitle("Appointments Barber");
        stage.show();

        // Attendi che JavaFX completi il rendering
        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500);  // per sicurezza (solo nei test)
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
    public void testGoToServiceView() throws Exception {
        // Azione: clicca sul pulsante per andare ai servizi
        clickOn("#serviceButton");

        // Verifica: controlla che la scena cambi a "Services"
        long start = System.currentTimeMillis();
        while (!"Service".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "Service"
        assertEquals("Service", stage.getTitle(),
                "La vista dovrebbe passare alla schermata dei servizi.");
    }

    @Test
    public void testGoToSendComunicationView() throws Exception {
        // Azione: clicca sul pulsante per andare alla comunicazione
        clickOn("#sendComunicationButton");

        // Verifica: controlla che la scena cambi a "Send Comunication"
        long start = System.currentTimeMillis();
        while (!"Send Comunication".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "Send Comunication"
        assertEquals("Send Comunication", stage.getTitle(),
                "La vista dovrebbe passare alla schermata di invio comunicazione.");
    }
}
