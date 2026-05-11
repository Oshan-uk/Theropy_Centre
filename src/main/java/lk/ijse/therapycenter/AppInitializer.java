package lk.ijse.therapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.therapycenter.config.FactoryConfiguration;



public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/Login.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Serenity Mental Health Therapy Center");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.isFullScreen();
    }

    @Override
    public void stop() {

        FactoryConfiguration.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}