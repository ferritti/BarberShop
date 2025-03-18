package Business;

import DBconnection.DAO.ConcreteServiceTypeDAO;
import DBconnection.DAO.ServiceTypeDAO;
import Model.ServiceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {

    @FXML
    private ImageView chair_icon;

    @FXML
    private TableColumn<ServiceType, Void> delete_col;

    @FXML
    private TableColumn<ServiceType, String> name_col;

    @FXML
    private TableColumn<ServiceType, Double> price_col;

    @FXML
    private TableView<ServiceType> service_table;

    @FXML
    private HBox add_service_box;

    @FXML
    private VBox add_service_box_container;  // Assicurati che sia il VBox corretto con i campi di input


    @FXML
    private Label ADD_button;

    @FXML
    private Label BACK_button;
    @FXML
    private Label service_name;

    @FXML
    private Label service_price;



    private final ServiceTypeDAO serviceTypeDAO = new ConcreteServiceTypeDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_col.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        name_col.setReorderable(false);
        price_col.setReorderable(false);

        centerTextInColumn(name_col);
        centerTextInColumn(price_col);

        addDeleteButtonToTable();  // Aggiungi questa riga per assicurarti che il pulsante di eliminazione venga aggiunto alla colonna

        service_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        service_table.setSelectionModel(null);

        loadServices();
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
                    ServiceType serviceType = getTableView().getItems().get(getIndex());
                    confirmAndDeleteService(serviceType);  // Mostra la conferma prima di eliminare il servizio
                });

                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    private void loadServices() {
        List<ServiceType> services = serviceTypeDAO.getAllServiceTypes();
        ObservableList<ServiceType> observableList = FXCollections.observableArrayList(services);
        service_table.setItems(observableList);
    }

    private <T> void centerTextInColumn(TableColumn<ServiceType, T> column) {
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

    private void confirmAndDeleteService(ServiceType serviceType) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmed Deletion");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to delete this service?");

        // Personalizza i pulsanti
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Mostra il dialogo e attendi la risposta
        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                // L'utente ha confermato, procedi con l'eliminazione
                deleteService(serviceType);
            }
            // Se l'utente preme "No", non viene eseguita alcuna azione
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void deleteService(ServiceType serviceType) {
        boolean deleted = serviceTypeDAO.removeServiceType(serviceType);  // Elimina dal database

        if (deleted) {
            service_table.getItems().remove(serviceType);  // Rimuovi dalla tabella
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while deleting the service. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void showAddServiceBox() {
        add_service_box_container.setOpacity(1);
        add_service_box_container.setDisable(false);
    }

    @FXML
    private void hideAddServiceBox() {
        add_service_box_container.setOpacity(0);
        add_service_box_container.setDisable(true);
        service_name.setText("");
        service_price.setText("");
    }


    @FXML
    private void confirmAddService() {
        String serviceName = service_name.getText().trim();
        String priceText = service_price.getText().trim();

        if (serviceName.isEmpty() || priceText.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.ERROR);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showAlert("Error", "Price must be greater than zero.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price format.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Addition");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to add this service?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                addService(serviceName, price);
            }
        });
    }

    private void addService(String name, double price) {
        ServiceType newService = new ServiceType(name, price);
        boolean added = serviceTypeDAO.addServiceType(newService);

        if (added) {
            service_table.getItems().add(newService);
            hideAddServiceBox();
            showAlert("Success", "Service added successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Error while adding the service.", Alert.AlertType.ERROR);
        }
    }
}