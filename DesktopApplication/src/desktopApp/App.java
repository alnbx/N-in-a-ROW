package desktopApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello There in FXML");

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/desktopApp/resources/desktopApp.fxml");

        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        desktopAppController controller = fxmlLoader.getController();
        controller.setApplication();
        controller.setPrimarySatge(primaryStage);

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
