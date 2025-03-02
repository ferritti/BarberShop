package DBconnection.DAO;
import Model.User;

import java.sql.SQLException;

public interface UserDAO {
    public void addUser(User user) throws SQLException;
    public void removeUser(User user) throws SQLException;
    public User findByEmail(String email) throws SQLException;
}
