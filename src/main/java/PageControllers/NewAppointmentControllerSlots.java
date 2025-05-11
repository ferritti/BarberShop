package PageControllers;

import Services.NewAppointmentSlotsService;
import Helpers.AlertHelper;
import Helpers.SceneHelper;
import Model.Appointment;
import Model.AvailableSlot;
import Payment.PaymentContext;
import Payment.PaymentFactory;
import Model.PaymentMethod;
import Payment.PaymentStrategy;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private NewAppointmentSlotsService newAppointmentSlotsService = new NewAppointmentSlotsService();
    private List<String> selectedServices = new ArrayList<>();
    String serviceString;
    Double servicePrice;
    private final HashMap<String, String> barbersData = newAppointmentSlotsService.getBarbersData();
    private final HashMap<String, Double> servicesData = newAppointmentSlotsService.getServicesData();

    private final Map<LocalTime, MFXButton> timeButtons = new HashMap<>();
    private final String DISABLED_STYLE = "-fx-border-color: transparent;";
    private final String ENABLED_STYLE = "-fx-border-color: #651FFF;";

    @FXML
    public void initialize() {
        if(newAppointmentSlotsService == null) {
            newAppointmentSlotsService = new NewAppointmentSlotsService();
        }
        timeButtons.put(LocalTime.of(8, 0), eightAMButton);
        timeButtons.put(LocalTime.of(9, 0), nineAMButton);
        timeButtons.put(LocalTime.of(10, 0), tenAMButton);
        timeButtons.put(LocalTime.of(11, 0), elevenAMButton);
        timeButtons.put(LocalTime.of(12, 0), twelveAMButton);
        timeButtons.put(LocalTime.of(13, 0), onePMButton);
        timeButtons.put(LocalTime.of(15, 0), threePMButton);
        timeButtons.put(LocalTime.of(16, 0), fourPMButton);
        timeButtons.put(LocalTime.of(17, 0), fivePMButton);
        timeButtons.put(LocalTime.of(18, 0), sixPMButton);
        timeButtons.put(LocalTime.of(19, 0), sevenPMButton);
        timeButtons.put(LocalTime.of(20, 0), eightPMButton);


        barberComboBox.getItems().addAll(barbersData.keySet());
        serviceComboBox.getItems().addAll(servicesData.keySet());

        disableAllButtons();
    }


    private void disableAllButtons() {
        for (MFXButton button : timeButtons.values()) {
            button.setDisable(true);
            button.setOpacity(0.5);
            button.setStyle(DISABLED_STYLE);
        }
    }

    private void enableButton(LocalTime time) {
        MFXButton button = timeButtons.get(time);
        if (button != null) {
            button.setDisable(false);
            button.setOpacity(1);
            button.setStyle(ENABLED_STYLE);
        }
    }

    public void setServiceComboBox() {

    }

    @FXML
    public void setBarberSlots() {
        disableAllButtons();

        LocalDate date = LocalDate.parse(dateLabel.getText());
        String selectedBarber = barberComboBox.getSelectionModel().getSelectedItem();

        List<AvailableSlot> barberSlots = newAppointmentSlotsService.getAvailableSlots(
                barbersData.get(selectedBarber), date);

        for (AvailableSlot slot : barberSlots) {
            enableButton(slot.getStartTime());
        }
    }

    @FXML
    public void selectSlotAction(javafx.event.ActionEvent actionEvent) {
        servicePrice = 0.0;
        serviceString = "";
        MFXButton button = (MFXButton) actionEvent.getSource();
        String timeString = button.getText();

        if (timeString.length() == 4) {
            timeString = "0" + timeString;
        }

        LocalTime time = LocalTime.parse(timeString);
        LocalDate date = LocalDate.parse(dateLabel.getText());

        List<Appointment> userAppointments = newAppointmentSlotsService.getAppointments();
        for (Appointment appointment : userAppointments) {
            if (newAppointmentSlotsService.isSameDateTime(appointment, date, time)) {
                AlertHelper.showError("Error", "You already have an appointment on this date and time");
                return;
            }
        }

        String selectedBarber = barberComboBox.getSelectionModel().getSelectedItem();
        selectedServices.addFirst(serviceComboBox.getSelectionModel().getSelectedItem());
        StringBuilder sb = new StringBuilder();

        if (selectedServices.getFirst() == null || selectedServices.getFirst().isEmpty()) {
            AlertHelper.showError("Error", "Please select a service before booking an appointment.");
            return;
        }

        for(String service : selectedServices){
            servicePrice += servicesData.get(service);
            sb.append(service).append(", ");
        }

        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        serviceString = sb.toString();

        String confirmationMessage = "Barber: " + selectedBarber + "\n" +
                "Service: " + serviceString + "\n" +
                "Price: " + servicePrice + "€" + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n";

        boolean confirmed = AlertHelper.showConfirmation("Confirmation Dialog",
                "Are you sure you want to book this appointment?\n" + confirmationMessage);
        if (confirmed) {
            showPaymentOptions(selectedBarber, selectedServices, date, time);
        }
    }


    private void showPaymentOptions(String barber, List<String> services, LocalDate date, LocalTime time) {
        Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
        paymentAlert.setTitle("Payment Options");
        paymentAlert.setHeaderText("Select Payment Method");
        paymentAlert.setContentText(
                "Barber: " + barber + "\n" +
                        "Service: " + serviceString + "\n" +
                        "Price: " + servicePrice + "€" + "\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time);

        ButtonType backButton = new ButtonType("Back", ButtonBar.ButtonData.LEFT);
        ButtonType paypalButton = new ButtonType("PayPal", ButtonBar.ButtonData.OTHER);
        ButtonType cardButton = new ButtonType("Credit Card", ButtonBar.ButtonData.OTHER);
        ButtonType shopButton = new ButtonType("Pay at Shop", ButtonBar.ButtonData.RIGHT);

        paymentAlert.getButtonTypes().setAll(paypalButton, cardButton, shopButton, backButton);

        AlertHelper.styleAlert(paymentAlert);

        paymentAlert.showAndWait().ifPresent(selectedType -> {
            if (selectedType == backButton) {
                return;
            }
            PaymentMethod paymentMethod = getPaymentMethod(selectedType, paypalButton, cardButton, shopButton);
            processPayment(barber, services, date, time, paymentMethod);
        });
    }

    private PaymentMethod getPaymentMethod(ButtonType selectedType, ButtonType paypalButton, ButtonType cardButton, ButtonType shopButton) {
        if (selectedType == paypalButton) {
            return PaymentMethod.PAYPAL;
        } else if (selectedType == cardButton) {
            return PaymentMethod.CREDIT_CARD;
        } else {
            return PaymentMethod.SHOP;
        }
    }

    private void processPayment(String barber, List<String> services, LocalDate date, LocalTime time, PaymentMethod paymentMethod) {
        boolean bookingSuccess = newAppointmentSlotsService.bookAppointment(barber, services, date, time, paymentMethod);


        if (bookingSuccess) {
            PaymentStrategy paymentStrategy = PaymentFactory.getPaymentMethod(paymentMethod);
            PaymentContext paymentContext = new PaymentContext(paymentStrategy);
            String paymentMSG = paymentContext.executePayment(servicePrice);
            AlertHelper.showInformation("Booking Confirmed", "Your appointment has been confirmed.\n" + paymentMSG);
            goToAppointmentsView();
        } else {
            AlertHelper.showError("Booking Error", "There was an error while processing your request. Please try again.");
        }
    }

    @FXML
    void backToCalendar() {
        SceneHelper.switchScene(barberComboBox, "/View/NewAppointmentCalendar.fxml", "Calendar");
    }

    @FXML
    void goToAppointmentsView() {
        SceneHelper.switchScene(serviceComboBox, "/View/AppointmentsCustomer.fxml", "Appointments");
    }

    @FXML
    void goToNewsView() {
        SceneHelper.switchScene(serviceComboBox, "/View/NewsCustomer.fxml", "News");
    }

    @FXML
    void goToProfileView () {
        SceneHelper.switchScene(serviceComboBox, "/View/ProfileCustomer.fxml", "Profile");
    }

    public void setDate(LocalDate date) {
        dateLabel.setText(date.toString());
    }

    public void setFirstServiceAction() {
        String firstSelected = serviceComboBox.getSelectionModel().getSelectedItem();
        if (firstSelected == null || firstSelected.isEmpty()) {
            AlertHelper.showError("Error", "Select a service first.");
            return;
        }

        selectedServices.clear();

        Alert alert = AlertHelper.createAlert(Alert.AlertType.CONFIRMATION, "Extra service", "Do you want to add service?");

        // ComboBox con gli altri servizi
        MFXComboBox<String> extraServiceCombo = new MFXComboBox<>();
        List<String> otherServices = new ArrayList<>(servicesData.keySet());
        extraServiceCombo.setId("extraServiceComboBox");
        otherServices.remove(firstSelected);
        extraServiceCombo.getItems().addAll(otherServices);
        extraServiceCombo.setPromptText("Select the extra service");

        // Layout per il contenuto del dialog
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Main service selected: " + firstSelected), extraServiceCombo);
        alert.getDialogPane().setContent(content);

        // Bottoni personalizzati
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Mostra e gestisce la risposta
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                String extra = extraServiceCombo.getSelectionModel().getSelectedItem();
                if (extra != null && !extra.isEmpty()) {
                    selectedServices.add(extra);
                    AlertHelper.showInformation("Extra service added", "You have also added: " + extra);
                } else {
                    AlertHelper.showWarning("Attention", "No extra service selected");
                }
            }
        });
    }

}



