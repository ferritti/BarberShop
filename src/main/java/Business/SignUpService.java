package Business;

import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.UserDAO;
import Model.Barber;
import Model.Customer;
import Model.User;

public class SignUpService {
    private UserDAO userDAO;
    private static final String BARBER_CODE = "I-AM-A-BARBER";

    public SignUpService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public SignUpService() {
        this.userDAO = new ConcreteUserDAO();
    }

    public String registerUser(String name, String surname, String email, String password, String phone, String code) {
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            return "notEmptyAlert"; // Messaggio per il controller
        }

        User newUser;
        if (!code.isEmpty() && BARBER_CODE.equals(code)) {
            newUser = new Barber(name, surname, email, password, phone);
        } else if (code.isEmpty()) {
            newUser = new Customer(name, surname, email, password, phone);
        } else {
            return "secretCodeAlert";
        }

        userDAO.addUser(newUser);
        return "success";
    }
}
