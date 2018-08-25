package desktopApp;

/**
 * Created by user on 25/08/2018.
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class xmlLoadingController {

    @FXML
    private VBox wrapping_vbox;

    @FXML
    private HBox Loading_hbox;

    @FXML
    private Label LoadingHbox_loadingFile_Label;

    @FXML
    private Label LoadingHbox_fileName_Label1;

    @FXML
    private HBox statusHbox;

    @FXML
    private Label statusHbox_status_label;

    @FXML
    private Label statusHbox_statusLabel_label;

    @FXML
    private HBox ProgressHbox;

    @FXML
    private Label ProgressHbox_Progress_label;

    @FXML
    private ProgressBar ProgressHbox_Progress_progressBar;

    public void updateFileName(String fileName) { LoadingHbox_fileName_Label1.setText(fileName); }

    public Label getStatusHbox_statusLabel_label() {
        return statusHbox_statusLabel_label;
    }

    public ProgressBar getProgressHbox_Progress_progressBar() {
        return ProgressHbox_Progress_progressBar;
    }
}
