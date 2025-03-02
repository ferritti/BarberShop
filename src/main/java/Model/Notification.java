package Model;

public class Notification {
    private String title;
    private String message;
    private final TargetType targetType;

    public static enum TargetType {
        CUSTOMER, BARBER, ALL;
    }

    public Notification(String title, String message, TargetType targetType) {
        this.title = title;
        this.message = message;
        this.targetType = targetType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TargetType getTargetType() {
        return targetType;
    }
}

