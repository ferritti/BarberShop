package Business;

import Authentication.SessionManager;
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

public class NewsCustomerController implements Initializable {

    @FXML
    private TableView<Notification> newsTable;

    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TableColumn<Notification, LocalTime> timeColumn;

    @FXML
    private TableColumn<Notification, String > titleColumn;

    private final NewsService newsService = new NewsService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        titleColumn.setReorderable(false);
        messageColumn.setReorderable(false);
        timeColumn.setReorderable(false);

        newsTable.setSelectionModel(null);

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
                        text.setWrappingWidth(titleColumn.getWidth() - 10);
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
                        text.setWrappingWidth(messageColumn.getWidth() - 10);
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        centerTextInColumn(timeColumn);

        newsService.deleteOldestNewsIfNecessary();
        loadNews();

        timeColumn.setSortType(TableColumn.SortType.DESCENDING);
        newsTable.getSortOrder().add(timeColumn);
        newsTable.sort();
    }

    private void loadNews() {
        List<Notification> news = newsService.getNews();
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
    private void goToAppointmentsView() {
        SceneNavigator.switchScene(newsTable, "/View/AppointmentsCustomer.fxml", "Appointments");
    }

    @FXML
    private void goToProfileView() {
        SceneNavigator.switchScene(newsTable, "/View/ProfileCustomer.fxml", "Profile");
    }

    @FXML
    private void goToNewAppointmentView() {
        SceneNavigator.switchScene(newsTable, "/View/NewAppointmentCalendar.fxml", "New Appointment");
    }

}