package Business;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertHelper {
    private static final String ALERT_CSS = "/styles/AlertStyle.css";
    private static final String CUSTOM_ALERT_CLASS = "custom-alert";

    // Impedisce l'instanziazione della classe di utilità
    private AlertHelper() {}

    private static Alert createAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(ALERT_CSS);
        alert.getDialogPane().getStyleClass().add(CUSTOM_ALERT_CLASS);
        return alert;
    }

    public static void showError(String title, String message) {
        Alert alert = createAlert(Alert.AlertType.ERROR, title, message);
        alert.showAndWait();
    }

    public static void showInformation(String title, String message) {
        Alert alert = createAlert(Alert.AlertType.INFORMATION, title, message);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        return showConfirmation(title, message, "Yes", "No");
    }

    public static boolean showConfirmation(String title, String message, String yesText, String noText) {
        Alert alert = createAlert(Alert.AlertType.CONFIRMATION, title, message);
        ButtonType buttonYes = new ButtonType(yesText, ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType(noText, ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    public static void styleAlert(Alert alert) {
        alert.getDialogPane().getStylesheets().add(ALERT_CSS);
        alert.getDialogPane().getStyleClass().add(CUSTOM_ALERT_CLASS);
    }
}

