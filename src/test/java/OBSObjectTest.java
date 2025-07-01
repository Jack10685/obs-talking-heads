public class OBSObjectTest {
    public static void main(String[] args) throws InterruptedException {
        OBSCommunication.createOBSRemoteController("<host>", "<port>", "<OBS password here>");
        OBSCommunication.setScene("test");
        Thread.sleep(1000);
        OBSCommunication.getController().getSceneItemId("test", "Image", 0,
                res -> {
                    System.out.println(res.toString());
                    System.out.println(res.getSceneItemId());
                    OBSTHObject test = new OBSTHObject("test", "Image", (float)873);
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
