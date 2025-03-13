package DBconnection.DAO;

import Model.Notification;
import Model.User;

import java.util.List;

public interface NewsDAO {

    public boolean addNews(Notification notification);
    public boolean removeNews(Notification notification);
    public List<Notification> getAllNews(User.UserType userType);
}
