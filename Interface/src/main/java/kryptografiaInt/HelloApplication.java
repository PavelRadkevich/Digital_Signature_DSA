package kryptografiaInt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 575);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        DSAController hc = new DSAController();
        hc.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}