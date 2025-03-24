package Unit;

import Business.SignupController;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SignupControllerTest {

    // Annota il controller con @InjectMocks per iniettare automaticamente i mock
    @InjectMocks
    private SignupController controller;

    // Annota la dipendenza UserDAO con @Mock per creare un mock di UserDAO
    @Mock
    private UserDAO mockUserDao;

    // Metodo di setup, necessario per inizializzare i mock e iniettarli
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inizializza i mock
    }

    @Test
    void testCheckBarberCode_ValidCode() {
        // Test per il metodo checkBarberCode
        assertTrue(controller.checkBarberCode("I-AM-A-BARBER"));
    }

    @Test
    void testCheckBarberCode_InvalidCode() {
        // Test per il metodo checkBarberCode
        assertFalse(controller.checkBarberCode("INVALID-CODE"));
    }

    @Test
    void testSignupAction_ValidCustomer() {
        // Test per il metodo signupAction quando si inserisce un cliente
        controller.nameField.setText("John");
        controller.surnameField.setText("Doe");
        controller.emailField.setText("john.doe@example.com");
        controller.passwordField.setText("password");
        controller.phoneField.setText("123456789");
        controller.secretCodeField.setText("");  // Nessun codice barbiere

        // Esegui l'azione di signup
        controller.signupAction(null);

        // Verifica che userDao.addUser sia stato chiamato con un oggetto Customer
        verify(mockUserDao).addUser(any(Customer.class));
    }

    @Test
    void testSignupAction_ValidBarber() {
        // Test per il metodo signupAction quando si inserisce un barbiere
        controller.nameField.setText("Jane");
        controller.surnameField.setText("Doe");
        controller.emailField.setText("jane.doe@example.com");
        controller.passwordField.setText("password");
        controller.phoneField.setText("987654321");
        controller.secretCodeField.setText("I-AM-A-BARBER");  // Codice barbiere

        // Esegui l'azione di signup
        controller.signupAction(null);

        // Verifica che userDao.addUser sia stato chiamato con un oggetto Barber
        verify(mockUserDao).addUser(any(Barber.class));
    }

    @Test
    void testSignupAction_EmptyFields() {
        // Test per il metodo signupAction quando i campi sono vuoti
        controller.nameField.setText("");
        controller.surnameField.setText("");
        controller.emailField.setText("");
        controller.passwordField.setText("");
        controller.phoneField.setText("");

        // Esegui l'azione di signup
        controller.signupAction(null);

        // Verifica che l'alert per campi vuoti sia visibile
        assertTrue(controller.notEmptyAlert.isVisible());
    }

    @Test
    void testSignupAction_InvalidBarberCode() {
        // Test per il metodo signupAction con un codice barbiere non valido
        controller.nameField.setText("Mark");
        controller.surnameField.setText("Smith");
        controller.emailField.setText("mark.smith@example.com");
        controller.passwordField.setText("password");
        controller.phoneField.setText("5551234567");
        controller.secretCodeField.setText("INVALID-CODE");  // Codice errato

        // Esegui l'azione di signup
        controller.signupAction(null);

        // Verifica che il messaggio di errore per il codice segreto sia visibile
        assertTrue(controller.secretCodeAlert.isVisible());
    }
}
