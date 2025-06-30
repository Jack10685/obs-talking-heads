import io.obswebsocket.community.client.OBSRemoteController;

public class OBSCommunication {
    private static OBSRemoteController controller;
    private static String scene;

    private static boolean ready = false;

    public static void createOBSRemoteController(String password) {
        controller = OBSRemoteController.builder()
            .host("localhost")                  // Default host
            .port(4455)                         // Default port
            .password(password)   // Provide your password here
            .connectionTimeout(0)               // Seconds the client will wait for OBS to respond
            .lifecycle().onReady(new SetReady())
            .and()
            .build();
        controller.connect();
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
            System.out.println("Ready");
        }
    }

}
