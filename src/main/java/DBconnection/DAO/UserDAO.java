package DBconnection.DAO;
import Model.User;

public interface UserDAO {
    public boolean addUser(User user);
    public boolean removeUser(User user);
    public User findByEmail(String email);
}
