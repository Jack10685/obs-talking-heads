import io.obswebsocket.community.client.model.SceneItem;

public class OBSTHObject {
    public static final int STATE_NOT_TALKING = 0;
    public static final int STATE_TALKING = 1;

    private float pos;
    private int talkingState;
    private Number sceneItemId;
    public OBSTHObject(Number sceneItemId, float position) {
        talkingState = STATE_NOT_TALKING;
        this.sceneItemId = sceneItemId;
        pos = position;
    }

    public void setSceneItemId(int sceneItemId) {
        this.sceneItemId = sceneItemId;
    }

    public void setTalkingState(int state) {
        if (talkingState != state) {
            talkingState = state;
            updateState();
        }
    }

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
