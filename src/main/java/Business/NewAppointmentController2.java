package Business;

import Authentication.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewAppointmentController2 {

    @FXML
    private ImageView back_icon;
    @FXML
    private VBox listContainer; // Contenitore dei nomi
    @FXML
    private Button upButton;   // Pulsante per scorrere in alto
    @FXML
    private Button downButton; // Pulsante per scorrere in basso

    private List<String> barbers = List.of("Barbiere 1", "Barbiere 2", "Barbiere 3", "Barbiere 4", "Barbiere 5", "Barbiere 6", "Barbiere 7");
    private List<Label> labels = new ArrayList<>();
    private int selectedIndex = 0;
    private static final int MAX_VISIBLE = 5;

    // Metodo per inizializzare la lista
    @FXML
    public void initialize() {
        // Creiamo etichette pari al numero di barbieri disponibili (max 5)
        int visibleCount = Math.min(barbers.size(), MAX_VISIBLE);
        for (int i = 0; i < visibleCount; i++) {
            Label label = new Label();
            label.setFont(Font.font(18));
            label.setTextFill(Color.GRAY);
            labels.add(label);
            listContainer.getChildren().add(label);
        }
        updateSelection(); // Imposta la visualizzazione iniziale
    }

    // Metodo per scorrere in basso
    @FXML
    private void scrollDown() {
        if (barbers.size() > 1) {
            selectedIndex = (selectedIndex + 1) % barbers.size(); // Loop infinito
            updateSelection();
        }
    }

    // Metodo per scorrere in alto
    @FXML
    private void scrollUp() {
        if (barbers.size() > 1) {
            selectedIndex = (selectedIndex - 1 + barbers.size()) % barbers.size(); // Loop infinito
            updateSelection();
        }
    }

    // Metodo per aggiornare la selezione
    private void updateSelection() {
        int visibleCount = labels.size();
        for (int i = 0; i < visibleCount; i++) {
            int realIndex = (selectedIndex + i - visibleCount / 2 + barbers.size()) % barbers.size();
            Label label = labels.get(i);
            label.setText(barbers.get(realIndex));

            if (i == visibleCount / 2) { // Nome centrale
                label.setFont(Font.font(24));
                label.setTextFill(Color.BLACK);
            } else {
                label.setFont(Font.font(18));
                label.setTextFill(Color.GRAY);
            }

            int finalIndex = realIndex;
            label.setOnMouseClicked(event -> {
                // Salva il barbiere selezionato nel Singleton
                String selectedBarber = barbers.get(finalIndex);
                AppointmentData.getInstance().setBarber(selectedBarber);

                // Passa alla schermata della selezione orari
                goToTimeSelection();
            });

        }

    }

    public void BackToCalendar(MouseEvent mouseEvent) {
        try {

            SessionManager.getInstance().closeSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointment1.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Calendar");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToTimeSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointment3.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) listContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleziona l'Orario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

