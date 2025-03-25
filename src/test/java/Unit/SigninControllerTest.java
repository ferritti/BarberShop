package Unit;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import Business.SigninController;
import DBconnection.DAO.UserDAO;
import Model.Customer;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import Authentication.SessionManager;

import java.awt.*;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class SigninControllerTest {

    @InjectMocks
    private SigninController signinController;

    @Mock
    private UserDAO userDAO;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private MFXTextField emailField;

    @Mock
    private MFXPasswordField passwordField;

    @Mock
    private Label incorrectLabel;

    @BeforeEach
    void setUp() throws Exception {
        // Inizializzare JavaFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> {
                try {
                    // Inizializza il test
                    MockitoAnnotations.openMocks(this);
                    signinController = new SigninController();
                    setPrivateField(signinController, "emailField", emailField);
                    setPrivateField(signinController, "passwordField", passwordField);
                    setPrivateField(signinController, "incorrectLabel", incorrectLabel);
                    setPrivateField(signinController, "userDAO", userDAO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testSigninAction_ValidCredentials() {
        String email = "test@example.com";
        String password = "password123";
        Customer mockCustomer = new Customer("Mario", "Rossi", email, password, "1234567890");

        // Simula il comportamento dei campi di testo e delle chiamate al database
        when(emailField.getText()).thenReturn(email);
        when(passwordField.getText()).thenReturn(password);
        when(userDAO.checkCredentials(email, password)).thenReturn(true);
        when(userDAO.findByEmail(email)).thenReturn(mockCustomer);

        // Esegui l'azione di login
        ActionEvent actionEvent = mock(ActionEvent.class);
        signinController.signinAction(actionEvent);

        // Verifica le interazioni con il mock
        verify(sessionManager).setCurrentUser(mockCustomer);
        verify(incorrectLabel, never()).setVisible(true);
    }

    // Classe JavaFX di test per avviare la piattaforma
    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // Crea una scena minima per inizializzare la piattaforma JavaFX
            primaryStage.setScene(new Scene(new StackPane(), 300, 250));
            primaryStage.show();
        }
    }
}









//    @Test
//    void testSigninAction_InvalidCredentials() {
//        // Imposta il comportamento mockato per credenziali errate
//        String email = "wrong@example.com";
//        String password = "wrongpassword";
//
//        // Simula il comportamento del campo email e password
//        when(emailField.getText()).thenReturn(email);
//        when(passwordField.getText()).thenReturn(password);
//
//        // Simula il fallimento della verifica delle credenziali
//        when(userDAO.checkCredentials(email, password)).thenReturn(false);
//
//        // Crea un oggetto Customer (anche se non verrà utilizzato, per completezza)
//        Customer mockCustomer = new Customer("Mario", "Rossi", email, password, "1234567890");
//        when(userDAO.findByEmail(email)).thenReturn(mockCustomer);
//
//        // Esegui l'azione
//        ActionEvent actionEvent = mock(ActionEvent.class);
//        signinController.signinAction(actionEvent);
//
//        // Verifica che l'etichetta di errore sia visibile
//        verify(incorrectLabel).setVisible(true);
//
//        // Verifica che la sessione non venga modificata (nessun login avvenuto)
//        verify(sessionManager, never()).setCurrentUser(mockCustomer);
//
//    }
//
//    @Test
//    void testForgotPassPopUp_EmailNotRegistered() {
//        // Impostare il comportamento per il dialogo della password
//        when(userDAO.findByEmail("not_registered@example.com")).thenReturn(null);
//
//        // Esegui il recupero della password
//        signinController.forgotPassPopUp();
//
//        // Verifica che l'alert di errore venga mostrato
//        // Potresti mockare l'alert per verificare la corretta interazione
//    }

//}
