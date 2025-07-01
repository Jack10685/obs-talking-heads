import io.obswebsocket.community.client.OBSRemoteController;

import java.util.*;

public class OBSConnectionTest {
    public static void main(String[] args) throws InterruptedException {
        OBSCommunication.createOBSRemoteController("<host>", "<port>", "<OBS password here>");
        Thread.sleep(3000);
        OBSCommunication.getController().disconnect();
        System.exit(0);
    }

}
