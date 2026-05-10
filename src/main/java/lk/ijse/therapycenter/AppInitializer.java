package lk.ijse.therapycenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))
        );

        stage.setScene(scene);

        stage.setFullScreen(true);

        stage.setTitle("Therapy Center");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}