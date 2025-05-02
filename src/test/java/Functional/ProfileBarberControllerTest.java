package Functional;

import Authentication.SessionManager;
import Business.ProfileService;
import Controllers.ProfileBarberController;
import Model.Barber;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class ProfileBarberControllerTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // 1. Barber fittizio loggato
        Barber dummyBarber = new Barber("Luca", "Bianchi", "l.bianchi@example.com", "securePass456", "0987654321");
        SessionManager.getInstance().setCurrentUser(dummyBarber);

        // 2. Carica FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileBarber.fxml"));
        Parent root = loader.load();

        // 3. Ottieni controller
        ProfileBarberController controller = loader.getController();

        // 4. Crea mock del ProfileService
        ProfileService mockService = Mockito.mock(ProfileService.class);
        Mockito.when(mockService.getUserData()).thenReturn(Map.of(
                "name", "Luca",
                "surname", "Bianchi",
                "email", "l.bianchi@example.com",
                "phone", "0987654321"
        ));

        // 5. Inietta il mock nel controller
        controller.setProfileService(mockService);

        // 6. Mostra scena
        stage.setScene(new Scene(root));
        stage.setTitle("ProfileBarber");
        stage.show();
    }

    @Test
    public void testLogoutSwitchesToSigninScene() throws Exception {
        clickOn("#logoutIcon");

        long start = System.currentTimeMillis();
        while (!"Signin".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Signin", stage.getTitle(),
                "Dopo il logout, la vista dovrebbe passare alla schermata di login");
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
    public void testGoToSendComunicationView() throws Exception {
        clickOn("#sendComunicationButton");

        long start = System.currentTimeMillis();
        while (!"Send Comunication".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("Send Comunication", stage.getTitle(),
                "La vista dovrebbe passare alla schermata di invio comunicazioni.");
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

    @Test
    public void testGoToNewsView() throws Exception {
        clickOn("#newsButton");

        long start = System.currentTimeMillis();
        while (!"News".equals(stage.getTitle()) &&
                System.currentTimeMillis() - start < 5000) {
            Thread.sleep(100);
        }

        assertEquals("News", stage.getTitle(),
                "La vista dovrebbe passare alla schermata delle news.");
    }

    @AfterEach
    public void tearDown() {
        SessionManager.getInstance().resetUser();
    }
}
