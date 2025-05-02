package Controllers;

import Business.NewsService;
import Helpers.SceneHelper;
import Model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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
    private TableColumn<Notification, LocalDate> dateColumn;

    @FXML
    private TableColumn<Notification, String > titleColumn;



    private NewsService newsService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(newsService == null) {
            newsService = new NewsService();
        }
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        newsTable.setSelectionModel(null);

        SceneHelper.setTextWrapping(titleColumn, 10);
        SceneHelper.setTextWrapping(messageColumn, 10);

        SceneHelper.centerTextInColumns(timeColumn, dateColumn);
        SceneHelper.setColumnsNotReorderable(titleColumn, messageColumn, timeColumn, dateColumn);

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
    private void goToProfileView() {
        SceneHelper.switchScene(newsTable, "/View/ProfileBarber.fxml", "Profile");
    }

    @FXML
    private void goToServicesView() {
        SceneHelper.switchScene(newsTable, "/View/Services.fxml", "Services");
    }

    @FXML
    private void goToSendComunicationView() {
        SceneHelper.switchScene(newsTable, "/View/SendComunication.fxml", "Send Comunication");
    }

    @FXML
    private void goToAppointmentsView() {
        SceneHelper.switchScene(newsTable, "/View/AppointmentsBarber.fxml", "Appointments");
    }

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

}
