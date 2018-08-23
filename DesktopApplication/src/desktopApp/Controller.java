package desktopApp;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import engine.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller {
    private GameLogic currentGameLogic;
    private GameFactory gameFactory;
    private BooleanProperty isValidXML;
    private BooleanProperty isReplayMode;
    private Stage primaryStage;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private ScrollPane MainPanel_ScrollPane;
    @FXML private BorderPane MainPanel_BorderPane;
    @FXML private ScrollPane CenterPanel_ScrollPane;
    @FXML private GridPane CenterPanel_boardArea_GridPane;
    @FXML private ScrollPane LeftPanel_ScrollPane;
    @FXML private VBox LeftPanel_players_VBox;
    @FXML private Label LeftPanel_playersLabel_Label;
    @FXML private TableView<?> LeftPanel_playersTable_TableView;
    @FXML private TableColumn<?, ?> LeftPanel_playerID_TableColumn;
    @FXML private TableColumn<?, ?> LeftPanel_playerMoves_TableColumn;
    @FXML private TableColumn<?, ?> LeftPanel_playerMoves_TableColumnLeftPanel_playerColour_TableColumn;
    @FXML private TableColumn<?, ?> LeftPanel_playerMoves_TableColumnLeftPanel_playerType_TableColumn;
    @FXML private Label LeftPanel_movesHistory_Label;
    @FXML private TableView<?> LeftPanel_movesHistory_TableView;
    @FXML private TableColumn<?, ?> LeftPanel_moveID_TableColumn;
    @FXML private TableColumn<?, ?> LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumn;
    @FXML private TableColumn<?, ?> LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumnLeftPanel_moveID_TableColumnLeftPanel_moveColumn_TableColumn;
    @FXML private ScrollPane LeftPanel_replay_ScrollPane;
    @FXML private HBox LeftPanel_replay_HBox;
    @FXML private Button LeftPanel_toggleReplay_Button;
    @FXML private Button LeftPanel_replayLeftArrow_Button;
    @FXML private Button LeftPanel_replayRightArrow_Button;
    @FXML private ScrollPane TopPanel_ScrollPane;
    @FXML private VBox TopPanel_VBox;
    @FXML private Label TopPanel_welcome_Label;
    @FXML private HBox TopPanel_buttons_HBox;
    @FXML private Button TopPanel_loadXML_Button;
    @FXML private Button TopPanel_resignPlayer_Button;
    @FXML private Button TopPanel_exitGame_Button;
    @FXML private Button TopPanel_playRound_Button;
    @FXML private Button TopPanel_endRound_Button;

    @FXML
    void initialize() {
        assert MainPanel_ScrollPane != null : "fx:id=\"MainPanel_ScrollPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert MainPanel_BorderPane != null : "fx:id=\"MainPanel_BorderPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert CenterPanel_ScrollPane != null : "fx:id=\"CenterPanel_ScrollPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert CenterPanel_boardArea_GridPane != null : "fx:id=\"CenterPanel_boardArea_GridPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_ScrollPane != null : "fx:id=\"LeftPanel_ScrollPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_players_VBox != null : "fx:id=\"LeftPanel_players_VBox\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playersLabel_Label != null : "fx:id=\"LeftPanel_playersLabel_Label\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playersTable_TableView != null : "fx:id=\"LeftPanel_playersTable_TableView\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playerID_TableColumn != null : "fx:id=\"LeftPanel_playerID_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playerMoves_TableColumn != null : "fx:id=\"LeftPanel_playerMoves_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playerMoves_TableColumnLeftPanel_playerColour_TableColumn != null : "fx:id=\"LeftPanel_playerMoves_TableColumnLeftPanel_playerColour_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playerMoves_TableColumnLeftPanel_playerType_TableColumn != null : "fx:id=\"LeftPanel_playerMoves_TableColumnLeftPanel_playerType_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_movesHistory_Label != null : "fx:id=\"LeftPanel_movesHistory_Label\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_movesHistory_TableView != null : "fx:id=\"LeftPanel_movesHistory_TableView\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveID_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumnLeftPanel_moveID_TableColumnLeftPanel_moveColumn_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumnLeftPanel_moveID_TableColumnLeftPanel_moveColumn_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_replay_ScrollPane != null : "fx:id=\"LeftPanel_replay_ScrollPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_replay_HBox != null : "fx:id=\"LeftPanel_replay_HBox\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_toggleReplay_Button != null : "fx:id=\"LeftPanel_toggleReplay_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_replayLeftArrow_Button != null : "fx:id=\"LeftPanel_replayLeftArrow_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_replayRightArrow_Button != null : "fx:id=\"LeftPanel_replayRightArrow_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_ScrollPane != null : "fx:id=\"TopPanel_ScrollPane\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_VBox != null : "fx:id=\"TopPanel_VBox\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_welcome_Label != null : "fx:id=\"TopPanel_welcome_Label\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_buttons_HBox != null : "fx:id=\"TopPanel_buttons_HBox\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_loadXML_Button != null : "fx:id=\"TopPanel_loadXML_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_resignPlayer_Button != null : "fx:id=\"TopPanel_resignPlayer_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_exitGame_Button != null : "fx:id=\"TopPanel_exitGame_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_playRound_Button != null : "fx:id=\"TopPanel_playRound_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert TopPanel_endRound_Button != null : "fx:id=\"TopPanel_endRound_Button\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
    }

    public Controller() {
        gameFactory = new GameFactory();
        isValidXML = new SimpleBooleanProperty();
        isValidXML.setValue(false);
        isReplayMode = new SimpleBooleanProperty();
        isReplayMode.set(false);
    }

    public void setApplication() {
        TopPanel_loadXML_Button.setDisable(false);
        TopPanel_playRound_Button.setDisable(true);
        TopPanel_endRound_Button.setDisable(true);
        TopPanel_resignPlayer_Button.setDisable(true);
        TopPanel_exitGame_Button.setDisable(false);

        TopPanel_playRound_Button.disableProperty().bind(isValidXML);
        TopPanel_endRound_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty().not());
        TopPanel_resignPlayer_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty().not());
        LeftPanel_toggleReplay_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty().not());
        LeftPanel_replayRightArrow_Button.disableProperty().bind(isReplayMode.not());
        LeftPanel_replayLeftArrow_Button.disableProperty().bind(isReplayMode.not());

    }

    private void createBoard() {
        CenterPanel_boardArea_GridPane = new GridPane();
        int cols = currentGameLogic.getCols();
        int rows = currentGameLogic.getRows();

        CenterPanel_boardArea_GridPane.setAlignment(Pos.CENTER);
        CenterPanel_boardArea_GridPane.setHgap(0);
        CenterPanel_boardArea_GridPane.setVgap(0);

        addColButtons(cols);
        addBoardCells(rows, cols);
        if (currentGameLogic.isPopout()) {
            addPopoutButtons(cols, rows);
        }
    }

    private void addBoardCells(int cols, int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < cols; j++ ) {
                Circle c = createNewDisc();

                CenterPanel_boardArea_GridPane.setRowIndex(c, i);
                CenterPanel_boardArea_GridPane.setColumnIndex(c, j);
                CenterPanel_boardArea_GridPane.getChildren().add(c);
            }
        }

    }

    private void addPopoutButtons(int cols, int rows) {
        for (int i = 0; i < cols; i++) {
            ImageView image = new ImageView("/resources/downArrow.jpeg");
            image.setFitHeight(25);
            image.setFitWidth(25);

            Button b_out = createNewArrowButton(i, image);

            CenterPanel_boardArea_GridPane.setRowIndex(b_out,rows + 1);
            CenterPanel_boardArea_GridPane.setColumnIndex(b_out, i);
            CenterPanel_boardArea_GridPane.getChildren().add(b_out);
        }
    }

    private Button createNewArrowButton(int col, ImageView img) {
        Button b = new Button("", img);

        return b;
    }

    private void addColButtons(int cols) {
        for (int i = 0; i < cols; i++) {
            ImageView image = new ImageView("/resources/downArrow.jpeg");
            image.setFitHeight(25);
            image.setFitWidth(25);
            Button b_in = createNewArrowButton(i, image);

            CenterPanel_boardArea_GridPane.setRowIndex(b_in,0);
            CenterPanel_boardArea_GridPane.setColumnIndex(b_in, i);
            CenterPanel_boardArea_GridPane.getChildren().add(b_in);
        }
    }


    private Circle createNewDisc() {
        Circle c = new Circle();
        c.setRadius(25);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.BLACK);

        return c;
    }

    void setDiscInCol(int col, int playerId) {

    }

    @FXML
    void endRound_onButtonAction(ActionEvent event) {

    }

    @FXML
    void exitGame_onButtonAction(ActionEvent event) {

    }

    void setPrimarySatge(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void loadNewSettingFile_onButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Settings File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files","*.xml"));
        File settingsFile = fileChooser.showOpenDialog(primaryStage);
        if (settingsFile==null)
            return;

        while (true) {
            try {
                currentGameLogic = gameFactory.getNewGame(settingsFile.getAbsolutePath().toString());
                createBoard();
                break;
            }
            catch (Exception e) {
                //TODO: popout message of invalid settings file
            }
        }

    }

    @FXML
    void playRound_onButtonAction(ActionEvent event) {

    }

    @FXML
    void playerResign_onButtonAction(ActionEvent event) {

    }

}
