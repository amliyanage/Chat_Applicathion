package lk.AM.MSG;

import javafx.scene.control.Label;

public class SendMsgFormController {
    public Label msg;

    public void setData(String message) {
        msg.setText(message);
    }
}
