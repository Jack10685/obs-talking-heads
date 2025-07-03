import io.obswebsocket.community.client.model.SceneItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private Integer itemID;
    @FXML
    private void addTalkingHead() {

    }

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
