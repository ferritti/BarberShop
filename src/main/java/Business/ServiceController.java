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
import javafx.scene.layout.StackPane;

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
}
