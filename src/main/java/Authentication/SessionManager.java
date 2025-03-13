package Authentication;

import Model.Notification;
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
            currentUser = user;
    }

    public User getCurrentUser() {
            return currentUser;
    }

    public void closeSession() {
        instance = null;
    }

}
