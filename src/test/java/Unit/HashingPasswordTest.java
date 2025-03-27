package Unit;

import Authentication.HashingPassword;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HashingPasswordTest {

    @Test
    void testHashPassword() {
        // Mocking del metodo statico BCrypt.gensalt() e BCrypt.hashpw()
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            // Quando chiamato, restituisce un valore fisso per il sale
            mockedBCrypt.when(BCrypt::gensalt).thenReturn("mockedSalt");

            // Quando viene chiamato hashpw con qualsiasi password e il sale mockato, restituisci un valore mockato per l'hash
            mockedBCrypt.when(() -> BCrypt.hashpw(anyString(), eq("mockedSalt"))).thenReturn("mockedHash");

            // Testa il comportamento del metodo hashPassword
            String password = "myPassword123";
            String hashedPassword = HashingPassword.hashPassword(password);

            // Verifica che l'hash restituito sia quello mockato
            assertEquals("mockedHash", hashedPassword);
        }
    }

    @Test
    void testCheckPasswordWithCorrectPassword() {
        // Mocking del metodo statico BCrypt.checkpw()
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            String password = "myPassword123";
            String hashedPassword = "mockedHash";

            // Quando chiamato checkpw, restituisci true (password corretta)
            mockedBCrypt.when(() -> BCrypt.checkpw(password, hashedPassword)).thenReturn(true);

            // Testa il comportamento del metodo checkPassword con la password corretta
            boolean result = HashingPassword.checkPassword(password, hashedPassword);

            // Verifica che il risultato sia true
            assertTrue(result);
        }
    }

    @Test
    void testCheckPasswordWithIncorrectPassword() {
        // Mocking del metodo statico BCrypt.checkpw()
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            String password = "myPassword123";
            String hashedPassword = "mockedHash";
            String wrongPassword = "wrongPassword";

            // Quando chiamato checkpw con una password sbagliata, restituisci false
            mockedBCrypt.when(() -> BCrypt.checkpw(wrongPassword, hashedPassword)).thenReturn(false);

            // Testa il comportamento del metodo checkPassword con la password errata
            boolean result = HashingPassword.checkPassword(wrongPassword, hashedPassword);

            // Verifica che il risultato sia false
            assertFalse(result);
        }
    }

    @Test
    void testCheckPasswordWithSamePasswordMultipleTimes() {
        // Verifica che l'operazione "hash + check" resti coerente anche per più test
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            String password = "samePassword123";
            String hashedPassword = "mockedHash";

            // Mocka la chiamata a checkpw per restituire sempre true
            mockedBCrypt.when(() -> BCrypt.checkpw(password, hashedPassword)).thenReturn(true);

            // Esegui il test più volte
            for (int i = 0; i < 5; i++) {
                boolean result = HashingPassword.checkPassword(password, hashedPassword);
                assertTrue(result, "Password non corrisponde all'hash (tentativo " + (i + 1) + ")");
            }
        }
    }

    @Test
    void testHashPasswordGeneratesDifferentHashes() {
        // Verifica che per la stessa password vengano generati hash diversi
        try (MockedStatic<BCrypt> mockedBCrypt = mockStatic(BCrypt.class)) {
            String password = "myPassword123";
            String firstHashedPassword = "mockedHash1";
            String secondHashedPassword = "mockedHash2";

            // Mocka hashpw per restituire valori diversi a seconda della chiamata
            mockedBCrypt.when(() -> BCrypt.hashpw(password, "mockedSalt"))
                    .thenReturn(firstHashedPassword)
                    .thenReturn(secondHashedPassword);

            // Testa il comportamento di hashPassword
            String hashedPassword1 = HashingPassword.hashPassword(password);
            String hashedPassword2 = HashingPassword.hashPassword(password);

            // Verifica che gli hash generati non siano nulli
            assertNotNull(hashedPassword1, "Il primo hash non dovrebbe essere nullo");
            assertNotNull(hashedPassword2, "Il secondo hash non dovrebbe essere nullo");

            // Verifica che gli hash generati siano diversi
            assertNotEquals(hashedPassword1, hashedPassword2, "Gli hash non dovrebbero essere uguali");
        }
    }


    @Test
    void testCheckPasswordWithNullHashedPassword() {
        // Testa l'edge case con hashedPassword null
        String password = "myPassword123";

        assertThrows(NullPointerException.class, () -> {
            HashingPassword.checkPassword(password, null);
        }, "Checking with a null hashed password should throw NullPointerException");
    }
}
