package Business;

import Model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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



        newsTable.setSelectionModel(null);

        SceneNavigator.setTextWrapping(titleColumn, 10);
        SceneNavigator.setTextWrapping(messageColumn, 10);

        SceneNavigator.centerTextInColumn(timeColumn);
        SceneNavigator.setColumnsNotReorderable(titleColumn, messageColumn, timeColumn);

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