package Model;

public interface NotificationSubject {
    public void addObserver(NotificationObserver no);
    public void removeObserver(NotificationObserver no);
    public void notifyObserver(Notification notification);

}
