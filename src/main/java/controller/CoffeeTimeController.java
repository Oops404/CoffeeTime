package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.ConfigUtil;
import util.Fishing;

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

    private ObservableList<String> logContentList;

    public CoffeeTimeController() {
        logContentList = FXCollections.observableArrayList();
    }

    public void showLogs() {
        logList = new ListView<>(logContentList);
        logList.setItems(logContentList);
        logList.getSelectionModel().selectionModeProperty().addListener(new ChangeListener<SelectionMode>() {
            @Override
            public void changed(ObservableValue<? extends SelectionMode> observable, SelectionMode oldValue, SelectionMode newValue) {
                String value = logList.getSelectionModel().getSelectedItem();
                logContentList.add(value);
            }
        });
    }

    public void loadConfigButtonOnClicked() {
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
                fishing = new Fishing(configUtil);
                System.out.println(1 / 0);
                fishing.auto();
            } catch (Exception e) {
                e.printStackTrace();
                logContentList.add("read settings failure.");
            }
        }
    }

    public void saveConfigButtonOnClicked() {
        System.out.println("save");
    }

    public void guideButtonOnClicked() {
        System.out.println("guide");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showLogs();
    }
}
