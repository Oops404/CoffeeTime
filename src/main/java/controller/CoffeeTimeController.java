package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * @Author CheneyJin
 * @Time 2019-04-17-12:53
 * @Email cheneyjin@outlook.com
 */
public class CoffeeTimeController implements Initializable {
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

    public void loadConfigButtonOnClicked() {
        System.out.println("load");
    }

    public void saveConfigButtonOnClicked() {
        System.out.println("save");
    }

    public void guideButtonOnClicked() {
        System.out.println("guide");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
