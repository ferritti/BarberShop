package Model;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements NotificationSubject {
    private List<NotificationObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            if (notification.getTargetType() == Notification.TargetType.ALL ||
                    (notification.getTargetType() == Notification.TargetType.BARBER && observer.getUserType() == User.UserType.BARBER) ||
                    (notification.getTargetType() == Notification.TargetType.CUSTOMER && observer.getUserType() == User.UserType.CUSTOMER)) {
                observer.update(notification);
            }
        }
    }
}
