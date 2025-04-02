package Unit;

import Authentication.HashingPassword;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HashingPasswordTest {

    @Test
    void testHashPassword() {

    }

    @Test
    void testCheckPasswordWithCorrectPassword() {

    }

    @Test
    void testCheckPasswordWithIncorrectPassword() {

    }

    @Test
    void testCheckPasswordWithSamePasswordMultipleTimes() {

    }

    @Test
    void testHashPasswordGeneratesDifferentHashes() {

    }


    @Test
    void testCheckPasswordWithNullHashedPassword() {
        String password = "myPassword123";

        assertThrows(NullPointerException.class, () -> {
            HashingPassword.checkPassword(password, null);
        }, "Checking with a null hashed password should throw NullPointerException");
    }
}