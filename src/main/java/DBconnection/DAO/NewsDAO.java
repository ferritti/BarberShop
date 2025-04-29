package DBconnection.DAO;

import Model.Notification;

import java.util.List;

public interface NewsDAO {

    public boolean addNotification(Notification notification);
    public List<Notification> getAllBarberNews(String barberEmail);
    public List<Notification> getAllCustomerNews();
    public boolean deleteNotification(Notification notification);
}
