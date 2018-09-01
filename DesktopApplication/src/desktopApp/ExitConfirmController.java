package desktopApp;

/**
 * Created by user on 01/09/2018.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExitConfirmController {

    private Stage stage;

    @FXML
    private VBox Outer_ConfirmExit_VBox;

    @FXML
    private Label ConfirmExit_ExitMessageLabel;

    @FXML
    private HBox Inner_ConfirmExit_HBox;

    @FXML
    private Button No_Button;

    @FXML
    private Button Yes_Button;

    @FXML
    public void exitConfirmNoButtonPressed(ActionEvent event) {
        this.stage.close();
    }

    @FXML
    public void exitConfirmYesButtonPressed(ActionEvent event) {
        System.exit(0);
    }

    public void setStage(Stage stage) { this.stage = stage; }

}
