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
        URL url = App.class.getResource("/resources/desktopApp.fxml");

        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        Controller controller = fxmlLoader.getController();
        controller.setApplication();
        controller.setPrimarySatge(primaryStage);

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
