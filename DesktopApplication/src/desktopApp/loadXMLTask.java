package desktopApp;

import engine.GameFactory;
import engine.GameLogic;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Created by user on 25/08/2018.
 */
public class loadXMLTask extends Task<Boolean> {
    private GameFactory gameFactory;
    private String fileName;
    private int sleepTime;

    public loadXMLTask(GameFactory gameFactory, String fileName) {
        this.gameFactory = gameFactory;
        this.fileName = fileName;
        this.sleepTime = 700;
    }

    @Override
    public Boolean call() throws Exception {
        updateMessage("Loading File");
        updateProgress(20, 100);
        Thread.sleep(this.sleepTime);

        updateProgress(50, 100);
        Thread.sleep(this.sleepTime);

        updateMessage("Validating File.");
        updateProgress(70, 100);
        Thread.sleep(this.sleepTime);

        try {
            updateProgress(90, 100);
            Thread.sleep(this.sleepTime);
            //GameLogic newGame = gameFactory.getNewGame(this.fileName);
            gameFactory.loadSettingsFile(this.fileName);
            updateProgress(100, 100);
            updateMessage("Finished loading XML file");
            Thread.sleep(this.sleepTime * 2);
            return true;
        }
        catch (Exception e) { return false; }
    }
}

