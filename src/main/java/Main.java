import javafx.stage.Stage;

import java.util.ArrayList;

public class Main {
    public static Stage stage;
    public static ArrayList<OBSTHObject> links;
    public static PanelController controller;

    /**
     * For some reason, having Login.main() be the main method throws an error, having this be the main method that calls Login.main() fixes it. I don't know why.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        links = new ArrayList<>();
        Login.main(args);
    }
}