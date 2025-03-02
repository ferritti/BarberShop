package Model;

public interface NotificationObserver {
    public void update(Notification notification);
    public User.UserType getUserType();
}
