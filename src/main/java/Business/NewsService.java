package Business;

import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.NewsDAO;
import Authentication.SessionManager;
import Model.Notification;
import Model.User;

import java.util.Comparator;
import java.util.List;

public class NewsService {
    private final NewsDAO newsDAO;
    private SessionManager sessionManager = SessionManager.getInstance();
    public NewsService() {
        this.newsDAO = new ConcreteNewsDAO();
    }

    public NewsService(NewsDAO newsDAO, SessionManager sessionManager) {
        this.newsDAO = newsDAO;
        this.sessionManager = sessionManager;
    }

    public void deleteOldestNewsIfNecessary() {
        if(sessionManager.getCurrentUser().getUserType() == User.UserType.BARBER) {
            List<Notification> news = newsDAO.getAllBarberNews(sessionManager.getCurrentUser().getEmail());
            if (news.size() > 30) {
                news.sort(Comparator.comparing(Notification::getTime));
                int numToDelete = news.size() - 30;
                for (int i = 0; i < numToDelete; i++) {
                    newsDAO.deleteNotification(news.get(i));
                }
            }
        } else {
            List<Notification> news = newsDAO.getAllCustomerNews();
            if (news.size() > 30) {
                news.sort(Comparator.comparing(Notification::getTime));
                int numToDelete = news.size() - 30;
                for (int i = 0; i < numToDelete; i++) {
                    newsDAO.deleteNotification(news.get(i));
                }
            }
        }
    }

    public List<Notification> getNews() {
        if(sessionManager.getCurrentUser().getUserType() == User.UserType.BARBER) {
            return newsDAO.getAllBarberNews(sessionManager.getCurrentUser().getEmail());
        } else {
            return newsDAO.getAllCustomerNews();
        }
    }
}