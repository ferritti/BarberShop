package Authentication;

public class SessionManager {
    private static SessionManager instance;
    private static String currentUserEmail;

    private SessionManager(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public static SessionManager getInstance(String currentUserEmail) {
        if (instance == null) {
            instance = new SessionManager(currentUserEmail);
        }
        return instance;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void closeSession() {
        instance = null;
    }

}

