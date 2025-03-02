package Model;

public interface NotificationSubject {
    public void addObserver(NotificationObserver notificationObs);
    public void removeObserver(NotificationObserver notificationObs);
    public void notifyObservers(Notification notification);

}
