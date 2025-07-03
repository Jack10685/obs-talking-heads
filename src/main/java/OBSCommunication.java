import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.model.SceneItem;

import java.io.IOException;
import java.util.List;

public class OBSCommunication {
    private static OBSRemoteController controller;
    private static String scene;

    private static boolean ready = false;

    public static void createOBSRemoteController(String host, String port, String password) {
        String uHost = host;
        if (host == null) {
            uHost = "localhost";
        }

        int uPort;
        if (Integer.getInteger(port) == null) {
            uPort = 4455;
        } else {
            uPort = Integer.getInteger(port);
        }
        controller = OBSRemoteController.builder()
            .host(uHost)                        // Default host
            .port(uPort)                        // Default port
            .password(password)                 // Provide your password here
            .connectionTimeout(0)       // Seconds the client will wait for OBS to respond
            .lifecycle().onReady(new SetReady())
            .and()
            .build();
        controller.connect();
    }

    public static List<SceneItem> getItemsInScene() {
        if (scene == null) {
            scene = "test";
        }
        return controller.getSceneItemList(scene, 3).getSceneItems();
    }

    public static boolean isReady() {
        return ready;
    }

    private static void setReady() {
        ready = true;
    }

    public static OBSRemoteController getController() {
        return controller;
    }

    public static void setScene(String scene) {
        OBSCommunication.scene = scene;
    }

    public static String getScene() {
        return scene;
    }

    private static class SetReady implements Runnable {

        @Override
        public void run() {
            setReady();
            LoginController.loginSuccessful();
            System.out.println("Ready");
        }
    }

}
