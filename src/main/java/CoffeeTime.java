import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.ConfigUtil;

import java.io.File;

/**
 * @Author CheneyJin
 * @Time 2019-04-17-11:59
 * @Email cheneyjin@outlook.com
 */
public class CoffeeTime extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CoffeeTime.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Coffee Time");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pics/fishing.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
