package Business;

import Authentication.SessionManager;
import DBconnection.DAO.*;
import Model.Appointment;
import Model.AvailableSlot;
import Model.Notification;
import Model.User;
import Payment.PaymentContext;
import Payment.PaymentFactory;
import Payment.PaymentMethod;
import Payment.PaymentStrategy;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewAppointmentControllerSlots {

    @FXML
    private MFXComboBox<String> barberComboBox;

    @FXML
    private MFXComboBox<String> serviceComboBox;

    @FXML
    private MFXButton eightAMButton;

    @FXML
    private MFXButton eightPMButton;

    @FXML
    private MFXButton elevenAMButton;

    @FXML
    private MFXButton fivePMButton;

    @FXML
    private MFXButton fourPMButton;

    @FXML
    private MFXButton nineAMButton;

    @FXML
    private MFXButton onePMButton;

    @FXML
    private MFXButton sevenPMButton;

    @FXML
    private MFXButton sixPMButton;

    @FXML
    private MFXButton tenAMButton;

    @FXML
    private MFXButton threePMButton;

    @FXML
    private MFXButton twelveAMButton;

    @FXML
    private Label dateLabel;

    NewAppointmentControllerSlotsService newAppointmentControllerSlotsService = new NewAppointmentControllerSlotsService();

    HashMap<String, String> barbersData = newAppointmentControllerSlotsService.getBarbersData();
    HashMap<String, Double> servicesData = newAppointmentControllerSlotsService.getServicesData();

    @FXML
    public void initialize() {
        barberComboBox.getItems().addAll(barbersData.keySet());

        serviceComboBox.getItems().addAll(servicesData.keySet());

        disableAllButtons();
    }


    public void disableAllButtons() {
        eightAMButton.setDisable(true);
        eightAMButton.setOpacity(0.5);
        eightAMButton.setStyle("-fx-border-color: transparent;");


        nineAMButton.setDisable(true);
        nineAMButton.setOpacity(0.5);
        nineAMButton.setStyle("-fx-border-color: transparent;");

        tenAMButton.setDisable(true);
        tenAMButton.setOpacity(0.5);
        tenAMButton.setStyle("-fx-border-color: transparent;");

        elevenAMButton.setDisable(true);
        elevenAMButton.setOpacity(0.5);
        elevenAMButton.setStyle("-fx-border-color: transparent;");

        twelveAMButton.setDisable(true);
        twelveAMButton.setOpacity(0.5);
        twelveAMButton.setStyle("-fx-border-color: transparent;");

        onePMButton.setDisable(true);
        onePMButton.setOpacity(0.5);
        onePMButton.setStyle("-fx-border-color: transparent;");

        threePMButton.setDisable(true);
        threePMButton.setOpacity(0.5);
        threePMButton.setStyle("-fx-border-color: transparent;");

        fourPMButton.setDisable(true);
        fourPMButton.setOpacity(0.5);
        fourPMButton.setStyle("-fx-border-color: transparent;");

        fivePMButton.setDisable(true);
        fivePMButton.setOpacity(0.5);
        fivePMButton.setStyle("-fx-border-color: transparent;");

        sixPMButton.setDisable(true);
        sixPMButton.setOpacity(0.5);
        sixPMButton.setStyle("-fx-border-color: transparent;");

        sevenPMButton.setDisable(true);
        sevenPMButton.setOpacity(0.5);
        sevenPMButton.setStyle("-fx-border-color: transparent;");

        eightPMButton.setDisable(true);
        eightPMButton.setOpacity(0.5);
        eightPMButton.setStyle("-fx-border-color: transparent;");
    }

    @FXML
    public void setBarberSlots() {
        disableAllButtons();

        LocalDate date = LocalDate.parse(dateLabel.getText());

        String selectedBarber = barberComboBox.getSelectionModel().getSelectedItem();

        List<AvailableSlot> barberSlots = newAppointmentControllerSlotsService.getAvailableSlots(barbersData.get(selectedBarber), date);

        for (AvailableSlot slot : barberSlots) {
            LocalTime startTime = slot.getStartTime();

            if (startTime.equals(LocalTime.of(8, 0, 0))) {
                eightAMButton.setDisable(false);
                eightAMButton.setOpacity(1);
                eightAMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(9, 0))) {
                nineAMButton.setDisable(false);
                nineAMButton.setOpacity(1);
                nineAMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(10, 0))) {
                tenAMButton.setDisable(false);
                tenAMButton.setOpacity(1);
                tenAMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(11, 0))) {
                elevenAMButton.setDisable(false);
                elevenAMButton.setOpacity(1);
                elevenAMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(12, 0))) {
                twelveAMButton.setDisable(false);
                twelveAMButton.setOpacity(1);
                twelveAMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(13, 0))) {
                onePMButton.setDisable(false);
                onePMButton.setOpacity(1);
                onePMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(15, 0))) {
                threePMButton.setDisable(false);
                threePMButton.setOpacity(1);
                threePMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(16, 0))) {
                fourPMButton.setDisable(false);
                fourPMButton.setOpacity(1);
                fourPMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(17, 0))) {
                fivePMButton.setDisable(false);
                fivePMButton.setOpacity(1);
                fivePMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(18, 0))) {
                sixPMButton.setDisable(false);
                sixPMButton.setOpacity(1);
                sixPMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(19, 0))) {
                sevenPMButton.setDisable(false);
                sevenPMButton.setOpacity(1);
                sevenPMButton.setStyle("-fx-border-color: #651FFF;");
            } else if (startTime.equals(LocalTime.of(20, 0))) {
                eightPMButton.setDisable(false);
                eightPMButton.setOpacity(1);
                eightPMButton.setStyle("-fx-border-color: #651FFF;");
            }
        }

    }

    @FXML
    void backToCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewAppointmentCalendar.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) serviceComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Calendar");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToAppointmentsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) serviceComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goToNewsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) serviceComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

        @FXML
        void goToProfileView () {
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

                Stage stage = (Stage) serviceComboBox.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Profile");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    @FXML
    public void selectSlotAction(javafx.event.ActionEvent actionEvent) {
        MFXButton button = (MFXButton) actionEvent.getSource();

        String timeString = button.getText();

        if (timeString.length() == 4) {
            timeString = "0" + timeString;
        }

        LocalTime time = LocalTime.parse(timeString);

        LocalDate date = LocalDate.parse(dateLabel.getText());

        List<Appointment> userAppointments = newAppointmentControllerSlotsService.getAppointments();

        for (Appointment appointment : userAppointments) {
            if(newAppointmentControllerSlotsService.isSameDateTime(appointment, date, time)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An appointment already exists");
                alert.setContentText("You have already an appointment in this date and time");
                alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
                alert.getDialogPane().getStyleClass().add("custom-alert");
                alert.showAndWait();
                return;
            }
        }

        String selectedBarber = barberComboBox.getSelectionModel().getSelectedItem();
        String selectedService = serviceComboBox.getSelectionModel().getSelectedItem();


        if (selectedService == null || selectedService.isEmpty()) {


            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Service Selection Required");
            errorAlert.setContentText("Please select a service before booking an appointment.");
            errorAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            errorAlert.getDialogPane().getStyleClass().add("custom-alert");

            errorAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            errorAlert.getDialogPane().getStyleClass().add("custom-alert");
            errorAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to book this appointment?");
        alert.setContentText("Barber: " + selectedBarber + "\n" +
                "Service: " + selectedService + "\n" +
                "Price: " + servicesData.get(selectedService) + "€" + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" );

        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType paymentButton = new ButtonType("Proceed to Payment", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(cancelButton, paymentButton);
        alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        alert.getDialogPane().getStyleClass().add("custom-alert");

        alert.showAndWait().ifPresent(type -> {
            if (type == paymentButton) {
                showPaymentOptions(selectedBarber, selectedService, date, time);
            }
        });
    }

    private void showPaymentOptions(String barber, String service, LocalDate date, LocalTime time) {
        Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
        paymentAlert.setTitle("Payment Options");
        paymentAlert.setHeaderText("Select Payment Method");
        paymentAlert.setContentText(
                "Barber: " + barber + "\n" +
                "Service: " + service + "\n" +
                "Price: " + servicesData.get(service) + "€" + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time);

        ButtonType backButton = new ButtonType("Back", ButtonBar.ButtonData.LEFT);
        ButtonType paypalButton = new ButtonType("PayPal", ButtonBar.ButtonData.OTHER);
        ButtonType cardButton = new ButtonType("Credit Card", ButtonBar.ButtonData.OTHER);
        ButtonType shopButton = new ButtonType("Pay at Shop", ButtonBar.ButtonData.RIGHT);

        paymentAlert.getButtonTypes().setAll(paypalButton, cardButton, shopButton, backButton);

        paymentAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        paymentAlert.getDialogPane().getStyleClass().add("custom-alert");



        paymentAlert.showAndWait().ifPresent(type -> {
            if (type == paypalButton || type == cardButton || type == shopButton) {
                String barberEmail = barbersData.get(barber);


                PaymentMethod paymentMethod;
                if (type == paypalButton) {
                    paymentMethod = PaymentMethod.PAYPAL;
                } else if (type == cardButton) {
                    paymentMethod = PaymentMethod.CREDIT_CARD;
                } else {
                    paymentMethod = PaymentMethod.SHOP;
                }


                User currentUser = SessionManager.getInstance().getCurrentUser();


                double servicePrice = servicesData.get(service);


                Appointment appointment = new Appointment(
                        paymentMethod,
                        service,
                        barber,
                        barberEmail,
                        currentUser.getEmail(),
                        currentUser.getPhone(),
                        time,
                        date,
                        servicePrice
                );


                AvailableSlot selectedSlot = new AvailableSlot(barberEmail, date, time);
                boolean slotRemoved = newAppointmentControllerSlotsService.removeAvSlot(selectedSlot);


                boolean appointmentAdded = newAppointmentControllerSlotsService.addAppointment(appointment);

                boolean notificationAdded = newAppointmentControllerSlotsService.addNotification(barberEmail);

                if (slotRemoved && appointmentAdded && notificationAdded) {
                    PaymentStrategy paymentStrategy = PaymentFactory.getPaymentMethod(paymentMethod);

                    PaymentContext paymentContext = new PaymentContext(paymentStrategy);

                    String paymentMSG = paymentContext.executePayment(servicePrice);

                    // Crea l'alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Booking Confirmed");
                    successAlert.setHeaderText("Appointment Booked Successfully");
                    successAlert.setContentText("Your appointment has been confirmed.\n" + paymentMSG);

// Applica uno stile CSS per migliorare l'aspetto
                    successAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
                    successAlert.getDialogPane().getStyleClass().add("custom-alert");

// Mostra l'alert
                    successAlert.showAndWait();


                    goToAppointmentsView();
                } else {

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Booking Error");
                    errorAlert.setHeaderText("Could not complete booking");
                    errorAlert.setContentText("There was an error while processing your request. Please try again.");
                    errorAlert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
                    errorAlert.getDialogPane().getStyleClass().add("custom-alert");
                    errorAlert.showAndWait();
                }
            }
        });
    }
        public void setDate(LocalDate date) {
            dateLabel.setText(date.toString());
        }

}



