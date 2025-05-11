package Helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

public class AlertHelper {
    private static final String ALERT_CSS = "/styles/AlertStyle.css";
    private static final String CUSTOM_ALERT_CLASS = "custom-alert";

    private AlertHelper() {}

    public static Alert createAlert(AlertType type, String title, String message) {
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

    public static boolean showError(String title, String message, String button1Text, String button2Text) {
        Alert alert = createAlert(Alert.AlertType.ERROR, title, message);
        ButtonType button1 = new ButtonType(button1Text, ButtonBar.ButtonData.OK_DONE);
        ButtonType button2 = new ButtonType(button2Text, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(button1, button2);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == button1;
    }


    public static void showInformation(String title, String message) {
        Alert alert = createAlert(Alert.AlertType.INFORMATION, title, message);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        return showConfirmation(title, message, "Yes", "No");
    }

    public static boolean showConfirmation(String title, String message, String button1Text, String button2Text) {
        Alert alert = createAlert(Alert.AlertType.CONFIRMATION, title, message);
        ButtonType buttonYes = new ButtonType(button1Text, ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType(button2Text, ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    public static void styleAlert(Alert alert) {
        alert.getDialogPane().getStylesheets().add(ALERT_CSS);
        alert.getDialogPane().getStyleClass().add(CUSTOM_ALERT_CLASS);
    }

    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert); // Se già esiste il metodo per lo stile
        alert.showAndWait();
    }

}

