package lk.AM.Client1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/View/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> System.exit(1));
        stage.show();
    }
}
