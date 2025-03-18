package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.NewsDAO;
import DBconnection.DAO.UserDAO;
import Model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewsBarberController implements Initializable {

    @FXML
    private ImageView chair_icon;

    @FXML
    private TableColumn<Notification, String> message_col;

    @FXML
    private ImageView news_icon;

    @FXML
    private TableView<Notification> news_table;

    @FXML
    private ImageView plus_icon;

    @FXML
    private ImageView profile_icon;

    @FXML
    private TableColumn<Notification, String > title_col;

    private NewsDAO newsDAO = new ConcreteNewsDAO();
    private UserDAO userDAO = new ConcreteUserDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
        message_col.setCellValueFactory(new PropertyValueFactory<>("message"));

        title_col.setReorderable(false);
        message_col.setReorderable(false);

        // Imposta il wrapping per la colonna titolo
        title_col.setCellFactory(tc -> {
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
                        text.setWrappingWidth(title_col.getWidth() - 10); // Adatta alla colonna
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        // Imposta il wrapping per la colonna messaggio
        message_col.setCellFactory(tc -> {
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
                        text.setWrappingWidth(message_col.getWidth() - 10); // Adatta alla colonna
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        loadNews();
    }


    private void loadNews() {
        SessionManager sessionManager = SessionManager.getInstance();
        List<Notification> news = newsDAO.getAllNews(sessionManager.getCurrentUser().getUserType());
        ObservableList<Notification> observableList = FXCollections.observableArrayList(news);
        news_table.setItems(observableList);
    }
}