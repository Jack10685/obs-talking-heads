import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField password;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private Label hostLabel;
    @FXML
    private Label portLabel;

    /**
     * Process login, calls OBSCommunication.createOBSRemoteController()
     */
    @FXML
    private void doLogin() {
        OBSCommunication.createOBSRemoteController(host.getText(), port.getText(), password.getText());
    }

    /**
     * called by OBSCommunication once login is successful, replaces Login screen with main screen
     */
    public static void loginSuccessful() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.stage.hide();
                try {
                    Main.stage.setScene(new Scene(FXMLLoader.load(LoginController.class.getResource("controlpanel.fxml"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Main.stage.show();
            }
        });
    }

    /**
     * By default, host and port are hidden and will not be shown unless user clicks "View more options" button.
     * This toggles their visibility.
     * They default to "localhost" and "4455" respectively
     */
    @FXML
    private void doShowMore() {
        if (host.isVisible()) {
            host.setVisible(false);
            port.setVisible(false);
            hostLabel.setVisible(false);
            portLabel.setVisible(false);
        } else {
            host.setVisible(true);
            port.setVisible(true);
            hostLabel.setVisible(true);
            portLabel.setVisible(true);
        }
    }
}
