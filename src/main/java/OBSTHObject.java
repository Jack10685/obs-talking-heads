//import com.fasterxml.jackson.annotation.JsonValue;
import io.obswebsocket.community.client.model.SceneItem;
import net.dv8tion.jda.api.entities.User;

/**
 * This holds the information that connects a specific user to a specific talking head
 */
public class OBSTHObject {
    public static final int STATE_NOT_TALKING = 0;
    public static final int STATE_TALKING = 1;

    private int offset;
    private float pos;
    private int talkingState;
    public Number sceneItemId;
    private User user;
    private String itemName;


    /**
     * Links member to talking head in OBS
     * @param id the id of the person in discord
     * @param itemName The name of the item in the OBS scene
     * @param offset how much to move the talking head
     */
    public OBSTHObject(String id, String itemName, int offset) {
        talkingState = STATE_NOT_TALKING;
        OBSCommunication.getController().getSceneItemId(OBSCommunication.getScene(), itemName, 0, res -> {
            sceneItemId = res.getSceneItemId();
            OBSCommunication.getController().getSceneItemTransform(OBSCommunication.getScene(), sceneItemId, res2 -> pos = res2.getSceneItemTransform().getPositionY());
        });
        this.offset = offset;
        user = DiscordCommunication.findUserById(id);
        this.itemName = itemName;
    }



    /**
     * used to get new scene item id and position of a talking head
     */
    public void refresh() {
        OBSCommunication.getController().getSceneItemId(OBSCommunication.getScene(), itemName, 0, res -> {
            sceneItemId = res.getSceneItemId();
            OBSCommunication.getController().getSceneItemTransform(OBSCommunication.getScene(), sceneItemId, res2 -> {
                pos = res2.getSceneItemTransform().getPositionY();
                if (talkingState == STATE_TALKING) {
                    pos += offset;
                }
            });
        });
    }

    /**
     * returns the Item name in the OBS scene
     * @return item name
     */
    public String getOBSItemName() {
        return itemName;
    }

    /**
     * Used to adjust the offset for the talking head
     * @param offset new offset to move talking head
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * returns the offset for the talking head
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * gets the discord user associated with this object
     * @return the discord user
     */
    public User getUser() {
        return user;
    }

    /**
     * sets the scene item id of the talking head (this will likely go away)
     * @param sceneItemId The item id of the talking head
     */
    public void setSceneItemId(int sceneItemId) {
        this.sceneItemId = sceneItemId;
    }

    /**
     * used to change the talking state
     * @param state the talking state to change to, options are either STATE_TALKING or STATE_NOT_TALKING
     */
    public void setTalkingState(int state) {
        if (talkingState != state) {
            talkingState = state;
            updateState();
        }
    }

    /**
     * called by setTalkingState, this makes sure updates are only made when the state changes.
     * (ie, if state is STATE_TALKING and setTalkingState(STATE_TALKING) is called, this doesn't do anything)
     */
    private void updateState() {
        if (sceneItemId != null) {
            if (talkingState == STATE_NOT_TALKING) {
                // obs request to lower talking head
                OBSCommunication.getController().setSceneItemTransform(OBSCommunication.getScene(), sceneItemId, SceneItem.Transform.builder().positionY(pos).build(), 0);
            } else if (talkingState == STATE_TALKING) {
                // obs request to raise talking head
                System.out.println(OBSCommunication.getScene());
                System.out.println(sceneItemId);

                OBSCommunication.getController().setSceneItemTransform(OBSCommunication.getScene(), sceneItemId, SceneItem.Transform.builder().positionY((pos - offset)).build(), 0);
            }
        }
    }
}
