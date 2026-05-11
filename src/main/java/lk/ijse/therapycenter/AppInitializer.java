package lk.ijse.therapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.therapycenter.config.HibernateUtil;



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
    }

    @Override
    public void stop() {

        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}