package Unit;

import Authentication.HashingPassword;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashingPasswordTest {

    @Test
    void testHashPassword() {
        // Testa se la password viene effettivamente "hashata" e non ritorna la stessa stringa
        String password = "myPassword123";

        String hashedPassword = HashingPassword.hashPassword(password);

        // Verifica che l'hash non sia vuoto
        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());

        // Verifica che la password in chiaro non corrisponda all'hash
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void testCheckPasswordWithCorrectPassword() {
        // Testa se la password e l'hash corrispondono
        String password = "myPassword123";
        String hashedPassword = HashingPassword.hashPassword(password);

        boolean result = HashingPassword.checkPassword(password, hashedPassword);

        // Verifica che la password in chiaro corrisponda all'hash
        assertTrue(result);
    }

    @Test
    void testCheckPasswordWithIncorrectPassword() {
        // Testa se la password e l'hash non corrispondono
        String password = "myPassword123";
        String hashedPassword = HashingPassword.hashPassword(password);

        String wrongPassword = "wrongPassword";

        boolean result = HashingPassword.checkPassword(wrongPassword, hashedPassword);

        // Verifica che la password sbagliata non corrisponda all'hash
        assertFalse(result);
    }

    @Test
    void testCheckPasswordWithSamePasswordMultipleTimes() {
        // Verifica che l'operazione "hash + check" resti coerente anche per più test
        String password = "samePassword123";
        String hashedPassword = HashingPassword.hashPassword(password);

        // Esegui il test più volte
        for (int i = 0; i < 5; i++) {
            boolean result = HashingPassword.checkPassword(password, hashedPassword);
            assertTrue(result, "Password non corrisponde all'hash (tentativo " + (i+1) + ")");
        }
    }

    @Test
    void testHashPasswordGeneratesDifferentHashes() {
        // Verifica che per la stessa password vengano generati hash diversi
        String password = "myPassword123";
        String hashedPassword1 = HashingPassword.hashPassword(password);
        String hashedPassword2 = HashingPassword.hashPassword(password);

        // Verifica che gli hash generati siano diversi (perchè i sali sono diversi)
        assertNotEquals(hashedPassword1, hashedPassword2);
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
