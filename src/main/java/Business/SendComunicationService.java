package Business;

import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.NewsDAO;
import Model.Notification;

public class SendComunicationService {
    private final NewsDAO newsDAO;

    public SendComunicationService(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public SendComunicationService() {
        this.newsDAO = new ConcreteNewsDAO();
    }

    public boolean areEmptyFields(String title, String message) {
        return title.isEmpty() || message.isEmpty();
    }

    public boolean addComunication(String title, String message) {
        Notification notification = new Notification(title, message, true);
        return newsDAO.addNotification(notification);
    }
}