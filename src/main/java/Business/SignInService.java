package Business;

import Authentication.SessionManager;
import Persistence.DAO.ConcreteUserDAO;
import Persistence.DAO.UserDAO;
import Model.Customer;
import Model.User;

public class SignInService {

    private final UserDAO userDAO;
    private final SessionManager sessionManager;

    public SignInService(UserDAO userDAO, SessionManager sessionManager) {
        this.userDAO = userDAO;
        this.sessionManager = sessionManager;
    }

    public SignInService() {
        this.userDAO = new ConcreteUserDAO();
        this.sessionManager = SessionManager.getInstance();
    }

    public boolean authenticateUser(String email, String password) {
        try {
            if (userDAO.checkCredentials(email, password)) {
                User user = userDAO.findByEmail(email);
                sessionManager.setCurrentUser(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCustomer() {
        return sessionManager.getCurrentUser() instanceof Customer;
    }

    public boolean checkEmailExists(String email) {
        return userDAO.findByEmail(email) != null;
    }
}
