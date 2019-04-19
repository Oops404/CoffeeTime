package controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.ConfigUtil;
import util.Fishing;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @Author CheneyJin
 * @Time 2019-04-17-12:53
 * @Email cheneyjin@outlook.com
 */
public class CoffeeTimeController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button loadConfigButton;
    @FXML
    private Button saveConfigButton;
    @FXML
    private Button guideButton;
    @FXML
    private TableView configContent;
    @FXML
    private TableColumn property, value;
    @FXML
    private ListView<String> logList;

    private Fishing fishing;

    public CoffeeTimeController() throws AWTException {
        fishing = new Fishing();
        borderPane = new BorderPane();
        logList = new ListView<>();
        borderPane.getChildren().add(logList);
    }

    public void loadConfigButtonOnClicked() {
        if (fishing != null) {
            fishing.close();
        }
        String workingFolder = System.getProperty("user.dir");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter
                = new FileChooser.ExtensionFilter("ini file (*.ini)", "*.ini");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File(workingFolder));

        Scene scene = this.borderPane.getScene();
        Window window = scene.getWindow();
        Stage stage = (Stage) window;
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                ConfigUtil configUtil = new ConfigUtil(file);
                fishing.loadProperties(configUtil);
                Fishing.Properties pros = fishing.getProperties();
            } catch (Exception e) {
                e.printStackTrace();
                logList.getItems().add(0, "read settings failure.");
            }
        } else {
            logList.getItems().add(0, "no settings choose.");
        }
    }

    public void saveConfigButtonOnClicked() {
        System.out.println("save");
    }

    public void guideButtonOnClicked() {
        System.out.println("guide");
    }

    public void actionStart() {
        logList.getItems().add(0, "start fishing monitor.");
        fishing.auto();
    }

    public void actionStop() {
        logList.getItems().add(0, "stop fishing monitor.");
        fishing.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
