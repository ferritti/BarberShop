package Unit;

import Persistence.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import Services.SignUpService;

class SignUpServiceTest {

    private UserDAO userDAO;
    private SignUpService signUpService;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        signUpService = new SignUpService(userDAO);
    }

    @Test
    void registerCustomerSuccess() {
        when(userDAO.addUser(any(Customer.class))).thenReturn(true);
        String result = signUpService.registerUser("Mario", "Rossi", "mario@example.com", "password123", "1234567890", "");
        assertEquals("success", result);
        verify(userDAO, times(1)).addUser(any(Customer.class));
    }

    @Test
    void registerBarberSuccess() {
        when(userDAO.addUser(any(Barber.class))).thenReturn(true);
        String result = signUpService.registerUser("Luigi", "Bianchi", "luigi@example.com", "securePass", "0987654321", "I-AM-A-BARBER");
        assertEquals("success", result);
        verify(userDAO, times(1)).addUser(any(Barber.class));
    }

    @Test
    void registerFailEmptyFields() {
        String result = signUpService.registerUser("", "Rossi", "mario@example.com", "password123", "1234567890", "");
        assertEquals("notEmptyAlert", result);
        verify(userDAO, never()).addUser(any());
    }

    @Test
    void registerFailInvalidBarberCode() {
        String result = signUpService.registerUser("Luigi", "Bianchi", "luigi@example.com", "securePass", "0987654321", "WRONG-CODE");
        assertEquals("secretCodeAlert", result);
        verify(userDAO, never()).addUser(any());
    }
}
