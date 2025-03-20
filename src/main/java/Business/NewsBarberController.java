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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewsBarberController implements Initializable {


    @FXML
    private TableColumn<Notification, String> message_col;

    @FXML
    private TableView<Notification> news_table;

    @FXML
    private TableColumn<Notification, String > title_col;

    private NewsDAO newsDAO = new ConcreteNewsDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
        message_col.setCellValueFactory(new PropertyValueFactory<>("message"));

        title_col.setReorderable(false);
        message_col.setReorderable(false);

        news_table.setSelectionModel(null);

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

            Stage stage = (Stage) news_table.getScene().getWindow();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Service.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) news_table.getScene().getWindow();
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

            Stage stage = (Stage) news_table.getScene().getWindow();
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

            Stage stage = (Stage) news_table.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Appointments");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
