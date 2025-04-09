package Controllers;

import Business.ServicesService;
import Helpers.SceneNavigator;
import Model.ServiceType;
import io.github.palexdev.materialfx.controls.MFXTextField;
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        SceneNavigator.setColumnsNotReorderable(nameColumn, priceColumn);
        SceneNavigator.centerTextInColumns(nameColumn, priceColumn);

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
            sendAlert("Error", "Both fields must be filled out.");
            return;
        }

        String priceValidationError = servicesService.validatePrice(priceText);
        if (priceValidationError != null) {
            sendAlert("Error", priceValidationError);
            return;
        }

        double price = Double.parseDouble(priceText);
        confirmAndAddNewService(name, price);

        textFieldName.clear();
        textFieldPrice.clear();
    }


    private void confirmAndAddNewService(String title, double price) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Add Service");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to add this service?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmDialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        confirmDialog.getDialogPane().getStyleClass().add("custom-alert");

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                if(servicesService.addService(title, price)) {
                    sendAlert("Success", "Service added successfully.");
                    loadServices();
                } else {
                    sendAlert("Error", "Service could not be added.");
                }
            }
        });
    }

    private void sendAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait();
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
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm deletion");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this service?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmDialog.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
        confirmDialog.getDialogPane().getStyleClass().add("custom-alert");

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                deleteService(serviceType);
            }
        });
    }

    private void deleteService(ServiceType serviceType) {
        boolean deleted = servicesService.deleteService(serviceType);

        if (deleted) {
            loadServices();
            sendAlert("Success", "Service deleted successfully.");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the service.");
            alert.getDialogPane().getStylesheets().add("/styles/AlertStyle.css");
            alert.getDialogPane().getStyleClass().add("custom-alert");
            alert.showAndWait();
        }
    }

    @FXML
    private void goToProfileView() {
        SceneNavigator.switchScene(serviceTable, "/View/ProfileBarber.fxml", "Profile");
    }

    @FXML
    private void goToNewsView() {
        SceneNavigator.switchScene(serviceTable, "/View/NewsBarber.fxml", "News");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneNavigator.switchScene(serviceTable, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneNavigator.switchScene(serviceTable, "/View/SendComunication.fxml", "Send Comunication");
    }
}