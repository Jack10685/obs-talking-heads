import javafx.stage.Stage;

public class Main {
    public static Stage stage;

    /**
     * For some reason, having Login.main() be the main method throws an error, having this be the main method that calls Login.main() fixes it. I don't know why.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Login.main(args);
    }
}