import io.obswebsocket.community.client.model.SceneItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import net.dv8tion.jda.api.entities.User;

public class PanelController {

    @FXML
    private FlowPane OBSImageBar;

    @FXML
    private FlowPane discordUserBar;

    @FXML
    private Label userLabel;

    @FXML
    private Label elementLabel;
    @FXML
    private PasswordField tokenField;
    @FXML
    private TextField joinCommandField;
    @FXML
    private TextField leaveCommandField;
    @FXML
    private TextField sceneField;

    private Integer itemID;
    @FXML
    private void addTalkingHead() {

    }



    /**
     * called by LocalStorage.loadSettings() if a settings.ini file exists
     * @param settings value holding class for transferring everything in one variable
     */
    public void setSettings(LocalStorage.SettingsStore settings) {
        if (settings.token != null)
            tokenField.setText(settings.token);
        if (settings.joinCommand != null)
            joinCommandField.setText(settings.joinCommand);
        if (settings.leaveCommand != null)
            leaveCommandField.setText(settings.leaveCommand);
        if (settings.scene != null)
            sceneField.setText(settings.scene);
        //TODO: add default shift setter (once that's linked and figured out)
    }

    /**
     * creates the talking head from the selected discord user and OBS scene item
     */
    @FXML
    private void createTalkingHead() {
        //TODO: implement adding new OBSTHObject into Main.links
    }

    /**
     * deleted all talking heads, deletes save file of talking heads (unimplemented)
     */
    @FXML
    private void deleteAllTalkingHeads() {
        //TODO: first implement talking head GUI functionality, then implement this delete all functionality
    }

    /**
     * in the event that scene ids change (reordering, deleting and readding, OBS being weird), this will realign the user to the correct talking head
     */
    @FXML
    private void refreshTalkingHeads() {
        //TODO: refresh functionality
    }

    /**
     * activates when pressing "save" button on settings panel, saves settings
     */
    @FXML
    private void saveSettings() {
        LocalStorage.SettingsStore settings = new LocalStorage.SettingsStore();
        if (!tokenField.getText().isBlank()) {
            if (!DiscordCommunication.checkToken(tokenField.getText())) {
                DiscordCommunication.setBotToken(tokenField.getText());
            }
            settings.token = tokenField.getText();
        }
        if (!sceneField.getText().isBlank()) {
            OBSCommunication.setScene(sceneField.getText());
            settings.scene = sceneField.getText();
        }
        if (!joinCommandField.getText().isBlank()) {
            DiscordCommunication.setJoinCommand(joinCommandField.getText());
            settings.joinCommand = joinCommandField.getText();
        }
        if (!leaveCommandField.getText().isBlank()) {
            DiscordCommunication.setLeaveCommand(leaveCommandField.getText());
            settings.leaveCommand = leaveCommandField.getText();
        }
        LocalStorage.saveSettings(settings);
    }

    /**
     * resets settings, does not remove discord token or change scene (no default values for these)
     */
    @FXML
    private void resetDefault() {
        DiscordCommunication.setJoinCommand("!thjoin");
        DiscordCommunication.setLeaveCommand("!thleave");
        joinCommandField.setText("!thjoin");
        leaveCommandField.setText("!thleave");
        //TODO: figure out spinner, implement resetting default
    }

    /**
     * repulls all of the currently speaking discord users and items in the OBS scene
     */
    @FXML
    private void refresh() {
        // remove all elements in left and right panes
        OBSImageBar.getChildren().removeAll(OBSImageBar.getChildren());
        discordUserBar.getChildren().removeAll(discordUserBar.getChildren());
        // get connected discord users and populate left pane
        for (User user : DiscordCommunication.getTalkingUsers()) {
            Button b = new Button(user.getEffectiveName());
            b.setPrefWidth(Button.USE_COMPUTED_SIZE);
            b.setMaxWidth(1.7976931348623157E308);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    userLabel.setText(b.getText());
                }
            });

            discordUserBar.getChildren().add(b);
        }
        // get all images in OBS scene
        for (SceneItem item : OBSCommunication.getItemsInScene()) {
            Button b = new Button(item.getSourceName());
            b.setPrefWidth(Button.USE_COMPUTED_SIZE);
            b.setMaxWidth(1.7976931348623157E308);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    elementLabel.setText(b.getText());
                    itemID = item.getSceneItemId();
                }
            });

            OBSImageBar.getChildren().add(b);
        }
        // reset user and element labels
        userLabel.setText("User");
        elementLabel.setText("Element");
        itemID = null;
    }
}
