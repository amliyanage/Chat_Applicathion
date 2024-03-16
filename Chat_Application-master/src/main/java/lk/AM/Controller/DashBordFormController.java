//package lk.AM.Controller;
//
//import java.io.*;
//import java.net.Socket;
//
//import com.jfoenix.controls.JFXButton;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.stage.FileChooser;
//import lk.AM.MSG.SendMsgFormController;
//import lk.AM.util.ImageAutoCrop;
//
//public class DashBordFormController {
//    public JFXButton btn_send;
//    public TextField filed_msg;
//    public VBox Chat_box;
//
//    private String name;
//
//
//    public void initialize() {
//        Font customFont = Font.loadFont(getClass().getResourceAsStream("/asstes/Front/Roboto/Roboto-Medium.ttf"), 12);
//        btn_send.setFont(customFont);
//        filed_msg.setFont(customFont);
//    }
//
//
//    ImageView image;
//    InputStream inputStream;
//
//    public void importImagebrnOnActhion(MouseEvent mouseEvent) {
//
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files","*.jpg","*jpeg","*png");
//        fileChooser.getExtensionFilters().add(filter);
//        File selectFile = fileChooser.showOpenDialog(null);
//
//        javafx.scene.image.Image TempImage;
//        if (selectFile != null){
//            String path = selectFile.getAbsolutePath();
//            javafx.scene.image.Image image = new Image("file:"+path);
//            ImageView imageView = new ImageView(image);
//            TempImage = image;
//            this.image = new ImageView(image);
//            this.filed_msg.setText(path);
//        }
//    }
//
//    public void sendbtnOnActhion(ActionEvent actionEvent) {
//
//        VBox vBox = new VBox();
//        vBox.setSpacing(30.0);
//        String msg = this.filed_msg.getText();
//
//        if (image != null){
//            ImageView imageView = new ImageView();
//            Image img= ImageAutoCrop.autoCropCenter(new Image(msg),1500,1500);
//            imageView.setImage(img);
//            imageView.setFitWidth(275);
//            imageView.setFitHeight(275);
//            vBox.getChildren().addAll(imageView);
//            image = null;
//            this.filed_msg.clear();
//        }else {
////            Label label = new Label(msg);
////            vBox.getChildren().addAll(label);
//
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MsgForm.fxml"));
//            try {
//                Node loadedNode = fxmlLoader.load();
//                SendMsgFormController load = fxmlLoader.getController();
//                vBox.getChildren().add(loadedNode);
//                load.setData(msg);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        this.Chat_box.getChildren().add(vBox);
//        Chat_box.setSpacing(30);
//    }
//}


package lk.AM.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lk.AM.MSG.ResiveMsgFormController;
import lk.AM.MSG.SendMsgFormController;
import lk.AM.util.ImageAutoCrop;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DashBordFormController {

    private String username;
    public void SetUsername(String userName){
        this.username = userName;
    }

    public JFXButton btn_send;
    public TextField filed_msg;
    public VBox Chat_box;

    private String name;
    private Socket clientSocket;
    private BufferedReader serverReader;
    private PrintWriter writer;

    public void initialize() {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/asstes/Front/Roboto/Roboto-Medium.ttf"), 12);
        btn_send.setFont(customFont);
        filed_msg.setFont(customFont);
        connectToServer(Chat_box);

    }

    ImageView image;
    InputStreamReader inputStream;

    public void importImagebrnOnActhion(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*jpeg", "*png");
        fileChooser.getExtensionFilters().add(filter);
        File selectFile = fileChooser.showOpenDialog(null);

        Image TempImage;
        if (selectFile != null) {
            String path = selectFile.getAbsolutePath();
            Image image = new Image("file:" + path);
            ImageView imageView = new ImageView(image);
            TempImage = image;
            this.image = new ImageView(image);
            this.filed_msg.setText(path);
        }
    }

    public void sendbtnOnActhion(ActionEvent actionEvent) {
        VBox vBox = new VBox();
        vBox.setSpacing(30.0);
        String msg = this.filed_msg.getText();

        if (image != null) {
            ImageView imageView = new ImageView();
            Image img = ImageAutoCrop.autoCropCenter(new Image(msg), 1500, 1500);
            imageView.setImage(img);
            imageView.setFitWidth(275);
            imageView.setFitHeight(275);
            vBox.getChildren().addAll(imageView);
            image = null;
            this.filed_msg.clear();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MsgForm.fxml"));
            try {
                Node loadedNode = fxmlLoader.load();
                SendMsgFormController load = fxmlLoader.getController();
                vBox.getChildren().add(loadedNode);
                load.setData(msg);
                sendMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //this.Chat_box.getChildren().add(vBox);
        Chat_box.setSpacing(30);
    }

    private void sendMessage(String message) {
        message = username+" : "+message;
        writer.println(message);
    }

    private void connectToServer(VBox chatVBox) {
        try {
            Socket socket = new Socket("localhost", 5555);
            Scanner scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Start a new thread to receive messages from the server
            Thread receiveThread = new Thread(() -> {
                while (scanner.hasNextLine()) {
                    String message = scanner.nextLine();
                    // Update UI with received message on JavaFX Application Thread
                    Platform.runLater(() -> updateUIWithMessage(message));
                }
            });
            receiveThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUIWithMessage(String message) {
        VBox vBox = new VBox();
        vBox.setSpacing(30.0);
        int uniqueLetterCount = (int) username.chars().distinct().count();
        String substring = message.substring(0, uniqueLetterCount);

        if (substring.equals(username)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MsgForm.fxml"));
            try {
                Node loadedNode = fxmlLoader.load();
                SendMsgFormController load = fxmlLoader.getController();
                vBox.getChildren().add(loadedNode);
                load.setData(message);
                // Update UI with received message on JavaFX Application Thread
                Platform.runLater(() -> this.Chat_box.getChildren().add(vBox));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ResiveMsgForm.fxml"));
            try {
                Node loadedNode = fxmlLoader.load();
                ResiveMsgFormController load = fxmlLoader.getController();
                vBox.getChildren().add(loadedNode);
                load.setData(message);
                // Update UI with received message on JavaFX Application Thread
                Platform.runLater(() -> this.Chat_box.getChildren().add(vBox));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

