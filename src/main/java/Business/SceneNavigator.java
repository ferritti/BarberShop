package Business;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public abstract class SceneNavigator {

    public static void switchScene(Node node, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <S, T> void centerTextInColumn(TableColumn<S, T> column) {
        column.setCellFactory(tc -> new TableCell<S, T>() {
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

    @SafeVarargs
    public static <S> void setColumnsNotReorderable(TableColumn<S, ?>... columns) {
        for (TableColumn<S, ?> column : columns) {
            column.setReorderable(false);
        }
    }

    @SafeVarargs
    public static <S> void centerTextInColumns(TableColumn<S, ?>... columns) {
        for (TableColumn<S, ?> column : columns) {
            centerTextInColumn(column);
        }
    }

    public static <S> void setTextWrapping(TableColumn<S, String> column, double padding) {
        column.setCellFactory(tc -> {
            TableCell<S, String> cell = new TableCell<>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.setWrappingWidth(column.getWidth() - padding);
                        setGraphic(text);
                    }
                }
            };

            column.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                if (cell.getGraphic() != null) {
                    Text t = (Text) cell.getGraphic();
                    t.setWrappingWidth(newWidth.doubleValue() - padding);
                }
            });

            return cell;
        });
    }
}