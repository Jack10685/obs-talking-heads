import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    /**
     * Opens Login screen, should not be called except by JavaFX.Application
     * @param stage passed in from Application.launch()
     */
   @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        stage.setTitle("Talking Heads");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("login.fxml"))));
        stage.show();
    }

    /**
     * runs JavaFX.Application.launch()
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
