import bean.ConfigItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.ConfigUtil;

import java.io.File;

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
    public void start(Stage primaryStage) throws Exception {
        configTable = new TableView<>();
        configKeyCol = new TableColumn<>("Key");
        configValueCol = new TableColumn<>("Value");

        configKeyCol.setCellValueFactory(new PropertyValueFactory<>("configKey"));
        configValueCol.setCellValueFactory(new PropertyValueFactory<>("configValue"));
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
        list.add(new ConfigItem("value", "123"));

        configTable.setItems(list);
        configTable.getItems().add(new ConfigItem("value2", "1233"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
