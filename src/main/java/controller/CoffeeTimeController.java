package controller;

import bean.ConfigItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane anchorPane;
    @FXML
    private Button loadConfigButton;
    @FXML
    private Button saveConfigButton;
    @FXML
    private Button guideButton;
    @FXML
    private TableView<ConfigItem> configTable;
    @FXML
    private TableColumn<ConfigItem, String> configKeyCol;
    @FXML
    private TableColumn<ConfigItem, String> configValueCol;
    @FXML
    private ListView<String> logList;

    private Fishing fishing;
    private ObservableList<ConfigItem> propsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");
        configKeyCol.setCellValueFactory(new PropertyValueFactory<>("configKey"));
        configValueCol.setCellValueFactory(new PropertyValueFactory<>("configValue"));
        propsList = FXCollections.observableArrayList();
        configTable.setItems(propsList);
    }

    public CoffeeTimeController() {
        System.out.println("Constructor");
        borderPane = new BorderPane();
        anchorPane = new AnchorPane();
        logList = new ListView<>();
        borderPane.getChildren().add(logList);
        borderPane.getChildren().add(anchorPane);

        configTable = new TableView<>();
        configKeyCol = new TableColumn<>();
        configValueCol = new TableColumn<>();
        configTable.getColumns().add(configKeyCol);
        configTable.getColumns().add(configValueCol);

        anchorPane.getChildren().add(configTable);

        fishing = new Fishing();
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

                propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_X, pros.loadBarX));
                propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_Y, pros.loadBarY));
                propsList.add(new ConfigItem(Fishing.Properties.SPRAY_STEP, pros.sprayStep));
                propsList.add(new ConfigItem(Fishing.Properties.TARGET_THRESHOLD, pros.targetThreshold));
                propsList.add(new ConfigItem(Fishing.Properties.TARGET_ALIGN_X, pros.targetAlignX));
                propsList.add(new ConfigItem(Fishing.Properties.TARGET_ALIGN_Y, pros.targetAlignY));
                propsList.add(new ConfigItem(Fishing.Properties.TARGET_SIZE, pros.targetSize));

                propsList.add(new ConfigItem(Fishing.Properties.LH, pros.lh));
                propsList.add(new ConfigItem(Fishing.Properties.LS, pros.ls));
                propsList.add(new ConfigItem(Fishing.Properties.LV, pros.lv));
                propsList.add(new ConfigItem(Fishing.Properties.HH, pros.hh));
                propsList.add(new ConfigItem(Fishing.Properties.HS, pros.hs));
                propsList.add(new ConfigItem(Fishing.Properties.HV, pros.hv));
                propsList.add(new ConfigItem(Fishing.Properties.GET_FISH_ALIGN_X, pros.getFishAlignX));
                propsList.add(new ConfigItem(Fishing.Properties.GET_FISH_ALIGN_Y, pros.getFishAlignY));
                propsList.add(new ConfigItem(Fishing.Properties.MASK_DEBUG, ConfigUtil.bool2Int(pros.maskDebug)));
                propsList.add(new ConfigItem(Fishing.Properties.CATCH_DEBUG, ConfigUtil.bool2Int(pros.catchDebug)));
                propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_DEBUG, ConfigUtil.bool2Int(pros.loadBarDebug)));
                propsList.add(new ConfigItem(Fishing.Properties.SPRAY_AREA_DEBUG, ConfigUtil.bool2Int(pros.sprayAreaDebug)));
                
                logList.getItems().add(0, "load settings succeed.");
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

}
