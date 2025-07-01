import io.obswebsocket.community.client.model.SceneItem;

/**
 * This holds the information that connects a specific user to a specific talking head
 */
public class OBSTHObject {
    public static final int STATE_NOT_TALKING = 0;
    public static final int STATE_TALKING = 1;

    private float pos;
    private int talkingState;
    private Number sceneItemId;

    /**
     * Creates an OBS Talking Head Object
     * @param sceneItemId The item id of the talking head (this will likely change to the source name)
     * @param position The default starting position (not talking) of a talking head (this will likely go away)
     */
    public OBSTHObject(Number sceneItemId, float position) {
        talkingState = STATE_NOT_TALKING;
        this.sceneItemId = sceneItemId;
        pos = position;
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
        if (talkingState == STATE_NOT_TALKING) {
            // obs request to lower talking head
            OBSCommunication.getController().setSceneItemTransform(OBSCommunication.getScene(), sceneItemId, SceneItem.Transform.builder().positionY(pos).build(), 0);
        } else if (talkingState == STATE_TALKING) {
            // obs request to raise talking head
            System.out.println(OBSCommunication.getScene());
            System.out.println(sceneItemId);

            OBSCommunication.getController().setSceneItemTransform(OBSCommunication.getScene(), sceneItemId, SceneItem.Transform.builder().positionY((pos-80)).build(), 0);
        }
    }
}
