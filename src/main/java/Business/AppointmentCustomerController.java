package Business;

import Authentication.SessionManager;
import DBconnection.DAO.*;
import Model.Appointment;
import Model.AvailableSlot;
import Model.Notification;
import Model.User;
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


public class AppointmentCustomerController implements Initializable {

    @FXML
    private TableView<Appointment> tableViewCustomerAppointments;
    @FXML
    private TableColumn<Appointment, String> barberColumn;
    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Appointment, Double> priceColumn;
    @FXML
    private TableColumn<Appointment, String> serviceColumn;
    @FXML
    private TableColumn<Appointment, LocalTime> timeColumn;
    @FXML
    private TableColumn<Appointment, String> paymentColumn;
    @FXML
    private TableColumn<Appointment, Void> deleteColumn;

    private final AppointmentDAO appointmentDAO = new ConcreteAppointmentDAO();
    private final AvailableSlotDAO availableSlotDAO = new ConcreteAvailableSlotDAO();
    private final NewsDAO newsDAO = new ConcreteNewsDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barberColumn.setCellValueFactory(new PropertyValueFactory<>("barberName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceTypeName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        barberColumn.setReorderable(false);
        dateColumn.setReorderable(false);
        priceColumn.setReorderable(false);
        serviceColumn.setReorderable(false);
        timeColumn.setReorderable(false);
        paymentColumn.setReorderable(false);

        centerTextInColumn(barberColumn);
        centerTextInColumn(dateColumn);
        centerTextInColumn(priceColumn);
        centerTextInColumn(serviceColumn);
        centerTextInColumn(timeColumn);
        centerTextInColumn(paymentColumn);

        tableViewCustomerAppointments.setSelectionModel(null);

        addDeleteButtonToTable();

        tableViewCustomerAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        paymentColumn.setMinWidth(95);
        dateColumn.setMinWidth(80);

        deleteColumn.setMaxWidth(40);
        timeColumn.setMaxWidth(50);
        priceColumn.setMaxWidth(40);

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
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }


    private void loadAppointments() {
        List<Appointment> appointments = appointmentDAO.findByEmailOfUser(SessionManager.getInstance().getCurrentUser().getEmail());
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        tableViewCustomerAppointments.setItems(observableList);
    }


    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
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

                    if (isPastAppointment(appointment)) {
                        showPastAppointmentError();
                    } else {
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

                    if (isPastAppointment(appointment)) {
                        deleteButton.setOpacity(0.3);
                    } else {
                        deleteButton.setOpacity(1.0);
                    }

                    setGraphic(pane);
                }
            }
        });
    }


    private boolean isPastAppointment(Appointment appointment) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
        LocalDateTime now = LocalDateTime.now();
        return appointmentDateTime.isBefore(now);
    }


    private void showPastAppointmentError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operation not allowed");
        alert.setHeaderText(null);
        alert.setContentText("It is not possible to delete past appointments.");

        ButtonType buttonTypeOk = new ButtonType("Ok");
        alert.getButtonTypes().setAll(buttonTypeOk);

        alert.showAndWait().ifPresent(buttonType -> {;
            if (buttonType == buttonTypeOk) {
                alert.close();
            }
        });
    }


    private void confirmAndDeleteAppointment(Appointment appointment) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm deletion");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this appointment?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                deleteAppointment(appointment);

                AvailableSlot availableSlot = new AvailableSlot(appointment.getBarberEmail(), appointment.getDate(), appointment.getTime());
                availableSlotDAO.addAvSlot(availableSlot);
            }
        });
    }


    private void deleteAppointment(Appointment appointment) {
        boolean deleted = appointmentDAO.deleteAppointment(appointment);

        if (deleted) {
            tableViewCustomerAppointments.getItems().remove(appointment);
            Notification notification = new Notification("Appointment available", "An appointment has become available on " + appointment.getDate() + " at " + appointment.getTime(), Notification.TargetType.CUSTOMER);
            newsDAO.addNews(notification);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the appointment.");
            alert.showAndWait();
        }
    }


    @FXML
    private void goToNewsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableViewCustomerAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfileAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileCustomer.fxml"));
            Parent root = loader.load();

            ProfileCustomerController controller = loader.getController();
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null) {
                controller.profileAction(
                        currentUser.getName(),
                        currentUser.getSurname(),
                        currentUser.getEmail(),
                        currentUser.getPhone());
            }

            Stage stage = (Stage) tableViewCustomerAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toNewAppointment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentCalendar.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tableViewCustomerAppointments.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("New Appointment");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}