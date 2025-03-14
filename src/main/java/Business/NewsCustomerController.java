package Business;

import Authentication.SessionManager;
import DBconnection.DAO.ConcreteNewsDAO;
import DBconnection.DAO.ConcreteUserDAO;
import DBconnection.DAO.NewsDAO;
import DBconnection.DAO.UserDAO;
import Model.Notification;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewsCustomerController implements Initializable {

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
                        text.setWrappingWidth(message_col.getWidth() - 10);
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

    @FXML
    private void goToAppointmentsAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) chair_icon.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("AppointmentsCustomer");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfileAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileCustomer.fxml"));
            Parent loginRoot = loader.load();

            ProfileCustomerController controller = loader.getController();
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null) {
                controller.profileAction(
                        currentUser.getName(),
                        currentUser.getSurname(),
                        currentUser.getEmail(),
                        currentUser.getPhone());
            }

            Stage stage = (Stage) chair_icon.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("ProfileCustomer");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}