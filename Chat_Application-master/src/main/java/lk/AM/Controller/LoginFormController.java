package lk.AM.Controller;

import java.awt.*;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginFormController {

    public TextField Username;
    @FXML
    private JFXButton button;

    @FXML
    void loginBtnOnAction(ActionEvent event) {

        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/DashBordForm.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                DashBordFormController load = fxmlLoader.getController();
                load.SetUsername(Username.getText());
                Scene scene = new Scene(parent);
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setTitle("DashBord");
                stage.centerOnScreen();
                stage.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    public void initialize(){
        Font.loadFont(getClass().getResourceAsStream("/asstes/Front/Roboto"),14);
    }
}
