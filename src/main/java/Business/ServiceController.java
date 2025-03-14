package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteServiceTypeDAO;
import DBconnection.DAO.ServiceTypeDAO;
import Model.ServiceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {

    @FXML
    private VBox add_box;

    @FXML
    private Button add_button;

    @FXML
    private HBox add_new_service;

    @FXML
    private ImageView chair_icon;

    @FXML
    private TableColumn<ServiceType, Void> delete_col;

    @FXML
    private TableColumn<ServiceType, String> name_col;

    @FXML
    private Label name_label;

    @FXML
    private ImageView news_icon;

    @FXML
    private ImageView plus_icon;

    @FXML
    private TableColumn<ServiceType, Double> price_col;

    @FXML
    private Label price_label;

    @FXML
    private ImageView profile_icon;

    @FXML
    private ImageView send_news_icon;

    @FXML
    private ImageView service_icon;

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

        service_table.setSelectionModel(null);

        addDeleteButtonToTable();
        loadServices();
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
                    setAlignment(Pos.CENTER); // Imposta l'allineamento al centro
                }
            }
        });
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
                    ServiceType service = getTableView().getItems().get(getIndex());
                    confirmAndDeleteServiceType(service);
                });
                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);
            }
        });
    }

    private void confirmAndDeleteServiceType(ServiceType service) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm deletion");
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
                deleteService(service);
            }
            // Se l'utente preme "No", non viene eseguita alcuna azione
        });
    }

    private void deleteService(ServiceType service) {
        boolean deleted = serviceTypeDAO.removeServiceType(service);

        if (deleted) {
            service_table.getItems().remove(service);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the service.");
            alert.showAndWait();
        }
    }

    private void loadServices() {
        List<ServiceType> serviceTypes = serviceTypeDAO.getAllServiceTypes();
        ObservableList<ServiceType> observableList = FXCollections.observableArrayList(serviceTypes);
        service_table.setItems(observableList);
    }

    @FXML
    void addService(ActionEvent event) {
        String serviceName = name_label.getText();
        String priceText = price_label.getText();

        // Verifica se il nome e il prezzo sono validi
        if (serviceName.isEmpty() || priceText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Both name and price must be filled in!");
            alert.showAndWait();
            return;
        }

        // Controlla che il prezzo sia un numero valido
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid price.");
            alert.showAndWait();
            return;
        }

        // Se il nome e il prezzo sono validi, chiedi conferma
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Addition");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to add this service?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                // Procedi con l'aggiunta del servizio
                ServiceType serviceType = new ServiceType(serviceName, price);
                boolean added = serviceTypeDAO.addServiceType(serviceType);

                if (added) {
                    service_table.getItems().add(serviceType);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error while adding the service.");
                    alert.showAndWait();
                }
            }
        });
    }

    @FXML
    void showAddServiceForm(MouseEvent event) {
        add_box.setVisible(true);  // Rendi visibile la Box

        // Creiamo una transizione di fade per animare l'opacità
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), add_box);
        fadeTransition.setFromValue(0);  // Partiamo da opacità 0 (invisibile)
        fadeTransition.setToValue(1);    // Arriviamo a opacità 1 (visibile)

        // Avvia la transizione
        fadeTransition.play();
    }
}
