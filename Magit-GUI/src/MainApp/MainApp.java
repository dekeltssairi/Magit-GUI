package MainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
        primaryStage.setTitle("Magit");
        primaryStage.setScene(new Scene(root, 748, 518));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
