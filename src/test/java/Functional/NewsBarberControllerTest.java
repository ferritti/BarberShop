package Functional;

import Authentication.SessionManager;
import Business.NewsService;
import Controllers.NewsBarberController;
import Model.Barber;
import Model.Notification;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class NewsBarberControllerTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // 1. Barber fittizio loggato
        Barber dummyBarber = new Barber("Giorgio", "Verdi", "g.verdi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyBarber);

        // 2. Carica FXML manualmente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsBarber.fxml"));
        Parent root = loader.load();

        // 3. Ottieni controller
        NewsBarberController controller = loader.getController();

        // 4. Crea mock del NewsService
        NewsService mockNewsService = Mockito.mock(NewsService.class);
        Mockito.when(mockNewsService.getNews()).thenReturn(List.of()); // ← lista vuota

        // 5. Inietta il mock nel controller
        controller.setNewsService(mockNewsService); // ← Assicurati che esista!

        // 6. Mostra scena
        stage.setScene(new Scene(root));
        stage.setTitle("News");
        stage.show();
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
                "La vista dovrebbe passare alla schermata degli appuntamenti.");
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
    public void testGoToSendComunicationView() throws Exception {
        clickOn("#sendComunicationButton");

        long start = System.currentTimeMillis();
        while (!"Send Comunication".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Send Comunication", stage.getTitle(),
                "La vista dovrebbe passare alla schermata di invio comunicazione.");
    }

    @Test
    public void testGoToServicesView() throws Exception {
        clickOn("#servicesButton");

        long start = System.currentTimeMillis();
        while (!"Services".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Services", stage.getTitle(),
                "La vista dovrebbe passare alla schermata dei servizi.");
    }

    @AfterEach
    public void tearDown() {
        SessionManager.getInstance().resetUser();
    }
}
