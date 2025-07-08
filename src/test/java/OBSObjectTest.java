public class OBSObjectTest {
    public static void main(String[] args) throws InterruptedException {
        OBSCommunication.createOBSRemoteController("localhost", "4455", "DHts1kjsVdweSCgv");
        OBSCommunication.setScene("test");
        Thread.sleep(1000);
        OBSCommunication.getController().getSceneItemId("test", "Image", 0,
                res -> {
                    System.out.println(res.toString());
                    System.out.println(res.getSceneItemId());
                    OBSTHObject test = new OBSTHObject("test", "Image", 873);
                    test.sceneItemId = 1;
                    try {
                        while (true) {
                            System.out.println("loop");
                            test.setTalkingState(OBSTHObject.STATE_TALKING);
                            Thread.sleep(1000);
                            test.setTalkingState(OBSTHObject.STATE_NOT_TALKING);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        });

    }

}
