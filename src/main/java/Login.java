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
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Main.stage = stage;

        Scene login = new Scene(root);
        stage.setTitle("Talking Heads");
        stage.setScene(login);
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
