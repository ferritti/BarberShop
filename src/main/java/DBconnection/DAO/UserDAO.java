package DBconnection.DAO;
import Model.User;

import java.sql.SQLException;

public interface UserDAO {
    public boolean addUser(User user) throws SQLException;
    public boolean removeUser(User user) throws SQLException;
    public User findByEmail(String email) throws SQLException;
}
