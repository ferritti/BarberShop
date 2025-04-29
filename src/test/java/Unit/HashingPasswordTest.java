package Unit;

import Security.HashingPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashingPasswordTest {
    @Test
    public void testHashPassword() {
        String password = "mySuperSecurePassword";
        String hashedPassword = HashingPassword.hashPassword(password);

        // Ensure the hash is not null or empty
        assertNotNull(hashedPassword, "The hashed password should not be null");
        assertFalse(hashedPassword.isEmpty(), "The hashed password should not be empty");

        // Ensure the hash is not equal to the plain password
        assertNotEquals(password, hashedPassword, "The hashed password should not be equal to the plain password");
    }

    @Test
    public void testCheckPasswordCorrect() {
        String password = "mySuperSecurePassword";
        String hashedPassword = HashingPassword.hashPassword(password);

        // Ensure the correct password is accepted
        assertTrue(HashingPassword.checkPassword(password, hashedPassword), "The password should be correct");
    }

    @Test
    public void testCheckPasswordIncorrect() {
        String password = "mySuperSecurePassword";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = HashingPassword.hashPassword(password);

        // Ensure an incorrect password is rejected
        assertFalse(HashingPassword.checkPassword(incorrectPassword, hashedPassword), "The password should be incorrect");
    }

    @Test
    public void testDifferentPasswordsProduceDifferentHashes() {
        String password1 = "password123";
        String password2 = "anotherPassword";

        String hash1 = HashingPassword.hashPassword(password1);
        String hash2 = HashingPassword.hashPassword(password2);

        assertNotEquals(hash1, hash2, "Different passwords should produce different hashes");
    }

    @Test
    public void testSamePasswordDifferentHashes() {
        String password = "password123";

        String hash1 = HashingPassword.hashPassword(password);
        String hash2 = HashingPassword.hashPassword(password);

        assertNotEquals(hash1, hash2, "Hashing the same password twice should produce different hashes due to salting");
    }

    @Test
    public void testEmptyPassword() {
        String password = "";
        String hashedPassword = HashingPassword.hashPassword(password);

        assertNotNull(hashedPassword, "Hash of an empty password should not be null");
        assertFalse(hashedPassword.isEmpty(), "Hash of an empty password should not be empty");
    }

    @Test
    public void testNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            HashingPassword.hashPassword(null);
        }, "Hashing a null password should throw an exception");
    }

}