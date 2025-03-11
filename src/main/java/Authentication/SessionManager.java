package Authentication;

public class SessionManager {
    private static SessionManager instance;
    private String currentUserEmail;

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUserEmail(String userEmail) {
        this.currentUserEmail = userEmail;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void closeSession() {
        currentUserEmail = null;
    }

}

