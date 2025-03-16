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
import java.util.List;
import java.util.ResourceBundle;

public class NewsCustomerController implements Initializable {

    @FXML
    private TableView<Notification> newsTable;

    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TableColumn<Notification, String > titleColumn;

    private NewsDAO newsDAO = new ConcreteNewsDAO();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        titleColumn.setReorderable(false);
        messageColumn.setReorderable(false);

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

        loadNews();
    }

    private void loadNews() {
        SessionManager sessionManager = SessionManager.getInstance();
        List<Notification> news = newsDAO.getAllNews(sessionManager.getCurrentUser().getUserType());
        ObservableList<Notification> observableList = FXCollections.observableArrayList(news);
        newsTable.setItems(observableList);
    }

    @FXML
    private void goToAppointmentsAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentsCustomer.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) newsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfileAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProfileCustomer.fxml"));
            Parent root = loader.load();

            ProfileCustomerController controller = loader.getController();
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
}