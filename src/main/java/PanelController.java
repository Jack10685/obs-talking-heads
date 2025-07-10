import io.obswebsocket.community.client.model.SceneItem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.WindowEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

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
    @FXML
    private Spinner<Integer> offsetSpinner;
    @FXML
    private Spinner<Integer> defaultOffsetSpinner;
    @FXML
    private TilePane talkingHeadPane;

    private String selectedId;

    /**
     * called once FXML elements loaded, used to load settings, initialize spinners, etc
     */
    @FXML
    public void initialize() {
        Main.stage.setResizable(false);
        Main.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                OBSCommunication.getController().disconnect();
                DiscordCommunication.shutdown();
                System.exit(0);
            }
        });
        Main.controller = this;
        offsetSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 80, 1));
        defaultOffsetSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 80, 1));
        LocalStorage.loadSettings();
    }

    /**
     * loads the talking heads save file, retrieves relevant info from DiscordCommunication, the updates GUI
     */
    public void loadTalkingHeads() {
        LocalStorage.loadTalkingHeads();
        refreshTalkingHeads();
    }

    /**
     * saves the current talking heads configuration to JSON file
     */
    @FXML
    public void saveTalkingHeads() {
        LocalStorage.saveTalkingHeads();
    }

    /**
     * called by LocalStorage.loadSettings() if a settings.ini file exists
     * @param settings value holding class for transferring everything in one variable
     */
    public void setSettings(LocalStorage.SettingsStore settings) {
        if (settings.token != null) {
            tokenField.setText(settings.token);
            DiscordCommunication.setBotToken(settings.token);
        }
        if (settings.joinCommand != null) {
            joinCommandField.setText(settings.joinCommand);
            DiscordCommunication.setJoinCommand(settings.joinCommand);
        }
        if (settings.leaveCommand != null) {
            leaveCommandField.setText(settings.leaveCommand);
            DiscordCommunication.setLeaveCommand(settings.leaveCommand);
        }
        if (settings.scene != null) {
            sceneField.setText(settings.scene);
            OBSCommunication.setScene(settings.scene);
        }
        if (settings.shift != null) {
            defaultOffsetSpinner.getValueFactory().setValue(Integer.parseInt(settings.shift));
            offsetSpinner.getValueFactory().setValue(Integer.parseInt(settings.shift));
        }
    }

    /**
     * creates the talking head from the selected discord user and OBS scene item, triggers a refresh
     */
    @FXML
    private void createTalkingHead() {
        Main.links.add(new OBSTHObject(selectedId, elementLabel.getText(), offsetSpinner.getValue()));
        refreshTalkingHeads();
        refreshSidePanels();
    }

    /**
     * deleted all talking heads, deletes save file of talking heads (unimplemented)
     */
    @FXML
    private void deleteAllTalkingHeads() {
        Main.links.removeAll(Main.links);
        LocalStorage.deleteTalkingHeadsSave();
        refreshTalkingHeads();
    }

    /**
     * in the event that scene ids change (reordering, deleting and readding, OBS being weird), this will realign the user to the correct talking head
     */
    @FXML
    private void refreshTalkingHeads() {
        talkingHeadPane.getChildren().removeAll(talkingHeadPane.getChildren());

        VBox general = new VBox();
        general.setAlignment(Pos.CENTER);
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (OBSTHObject heads : Main.links) {
                    heads.refresh();
                }
                refreshTalkingHeads();
            }
        });
        refresh.setMaxWidth(Double.MAX_VALUE);
        refresh.setMaxHeight(Double.MAX_VALUE);
        Button save = new Button("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalStorage.saveTalkingHeads();
            }
        });
        save.setMaxWidth(Double.MAX_VALUE);
        save.setMaxHeight(Double.MAX_VALUE);

        Button addManually = new Button("Add Manually");
        addManually.setMaxWidth(Double.MAX_VALUE);
        addManually.setMaxHeight(Double.MAX_VALUE);

        general.getChildren().add(refresh);
        general.getChildren().add(save);
        general.getChildren().add(addManually);

        talkingHeadPane.getChildren().add(general);


        //TODO: change this to open a second window to make more edits
        for (OBSTHObject heads : Main.links) {
            VBox headContainer = new VBox();
            headContainer.setAlignment(Pos.CENTER);
            headContainer.setBackground(Background.fill(Paint.valueOf("lightgrey")));

            Label discordName;
            if (heads.getUser() != null)
                discordName = new Label(heads.getUser().getEffectiveName());
            else
                discordName = new Label("unknown");
            Label to = new Label("to");
            Label OBSItem = new Label(heads.getOBSItemName());
            Label offsetLabel = new Label("Offset:");
            Spinner<Integer> offset = new Spinner<>();
            offset.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, heads.getOffset()));
            offset.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        heads.setOffset(offset.getValue());
                    }
                }
            });
            Button saveOffset = new Button("Update Offset");
            saveOffset.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    heads.setOffset(offset.getValue());
                }
            });

            Button delete = new Button("Delete");
            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.links.remove(heads);
                }
            });

            headContainer.getChildren().add(discordName);
            headContainer.getChildren().add(to);
            headContainer.getChildren().add(OBSItem);
            headContainer.getChildren().add(offsetLabel);
            headContainer.getChildren().add(offset);
            headContainer.getChildren().add(saveOffset);
            headContainer.getChildren().add(delete);

            talkingHeadPane.getChildren().add(headContainer);

        }
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
        settings.shift = defaultOffsetSpinner.getValue().toString();
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
        defaultOffsetSpinner.getValueFactory().setValue(80);
        offsetSpinner.getValueFactory().setValue(80);
    }

    /**
     * repulls all of the currently speaking discord users and items in the OBS scene, resets linking elements
     */
    @FXML
    private void refreshSidePanels() {
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
                    selectedId = user.getId();
                }
            });

            discordUserBar.getChildren().add(b);
        }
        // get all images in OBS scene
        OBSCommunication.getItemsInScene();
        // reset user and element labels
        userLabel.setText("User");
        elementLabel.setText("Element");
        selectedId = null;
        offsetSpinner.getValueFactory().setValue(defaultOffsetSpinner.getValue());
    }

    public void addOBSItemButtons(List<SceneItem> items)  {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (SceneItem item : items) {
                    Button b = new Button(item.getSourceName());
                    b.setPrefWidth(Button.USE_COMPUTED_SIZE);
                    b.setMaxWidth(1.7976931348623157E308);
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            elementLabel.setText(b.getText());
                        }
                    });

                    OBSImageBar.getChildren().add(b);
                }
            }
        });

    }
}
