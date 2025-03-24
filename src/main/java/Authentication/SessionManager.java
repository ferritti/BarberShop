package Authentication;

import Model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.currentUser = user;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("User not logged in");
        }
        return currentUser;
    }

    public void resetUser() {
        currentUser = null;
    }

    public void closeSession() {
        instance = null;
    }
}
