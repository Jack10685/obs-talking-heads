import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.model.SceneItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBSCommunication {
    private static OBSRemoteController controller;
    private static String scene;

    private static boolean ready = false;

    /**
     * creates OBSRemoteController object from connection settings
     * @param host host ip/address of OBS instance
     * @param port port OBS websocket is using
     * @param password password to log in
     */
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
            .connectionTimeout(3)       // Seconds the client will wait for OBS to respond
            .lifecycle().onReady(new SetReady())
            .and()
            .build();
        controller.connect();
    }

    /**
     * gets a List of all the items in a scene (Images, videos, etc)
     * @return list of all scene items, empty list if no scene set
     */
    public static List<SceneItem> getItemsInScene() {
        if (scene == null) {
            return List.of();
        }
        return controller.getSceneItemList(scene, 3).getSceneItems();
    }

    /**
     * returns whether the OBSRemoteController is ready to receive commands
     * @return true/false if controller can receive commands
     */
    public static boolean isReady() {
        return ready;
    }

    /**
     * called by OBSRemoteController once connection established and ready for commands
     */
    private static void setReady() {
        ready = true;
    }

    /**
     * returns the controller to run commands
     * @return the OBSRemoteController object
     */
    public static OBSRemoteController getController() {
        return controller;
    }

    /**
     * sets the scene to look for and animate talking heads
     * @param scene  name of the scene
     */
    public static void setScene(String scene) {
        OBSCommunication.scene = scene;
    }

    /**
     * returns the currently set scene,
     * @return the scene, null if no scene
     */
    public static String getScene() {
        return scene;
    }

    /**
     * runnable class used by OBSRemoteControl to set when it is ready to receive commands
     */
    private static class SetReady implements Runnable {

        @Override
        public void run() {
            setReady();
            LoginController.loginSuccessful();
            System.out.println("Ready");
        }
    }

}
