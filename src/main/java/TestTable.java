import bean.ConfigItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @Author CheneyJin
 * @Time 2019-04-17-11:59
 * @Email cheneyjin@outlook.com
 */
public class TestTable extends Application {

    private TableView<ConfigItem> configTable;
    private TableColumn<ConfigItem, String> configKeyCol;
    private TableColumn<ConfigItem, String> configValueCol;

    @Override
    public void start(Stage primaryStage) {
        configTable = new TableView<>();
        configKeyCol = new TableColumn<>("Key");
        configValueCol = new TableColumn<>("Value");

        configKeyCol.setCellValueFactory(new PropertyValueFactory<>("configKey"));
        configValueCol.setCellValueFactory(new PropertyValueFactory<>("configValue"));
//        configKeyCol.setCellValueFactory(features -> features.getValue().configKeyProperty());
//        configValueCol.setCellValueFactory(features -> features.getValue().configValueProperty());
        configTable.getColumns().add(configKeyCol);
        configTable.getColumns().add(configValueCol);
        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(configTable);
        primaryStage.setTitle("Table View Sample");
        primaryStage.setWidth(350);
        primaryStage.setHeight(350);
        primaryStage.setScene(scene);
        primaryStage.show();

        ObservableList<ConfigItem> list = FXCollections.observableArrayList();
        list.add(new ConfigItem("value", 123));

        configTable.setItems(list);
        configTable.getItems().add(new ConfigItem("value2", 1233));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
