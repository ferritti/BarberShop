package Business;

import Authentication.SessionManager;
import DBconnection.DAO.*;
import Model.AvailableSlot;
import Model.ServiceType;
import Model.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewAppointmentControllerSlots {

    @FXML
    private MFXButton backButton;

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

    private UserDAO userDAO = new ConcreteUserDAO();
    private ServiceTypeDAO serviceTypeDAO = new ConcreteServiceTypeDAO();
    private AvailableSlotDAO availableSlotDAO = new ConcreteAvailableSlotDAO();

    HashMap<String, String> barbersData = userDAO.getBarbersData();

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
    public void initialize() {
        barberComboBox.getItems().addAll(barbersData.keySet());

        List<ServiceType> services = serviceTypeDAO.getAllServiceTypes();

        for (ServiceType serviceType : services) {
            serviceComboBox.getItems().add(serviceType.getServiceName());
        }

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
        List<AvailableSlot> barberSlots = availableSlotDAO.getAvSlotsAtSelectedDate(date, barbersData.get(selectedBarber));

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

        public void setDate(LocalDate date) {
            dateLabel.setText(date.toString());
        }
    }



