public class OBSObjectTest2 {
    public static void main(String[] args) throws InterruptedException {
        OBSCommunication.createOBSRemoteController("localhost", "4455", "DHts1kjsVdweSCgv");
        OBSCommunication.setScene("test");
        Thread.sleep(1000);

        OBSTHObject test = new OBSTHObject("test", "Image", 80);

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

    }
}
