package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteServiceTypeDAO;
import DBconnection.DAO.ServiceTypeDAO;
import Model.ServiceType;
import Model.User;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
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
    private ImageView news_icon;

    @FXML
    private TableColumn<ServiceType, String> price_col;

    @FXML
    private ImageView profile_icon;

    @FXML
    private ImageView send_news_icon;

    @FXML
    private ImageView service_icon;

    @FXML
    private TableView<ServiceType> service_table;

    @FXML
    private MFXTextField text_field_name;

    @FXML
    private MFXTextField text_field_price;

    private ServiceTypeDAO serviceTypeDAO = new ConcreteServiceTypeDAO();

    @FXML
    void addNewService(ActionEvent event) {
        String name = text_field_name.getText();
        String priceText = text_field_price.getText();

        if (name.isEmpty() || priceText.isEmpty()) {
            sendAlert("Error", "Both fields must be filled out.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            sendAlert("Error", "Price must be a valid number.");
            return;
        }

        if (price <= 0) {
            sendAlert("Error", "Price must be greater than 0.");
            return;
        }

        confirmAndAddNewService(name, price);

        text_field_name.clear();
        text_field_price.clear();
    }


    private void confirmAndAddNewService(String title, double price) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Add Service");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to add this service?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        confirmDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                ServiceType serviceType = new ServiceType(title, price);
                if(serviceTypeDAO.addServiceType(serviceType)) {
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
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_col.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        name_col.setReorderable(false);
        price_col.setReorderable(false);

        service_table.setSelectionModel(null);

        service_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addDeleteButtonToTable();

        loadServices();
    }

    private void loadServices() {
        List<ServiceType> serviceTypes = serviceTypeDAO.getAllServiceTypes();
        ObservableList<ServiceType> observableList = FXCollections.observableArrayList(serviceTypes);
        service_table.setItems(observableList);
    }

    @FXML
    private void goToProfileView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileBarber.fxml"));
            Parent root = loader.load();

            ProfileBarberController controller = loader.getController();
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null) {
                controller.profileAction(
                        currentUser.getName(),
                        currentUser.getSurname(),
                        currentUser.getEmail(),
                        currentUser.getPhone());
            }

            Stage stage = (Stage) profile_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToNewsView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/NewsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) news_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("News");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAppointmentsView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsBarber.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) news_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSendComunicationView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SendComunication.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) news_icon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Comunication");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToTable() {
        // Impostiamo la cella di eliminazione
        delete_col.setCellFactory(param -> new TableCell<ServiceType, Void>() {
            private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete.png")));
            private final Button deleteButton = new Button();
            private final StackPane pane = new StackPane();

            {
                // Impostiamo le dimensioni e l'icona del pulsante
                deleteIcon.setFitWidth(15);
                deleteIcon.setFitHeight(15);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                // Azione di eliminazione del servizio
                deleteButton.setOnAction(event -> {
                    ServiceType serviceType = getTableView().getItems().get(getIndex());
                    confirmAndDeleteService(serviceType);  // Chiama la funzione di conferma per eliminare
                });

                // Aggiungiamo il pulsante al pannello di StackPane
                pane.getChildren().add(deleteButton);
                pane.setAlignment(Pos.CENTER);  // Centriamo il pulsante nella cella
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // Se la cella è vuota o non c'è un ServiceType valido in questa riga, non mostrare nulla
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                // Verifica se c'è un ServiceType valido prima di mostrare il pulsante
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

        confirmDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                deleteService(serviceType);
            }
        });
    }


    private void deleteService(ServiceType serviceType) {
        boolean deleted = serviceTypeDAO.removeServiceType(serviceType);

        if (deleted) {
            loadServices();
            sendAlert("Success", "Service deleted successfully.");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error while deleting the service.");
            alert.showAndWait();
        }
    }
}