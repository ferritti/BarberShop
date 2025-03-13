package Business;

import Authentication.SessionManager;
import DBconnection.DAO.AppointmentDAO;
import DBconnection.DAO.ConcreteAppointmentDAO;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentBarberController implements Initializable {

    @FXML
    private TableView<Appointment> appointments_table;

    @FXML
    private ImageView chair_icon;

    @FXML
    private TableColumn<Appointment, String> customer_col;

    @FXML
    private TableColumn<Appointment, LocalDate> date_col;

    @FXML
    private TableColumn<Appointment, Void> delete_col;

    @FXML
    private ImageView news_icon;

    @FXML
    private TableColumn<Appointment, String> payment_col;

    @FXML
    private ImageView plus_icon;

    @FXML
    private TableColumn<Appointment, Double> price_col;

    @FXML
    private ImageView profile_icon;

    @FXML
    private ImageView send_news_icon;

    @FXML
    private TableColumn<Appointment, String> service_col;

    @FXML
    private ImageView service_icon;

    @FXML
    private TableColumn<Appointment, LocalTime> time_col;

    private final AppointmentDAO appointmentDAO = new ConcreteAppointmentDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer_col.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        service_col.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        time_col.setCellValueFactory(new PropertyValueFactory<>("time"));
        payment_col.setCellValueFactory(new PropertyValueFactory<>("payment"));

        customer_col.setReorderable(false);
        date_col.setReorderable(false);
        price_col.setReorderable(false);
        service_col.setReorderable(false);
        time_col.setReorderable(false);
        payment_col.setReorderable(false);

        centerTextInColumn(customer_col);
        centerTextInColumn(date_col);
        centerTextInColumn(price_col);
        centerTextInColumn(service_col);
        centerTextInColumn(time_col);
        centerTextInColumn(payment_col);

        appointments_table.setSelectionModel(null);

        addDeleteButtonToTable();
        loadAppointments();
    }

    private <T> void centerTextInColumn(TableColumn<Appointment, T> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER); // Imposta l'allineamento al centro
                }
            }
        });
    }


    private void loadAppointments() {
        List<Appointment> appointments = appointmentDAO.findByEmailOfUser(SessionManager.getInstance().getCurrentUser().getEmail());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        appointments_table.setItems(observableList);
    }

    private void addDeleteButtonToTable() {
        delete_col.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png")));
            private final Button deleteButton = new Button();
            private final StackPane pane = new StackPane();

            {
                deleteIcon.setFitWidth(15);
                deleteIcon.setFitHeight(15);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                deleteButton.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());

                    // Verifica se l'appuntamento è nel passato
                    if (isPastAppointment(appointment)) {
                        showPastAppointmentError();
                    } else {
                        // Mostra una richiesta di conferma
                        confirmAndDeleteAppointment(appointment);
                    }
                });

                // Usare StackPane per centrare il bottone nella cella
                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Appointment appointment = getTableView().getItems().get(getIndex());

                    // Disabilita visivamente il pulsante per appuntamenti passati
                    if (isPastAppointment(appointment)) {
                        deleteButton.setDisable(true);
                        deleteButton.setOpacity(0.3);
                    } else {
                        deleteButton.setDisable(false);
                        deleteButton.setOpacity(1.0);
                    }

                    setGraphic(pane);
                }
            }
        });
    }

    /**
     * Verifica se un appuntamento è nel passato
     * @param appointment L'appuntamento da verificare
     * @return true se l'appuntamento è passato, false altrimenti
     */
    private boolean isPastAppointment(Appointment appointment) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.isBefore(now);
    }

    /**
     * Mostra un messaggio di errore per gli appuntamenti passati
     */
    private void showPastAppointmentError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operazione non consentita");
        alert.setHeaderText(null);
        alert.setContentText("Non è possibile eliminare appuntamenti passati.");
        alert.showAndWait();
    }

    private void confirmAndDeleteAppointment(Appointment appointment) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm deletion");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this appointment?");

        // Personalizza i pulsanti
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Mostra il dialogo e attendi la risposta
        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                // L'utente ha confermato, procedi con l'eliminazione
                deleteAppointment(appointment);
            }
            // Se l'utente preme "No", non viene eseguita alcuna azione
        });
    }

    private void deleteAppointment(Appointment appointment) {
        boolean deleted = appointmentDAO.deleteAppointment(appointment);

        if (deleted) {
            appointments_table.getItems().remove(appointment);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the appointment.");
            alert.showAndWait();
        }
    }

    @FXML
    private void goToProfileAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileBarber.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) profile_icon.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("ProfileBarber");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}