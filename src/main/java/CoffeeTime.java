import controller.CoffeeTimeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author CheneyJin
 * @time 2019-04-17-11:59
 * @email cheneyjin@outlook.com
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

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("CoffeeTime closed.");
            primaryStage.close();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
