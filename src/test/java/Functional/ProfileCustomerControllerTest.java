package Functional;

import Authentication.SessionManager;
import Services.ProfileService;
import PageControllers.ProfileCustomerController;
import Model.Customer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileCustomerControllerTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // 1. Customer fittizio loggato
        Customer dummyCustomer = new Customer("Mario", "Rossi", "m.rossi@example.com", "securePass123", "1234567890");
        SessionManager.getInstance().setCurrentUser(dummyCustomer);

        // 2. Carica FXML manualmente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileCustomer.fxml"));
        Parent root = loader.load();

        // 3. Ottieni controller
        ProfileCustomerController controller = loader.getController();

        // 4. Crea mock del ProfileService
        ProfileService mockService = Mockito.mock(ProfileService.class);
        Mockito.when(mockService.getUserData()).thenReturn(Map.of(
                "name", "Mario",
                "surname", "Rossi",
                "email", "m.rossi@example.com",
                "phone", "1234567890"
        ));

        // 5. Inietta il mock nel controller
        controller.setProfileService(mockService);

        // 6. Mostra scena
        stage.setScene(new Scene(root));
        stage.setTitle("ProfileCustomer");
        stage.show();
    }


    @Test
    public void testLogoutSwitchesToSigninScene() throws Exception {
        // Azione: clicca sull'icona del logout
        clickOn("#logoutIcon"); // Simula il clic sull'icona di logout

        // Verifica: controlla che la scena cambi a "Sign In"
        long start = System.currentTimeMillis();
        while (!"Signin".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "Sign In"
        assertEquals("Signin", stage.getTitle(),
                "Dopo il logout, la vista dovrebbe passare alla schermata di login");
    }

    @Test
    public void testGoToAppointmentsView() throws Exception {
        // Azione: clicca sul pulsante per andare agli appuntamenti
        clickOn("#appointmentsButton");

        // Verifica: controlla che la scena cambi a "Appointments"
        long start = System.currentTimeMillis();
        while (!"Appointments".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "Appointments"
        assertEquals("Appointments", stage.getTitle(),
                "La vista dovrebbe passare alla schermata degli appuntamenti.");
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
    public void testGoToNewAppointmentView() throws Exception {
        // Azione: clicca sul pulsante per andare al nuovo appuntamento
        clickOn("#newAppointmentButton");

        // Verifica: controlla che la scena cambi a "Nuovo Appuntamento"
        long start = System.currentTimeMillis();
        while (!"Nuovo Appuntamento".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        // Assert: Verifica che il titolo della scena sia "Nuovo Appuntamento"
        assertEquals("Nuovo Appuntamento", stage.getTitle(),
                "La vista dovrebbe passare alla schermata di nuovo appuntamento.");
    }

    @AfterEach
    public void tearDown() {
        SessionManager.getInstance().resetUser();
    }
}