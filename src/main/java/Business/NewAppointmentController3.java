package Business;

import Authentication.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewAppointmentController3 implements Initializable {
    @FXML
    public Label data_label;

    @FXML
    private ImageView back_icon;

    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12,
            btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20, btn21, btn22, btn23, btn24;

    private final List<Button> indices = new ArrayList<>();
    private final List<Integer> selectedIndicesList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Aggiungi i bottoni alla lista
        indices.addAll(Arrays.asList(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12,
                btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20, btn21, btn22, btn23, btn24
        ));

        // Imposta lo stile iniziale e il comportamento dei bottoni
        for (int i = 0; i < indices.size(); i++) {
            int index = i + 1;
            Button button = indices.get(i);
        }


        // Imposta la data corrente
        data_label.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void toggleButton(Button button, int index) {
        if (selectedIndicesList.contains(index)) {
            selectedIndicesList.remove(Integer.valueOf(index));
            button.setStyle("-fx-background-color: white;");
        } else {
            selectedIndicesList.add(index);
            button.setStyle("-fx-background-color: lightblue;");
        }
    }

    // Metodo per aggiornare la data
    public void updateDate(LocalDate date) {
        data_label.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void BackToBarbierSelectionAction(MouseEvent mouseEvent) {
        try {

            SessionManager.getInstance().closeSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointment2.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BarberSelection");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonActionNewApp(ActionEvent event) {
        // Ottieni l'istanza di AppointmentData
        AppointmentData appointmentData = AppointmentData.getInstance();

        // Supponiamo che la data e il barbiere siano già stati impostati in AppointmentData
        LocalDate data = appointmentData.getData(); // La data selezionata
        String barbiere = appointmentData.getBarber(); // Il barbiere selezionato

        // Ottieni l'orario dal numero del pulsante premuto
        Button buttonPressed = (Button) event.getSource();  // Ottieni il pulsante che è stato premuto
        String orario = getOrarioFromButton(buttonPressed.getText());  // Mappa il numero del pulsante all'orario

        // Crea il messaggio per l'alert di conferma
        String messaggio = String.format(
                "Hai prenotato un appuntamento con %s per il giorno %s alle ore %s.\nConfermi?",
                barbiere,
                data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                orario
        );

        // Crea l'alert di conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Appuntamento");
        alert.setHeaderText("Conferma la tua prenotazione");
        alert.setContentText(messaggio);

        // Mostra l'alert e gestisci la risposta dell'utente
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // L'utente ha confermato l'appuntamento
                System.out.println("Appuntamento confermato!");
                AppointmentData.resetInstance();
            } else {
                // L'utente ha annullato l'appuntamento
                System.out.println("Appuntamento annullato.");
            }
        });
    }

    // Metodo per mappare il numero del pulsante all'orario
    private String getOrarioFromButton(String buttonText) {
        // Mappa il numero del pulsante all'orario in formato "HH:mm"
        switch (buttonText) {
            case "1": return "1.00";
            case "2": return "2.00";
            case "3": return "3.00";
            case "4": return "4.00";
            case "5": return "5.00";
            case "6": return "6.00";
            case "7": return "7.00";
            case "8": return "8.00";
            case "9": return "9:00";
            case "10": return "10:00";
            case "11": return "11:00";
            case "12": return "12:00";
            case "13": return "13:00";
            case "14": return "14:00";
            case "15": return "15:00";
            case "16": return "16:00";
            case "17": return "17:00";
            case "18": return "18:00";
            case "19": return "19:00";
            case "20": return "20:00";
            case "21": return "21:00";
            case "22": return "22:00";
            case "23": return "23:00";
            case "24": return "24:00";
            default: return "Orario non valido";
        }
    }

}
