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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import sound.SoundPlayer;
import sound.Tone;
import util.ConfigUtil;
import util.FileUtil;
import util.Fishing;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    private Button startButton;
    @FXML
    private Button guideButton;
    @FXML
    private Button googleGirl;
    @FXML
    private TableView<ConfigItem> configTable;
    @FXML
    private TableColumn<ConfigItem, String> configKeyCol;
    @FXML
    private TableColumn<ConfigItem, Integer> configValueCol;
    @FXML
    private TableColumn<ConfigItem, String> configNoteCol;
    @FXML
    private ListView<String> logList;

    private Fishing fishing;
    private ObservableList<ConfigItem> propsList;

    private byte[] soundGuide = new Tone("mp3/guide.mp3").getMusic();
    private byte[] googleGirlShutup = new Tone("mp3/shut_up.mp3").getMusic();
    private byte[] googleGirlSpeak = new Tone("mp3/speak.mp3").getMusic();

    public void close() {
        fishing.close();
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
        configNoteCol = new TableColumn<>();
        configTable.getColumns().add(configKeyCol);
        configTable.getColumns().add(configValueCol);
        configTable.getColumns().add(configNoteCol);
        configTable.setEditable(true);
        anchorPane.getChildren().add(configTable);

        fishing = new Fishing();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");
        configKeyCol.setCellValueFactory(new PropertyValueFactory<>("configKey"));
        configValueCol.setCellValueFactory(new PropertyValueFactory<>("configValue"));
        configNoteCol.setCellValueFactory(new PropertyValueFactory<>("configNote"));

        configValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        configValueCol.setOnEditCommit(
                event -> {
                    int newValue = event.getNewValue();
                    ConfigItem item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                    item.setConfigValue(newValue);
                    ConfigUtil.changeAttribute(fishing.getProperties(), item.getConfigKey(), item.getConfigValue());
                    System.out.println("change ConfigItem in list: " +
                            event.getTableView().getItems().get(event.getTablePosition().getRow()));
                    System.out.println("update fishing props turn to: " + fishing.getProperties());
                    logList.getItems().add(0, "update props " + item.getConfigKey()
                            + "'s value to " + item.getConfigValue() + ".");
                }
        );
        configNoteCol.setCellFactory(TextFieldTableCell.forTableColumn());
        configNoteCol.setOnEditCommit(
                event -> {
                    String newValue = event.getNewValue();
                    System.out.println(newValue);
                    if (null != newValue && !"".equals(newValue)) {
                        ConfigItem item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                        item.setConfigNote(newValue);
                        logList.getItems().add(0,
                                "update " + item.getConfigKey() + "'s note: " + newValue + ". ");
                    }
                }
        );
        propsList = FXCollections.observableArrayList();
        configTable.setItems(propsList);
    }

    public void loadConfigButtonOnClicked() {
        if (fishing != null) {
            fishing.close();
        }
        FileChooser fileChooser = settingsChooser();
        File file = fileChooser.showOpenDialog(appStage());
        if (file == null) {
            logList.getItems().add(0, "no settings choose.");
            return;
        }
        try {
            ConfigUtil configUtil = new ConfigUtil(file);
            fishing.loadProperties(configUtil);
            Fishing.Properties pros = fishing.getProperties();
            if (propsList.size() > 0) {
                propsList.clear();
            }
            propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_X, pros.loadBarX,
                    configUtil.getAnnotation(Fishing.Properties.LOAD_BAR_X)));
            propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_Y, pros.loadBarY,
                    configUtil.getAnnotation(Fishing.Properties.LOAD_BAR_Y)));
            propsList.add(new ConfigItem(Fishing.Properties.SPRAY_STEP, pros.sprayStep,
                    configUtil.getAnnotation(Fishing.Properties.SPRAY_STEP)));
            propsList.add(new ConfigItem(Fishing.Properties.TARGET_THRESHOLD, pros.targetThreshold,
                    configUtil.getAnnotation(Fishing.Properties.TARGET_THRESHOLD)));
            propsList.add(new ConfigItem(Fishing.Properties.TARGET_ALIGN_X, pros.targetAlignX,
                    configUtil.getAnnotation(Fishing.Properties.TARGET_ALIGN_X)));
            propsList.add(new ConfigItem(Fishing.Properties.TARGET_ALIGN_Y, pros.targetAlignY,
                    configUtil.getAnnotation(Fishing.Properties.TARGET_ALIGN_Y)));
            propsList.add(new ConfigItem(Fishing.Properties.TARGET_SIZE, pros.targetSize,
                    configUtil.getAnnotation(Fishing.Properties.TARGET_SIZE)));

            propsList.add(new ConfigItem(Fishing.Properties.LH, pros.lh,
                    configUtil.getAnnotation(Fishing.Properties.LH)));
            propsList.add(new ConfigItem(Fishing.Properties.LS, pros.ls,
                    configUtil.getAnnotation(Fishing.Properties.LS)));
            propsList.add(new ConfigItem(Fishing.Properties.LV, pros.lv,
                    configUtil.getAnnotation(Fishing.Properties.LV)));
            propsList.add(new ConfigItem(Fishing.Properties.HH, pros.hh,
                    configUtil.getAnnotation(Fishing.Properties.HH)));
            propsList.add(new ConfigItem(Fishing.Properties.HS, pros.hs,
                    configUtil.getAnnotation(Fishing.Properties.HS)));
            propsList.add(new ConfigItem(Fishing.Properties.HV, pros.hv,
                    configUtil.getAnnotation(Fishing.Properties.HV)));
            propsList.add(new ConfigItem(Fishing.Properties.GET_FISH_ALIGN_X, pros.getFishAlignX,
                    configUtil.getAnnotation(Fishing.Properties.GET_FISH_ALIGN_X)));
            propsList.add(new ConfigItem(Fishing.Properties.GET_FISH_ALIGN_Y, pros.getFishAlignY,
                    configUtil.getAnnotation(Fishing.Properties.GET_FISH_ALIGN_Y)));
            propsList.add(new ConfigItem(Fishing.Properties.MASK_DEBUG, ConfigUtil.bool2Int(pros.maskDebug),
                    configUtil.getAnnotation(Fishing.Properties.MASK_DEBUG)));
            propsList.add(new ConfigItem(Fishing.Properties.CATCH_DEBUG, ConfigUtil.bool2Int(pros.catchDebug),
                    configUtil.getAnnotation(Fishing.Properties.CATCH_DEBUG)));
            propsList.add(new ConfigItem(Fishing.Properties.LOAD_BAR_DEBUG, ConfigUtil.bool2Int(pros.loadBarDebug),
                    configUtil.getAnnotation(Fishing.Properties.LOAD_BAR_DEBUG)));
            propsList.add(new ConfigItem(Fishing.Properties.SPRAY_AREA_DEBUG, ConfigUtil.bool2Int(pros.sprayAreaDebug),
                    configUtil.getAnnotation(Fishing.Properties.SPRAY_AREA_DEBUG)));
            logList.getItems().add(0, "load settings succeed.");
        } catch (Exception e) {
            e.printStackTrace();
            logList.getItems().add(0, "read settings failure.");
        }
    }

    public void saveConfigButtonOnClicked() {
        System.out.println("save");
        FileChooser fileChooser = settingsChooser();
        fileChooser.setTitle("Save Settings.");
        File file = fileChooser.showSaveDialog(appStage());
        if (file == null) {
            logList.getItems().add(0, "no settings file saved.");
            return;
        }
        try (BufferedWriter fileWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
            for (ConfigItem item : propsList) {
                fileWriter.write(item.toString());
            }
            fileWriter.flush();
            logList.getItems().add(0, "save settings succeed.");
        } catch (IOException e) {
            e.printStackTrace();
            logList.getItems().add(0, "save settings failure.");
        }
    }

    public void guideButtonOnClicked() {
        new SoundPlayer(soundGuide).start();
        logList.getItems().add(0, "4. 关闭电脑显示器，音箱，键盘鼠标...只需要保证网络稳定且主机工作即可。");
        logList.getItems().add(0, "3. 关闭电脑所有不必要的软件和后台程序（实在不能关闭的请最小化），只保留魔兽世界。");
        logList.getItems().add(0, "2. 魔兽世界画面高级设置,限制前台帧数（60），后台帧数（50）。");
        logList.getItems().add(0, "1. 魔兽世界画面设置,全部低，但保留水体细节（普通/优良），粒子密度（最高），轮廓线（高）。");
        logList.getItems().add(0, "[迭代版本 0.4Version] 羊羊羊(oﾟvﾟ)ノ省电教程↓:");

    }

    public void actionStart() {
        logList.getItems().add(0, "start fishing monitor.");
        fishing.auto();
    }

    public void googleGirlOral() {
        Fishing.isOralable = !Fishing.isOralable;
        if (Fishing.isOralable) {
            new SoundPlayer(googleGirlSpeak).start();
            googleGirl.setText("闭嘴谷歌娘");
        } else {
            new SoundPlayer(googleGirlShutup).start();
            googleGirl.setText("张嘴谷歌娘");
        }
    }

    private Stage appStage() {
        Scene scene = this.borderPane.getScene();
        Window window = scene.getWindow();
        return (Stage) window;
    }

    private FileChooser settingsChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter
                = new FileChooser.ExtensionFilter("ini file (*.ini)", "*.ini");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File(FileUtil.getWorkFolder()));
        return fileChooser;
    }
}
