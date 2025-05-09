package Functional;

import Authentication.SessionManager;
import Services.NewsService;
import PageControllers.NewsCustomerController;
import Model.Customer;
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
class NewsCustomerControllerTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // 1. Customer fittizio loggato
        Customer dummyCustomer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyCustomer);

        // 2. Carica FXML manualmente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsCustomer.fxml"));
        Parent root = loader.load();

        // 3. Ottieni controller
        NewsCustomerController controller = loader.getController();

        // 4. Crea mock del NewsService
        NewsService mockNewsService = Mockito.mock(NewsService.class);
        Mockito.when(mockNewsService.getNews()).thenReturn(List.of()); // ← lista vuota

        // 5. Inietta il mock nel controller
        controller.setNewsService(mockNewsService); // Assicurati che esista questo metodo!

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

    @AfterEach
    public void tearDown() {
        SessionManager.getInstance().resetUser();
    }
}
