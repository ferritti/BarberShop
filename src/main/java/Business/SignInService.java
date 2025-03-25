package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Customer;
import Model.User;

public class SignInService {

    private final UserDAO userDAO;
    private final SessionManager sessionManager;

    // Costruttore con dipendenze
    public SignInService(UserDAO userDAO, SessionManager sessionManager) {
        this.userDAO = userDAO;
        this.sessionManager = sessionManager;
    }

    // Costruttore predefinito per uso con singleton
    public SignInService() {
        this.userDAO = new ConcreteUserDAO();
        this.sessionManager = SessionManager.getInstance();
    }

    // Metodo per autenticare l'utente
    public boolean authenticateUser(String email, String password) {
        try {
            if (userDAO.checkCredentials(email, password)) {
                User user = userDAO.findByEmail(email);
                sessionManager.setCurrentUser(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Logga o gestisci l'errore in modo appropriato
        }
        return false;
    }

    // Verifica se l'utente corrente è un Customer
    public boolean isCustomer() {
        return sessionManager.getCurrentUser() instanceof Customer;
    }

    // Controlla se l'email esiste già
    public boolean checkEmailExists(String email) {
        return userDAO.findByEmail(email) != null;
    }

    // Metodo per il logout
    public void signOut() {
        sessionManager.setCurrentUser(null); // Rimuove l'utente dalla sessione
    }
}
