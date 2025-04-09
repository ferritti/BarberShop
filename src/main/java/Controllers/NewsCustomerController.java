package Controllers;

import Business.NewsService;
import Helpers.SceneNavigator;
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

    @FXML
    private TableColumn<Notification, String> dateColumn;

    private final NewsService newsService = new NewsService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        newsTable.setSelectionModel(null);

        SceneNavigator.setTextWrapping(titleColumn, 10);
        SceneNavigator.setTextWrapping(messageColumn, 10);

        SceneNavigator.centerTextInColumns(timeColumn, dateColumn);
        SceneNavigator.setColumnsNotReorderable(titleColumn, messageColumn, timeColumn, dateColumn);

        newsService.deleteOldestNewsIfNecessary();
        loadNews();

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        timeColumn.setSortType(TableColumn.SortType.DESCENDING);

        newsTable.getSortOrder().clear();
        newsTable.getSortOrder().add(dateColumn);
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