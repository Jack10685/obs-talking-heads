import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        System.out.println("test");
    }

    /**
     * called by OBSCommunication once login is successful, replaces Login screen with main screen
     */
    public static void loginSuccessful() {

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
