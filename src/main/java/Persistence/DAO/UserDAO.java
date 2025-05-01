package Persistence.DAO;

import Model.User;
import java.util.HashMap;

public interface UserDAO {
    public boolean addUser(User user);
    public void removeUserByEmail(String email);
    public User findByEmail(String email);
    public boolean checkCredentials(String email, String password);
    public HashMap <String, String> getBarbersData();
}
