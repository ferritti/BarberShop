package Unit;

import Authentication.SessionManager;
import Business.ProfileService;
import Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private SessionManager sessionManager;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        sessionManager = mock(SessionManager.class);
        profileService = new ProfileService(sessionManager);
    }

    @Test
    void logoutCallsResetUser() {
        profileService.logout();
        verify(sessionManager, times(1)).resetUser();
    }

    @Test
    void getUserDataWithLoggedUser() {
        User user = mock(User.class);
        when(user.getName()).thenReturn("Mario");
        when(user.getSurname()).thenReturn("Rossi");
        when(user.getEmail()).thenReturn("mario.rossi@example.com");
        when(user.getPhone()).thenReturn("123456789");
        when(sessionManager.getCurrentUser()).thenReturn(user);

        Map<String, String> data = profileService.getUserData();

        assertEquals(4, data.size());
        assertEquals("Mario", data.get("name"));
        assertEquals("Rossi", data.get("surname"));
        assertEquals("mario.rossi@example.com", data.get("email"));
        assertEquals("123456789", data.get("phone"));
    }

    @Test
    void getUserDataHandlesNullUser() {
        when(sessionManager.getCurrentUser()).thenReturn(null);

        Map<String, String> data = profileService.getUserData();

        assertTrue(data.isEmpty());
    }
}