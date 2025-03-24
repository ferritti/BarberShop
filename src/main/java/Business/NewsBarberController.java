package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.NewsDAO;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class NewsBarberController implements Initializable {


    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TableView<Notification> newsTable;

    @FXML
    private TableColumn<Notification, LocalTime> timeColumn;

    @FXML
    private TableColumn<Notification, String > titleColumn;

    private NewsDAO newsDAO = new ConcreteNewsDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        titleColumn.setReorderable(false);
        messageColumn.setReorderable(false);
        timeColumn.setReorderable(false);
        newsTable.setSelectionModel(null);

        // Imposta il wrapping per la colonna titolo
        titleColumn.setCellFactory(tc -> {
            TableCell<Notification, String> cell = new TableCell<>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.setWrappingWidth(titleColumn.getWidth() - 10); // Adatta alla colonna
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        // Imposta il wrapping per la colonna messaggio
        messageColumn.setCellFactory(tc -> {
            TableCell<Notification, String> cell = new TableCell<>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.setWrappingWidth(messageColumn.getWidth() - 10); // Adatta alla colonna
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        centerTextInColumn(timeColumn);

        loadNews();

        timeColumn.setSortType(TableColumn.SortType.DESCENDING);
        newsTable.getSortOrder().add(timeColumn);
        newsTable.sort();
    }

    private void loadNews() {
        SessionManager sessionManager = SessionManager.getInstance();
        List<Notification> news = newsDAO.getAllBarberNews(sessionManager.getCurrentUser().getEmail());
        ObservableList<Notification> observableList = FXCollections.observableArrayList(news);
        newsTable.setItems(observableList);
    }

    private <T> void centerTextInColumn(TableColumn<Notification, T> column) {
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

            Stage stage = (Stage) newsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goToServicesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Services.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) newsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Service");
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

            Stage stage = (Stage) newsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Send Comunication");
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

            Stage stage = (Stage) newsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
