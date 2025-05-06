package Controllers;

import Business.ServicesService;
import Helpers.AlertHelper;
import Helpers.SceneHelper;
import Model.ServiceType;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {

    @FXML
    private TableColumn<ServiceType, Void> deleteColumn;

    @FXML
    private TableColumn<ServiceType, String> nameColumn;

    @FXML
    private TableColumn<ServiceType, String> priceColumn;

    @FXML
    private TableView<ServiceType> serviceTable;

    @FXML
    private MFXTextField textFieldName;

    @FXML
    private MFXTextField textFieldPrice;

    private final ServicesService servicesService = new ServicesService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        SceneHelper.setColumnsNotReorderable(nameColumn, priceColumn);
        SceneHelper.centerTextInColumns(nameColumn, priceColumn);

        serviceTable.setSelectionModel(null);

        serviceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addDeleteButtonToTable();

        loadServices();
    }

    @FXML
    void addNewService() {
        String name = textFieldName.getText();
        String priceText = textFieldPrice.getText();

        if (servicesService.areEmptyFields(name, priceText)) {
            AlertHelper.showError("Error", "Both fields must be filled out.");
            return;
        }

        String priceValidationError = servicesService.validatePrice(priceText);
        if (priceValidationError != null) {
            AlertHelper.showError("Error", priceValidationError);
            return;
        }

        double price = Double.parseDouble(priceText);
        confirmAndAddNewService(name, price);

        textFieldName.clear();
        textFieldPrice.clear();
    }


    private void confirmAndAddNewService(String name, double price) {
        boolean confirmed = AlertHelper.showConfirmation("Confirm Add Service", "Are you sure you want to add this service?");
        if (confirmed) {
            if (servicesService.addService(name, price)) {
                AlertHelper.showInformation("Success", "Service added successfully.");
                loadServices();
            } else {
                AlertHelper.showError("Error", "Service could not be added.");
            }
        }
    }

    private void loadServices() {
        List<ServiceType> serviceTypes = servicesService.getService();
        ObservableList<ServiceType> observableList = FXCollections.observableArrayList(serviceTypes);
        serviceTable.setItems(observableList);
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<ServiceType, Void>() {
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png")));
            private final Button deleteButton = new Button();
            private final StackPane pane = new StackPane();

            {
                deleteIcon.setFitWidth(15);
                deleteIcon.setFitHeight(15);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setId("delete-button");

                deleteButton.setOnAction(event -> {
                    ServiceType serviceType = getTableView().getItems().get(getIndex());
                    confirmAndDeleteService(serviceType);
                });

                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                ServiceType serviceType = getTableView().getItems().get(getIndex());
                if (serviceType != null) {
                    setGraphic(pane);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void confirmAndDeleteService(ServiceType serviceType) {
        boolean confirmed = AlertHelper.showConfirmation("Confirm deletion", "Are you sure you want to delete this service?");
        if (confirmed) {
            deleteService(serviceType);
        }
    }

    private void deleteService(ServiceType serviceType) {
        boolean deleted = servicesService.deleteService(serviceType);
        if (deleted) {
            loadServices();
            AlertHelper.showInformation("Success", "Service deleted successfully.");
        } else {
            AlertHelper.showError("Error", "Error while deleting the service.");
        }
    }

    @FXML
    private void goToProfileView() {
        SceneHelper.switchScene(serviceTable, "/View/ProfileBarber.fxml", "Profile");
    }

    @FXML
    private void goToNewsView() {
        SceneHelper.switchScene(serviceTable, "/View/NewsBarber.fxml", "News");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneHelper.switchScene(serviceTable, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneHelper.switchScene(serviceTable, "/View/SendComunication.fxml", "Send Comunication");
    }
}