package desktopApp;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import common.MoveType;
import common.PlayerTypes;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import engine.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class desktopAppController {
    private GameLogic currentGameLogic;
    private GameFactory gameFactory;
    private SimpleBooleanProperty isValidXML;
    private SimpleIntegerProperty isReplayMode;
    private Stage primaryStage;
    private List<PlayerDisplay> players;

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ScrollPane MainPanel_ScrollPane;
    @FXML
    private BorderPane MainPanel_BorderPane;
    @FXML
    private ScrollPane CenterPanel_ScrollPane;
    @FXML
    private GridPane CenterPanel_boardArea_GridPane;
    @FXML
    private ScrollPane LeftPanel_ScrollPane;
    @FXML
    private VBox LeftPanel_players_VBox;

    @FXML
    private Label LeftPanel_playersLabel_Label;
    @FXML
    private TableView<PlayerDisplay> LeftPanel_playersTable_TableView;
    @FXML
    private TableColumn<PlayerDisplay, Integer> LeftPanel_playerID_TableColumn;
    @FXML
    private TableColumn<PlayerDisplay, String> LeftPanel_playerName_TableColumn;
    @FXML
    private TableColumn<PlayerDisplay, Integer> LeftPanel_playerMoves_TableColumn;
    @FXML
    private TableColumn<PlayerDisplay, String> LeftPanel_playerColour_TableColumn;
    @FXML
    private TableColumn<PlayerDisplay, PlayerTypes> LeftPanel_playerType_TableColumn;

    @FXML
    private Label LeftPanel_movesHistory_Label;
    @FXML
    private TableView<Move> LeftPanel_movesHistory_TableView;
    @FXML
    private TableColumn<Move, Integer> LeftPanel_moveID_TableColumn;
    @FXML
    private TableColumn<Move, MoveType> LeftPanel_moveType_TableColumn;
    @FXML
    private TableColumn<Move, Integer> LeftPanel_moveColumn_TableColumn;

    @FXML
    private ScrollPane LeftPanel_replay_ScrollPane;
    @FXML
    private HBox LeftPanel_replay_HBox;
    @FXML
    private Button LeftPanel_toggleReplay_Button;
    @FXML
    private Button LeftPanel_replayLeftArrow_Button;
    @FXML
    private Button LeftPanel_replayRightArrow_Button;
    @FXML
    private ScrollPane TopPanel_ScrollPane;
    @FXML
    private VBox TopPanel_VBox;
    @FXML
    private Label TopPanel_welcome_Label;
    @FXML
    private HBox TopPanel_buttons_HBox;
    @FXML
    private Button TopPanel_loadXML_Button;
    @FXML
    private Button TopPanel_resignPlayer_Button;
    @FXML
    private Button TopPanel_exitGame_Button;
    @FXML
    private Button TopPanel_playRound_Button;
    @FXML
    private Button TopPanel_endRound_Button;

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
        assert LeftPanel_playerColour_TableColumn != null : "fx:id=\"LeftPanel_playerMoves_TableColumnLeftPanel_playerColour_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_playerType_TableColumn != null : "fx:id=\"LeftPanel_playerMoves_TableColumnLeftPanel_playerType_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_movesHistory_Label != null : "fx:id=\"LeftPanel_movesHistory_Label\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_movesHistory_TableView != null : "fx:id=\"LeftPanel_movesHistory_TableView\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveID_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveType_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
        assert LeftPanel_moveColumn_TableColumn != null : "fx:id=\"LeftPanel_moveID_TableColumnLeftPanel_moveType_TableColumnLeftPanel_moveID_TableColumnLeftPanel_moveColumn_TableColumn\" was not injected: check your FXML file '/resources/desktopApp.fxml'.";
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

    public desktopAppController() {
        gameFactory = new GameFactory();
        isValidXML = new SimpleBooleanProperty();
        isValidXML.setValue(false);
        isReplayMode = new SimpleIntegerProperty();
        isReplayMode.setValue(0);
    }

    public void setApplication() {
        TopPanel_loadXML_Button.setDisable(false);
        TopPanel_playRound_Button.setDisable(true);
        TopPanel_endRound_Button.setDisable(true);
        TopPanel_resignPlayer_Button.setDisable(true);
        TopPanel_exitGame_Button.setDisable(false);

        TopPanel_loadXML_Button.disableProperty().bind(Bindings.selectBoolean(isValidXML));
        TopPanel_playRound_Button.disableProperty().bind(TopPanel_loadXML_Button.disabledProperty().not());
        TopPanel_endRound_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty());
        TopPanel_resignPlayer_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty());
        LeftPanel_toggleReplay_Button.disableProperty().bind(TopPanel_playRound_Button.disabledProperty());
        LeftPanel_replayRightArrow_Button.disableProperty().bind(isReplayMode.isEqualTo(0).not());
        LeftPanel_replayLeftArrow_Button.disableProperty().bind(isReplayMode.isEqualTo(0).not());

        LeftPanel_playerID_TableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        LeftPanel_playerName_TableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LeftPanel_playerColour_TableColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        LeftPanel_playerMoves_TableColumn.setCellValueFactory(new PropertyValueFactory<>("numMoves"));
        LeftPanel_playerType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        LeftPanel_moveID_TableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        LeftPanel_moveType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        LeftPanel_moveColumn_TableColumn.setCellValueFactory(new PropertyValueFactory<>("col"));

        Callback<TableColumn<PlayerDisplay, String>, TableCell<PlayerDisplay, String>> colorCellFactory =
                new Callback<TableColumn<PlayerDisplay, String>, TableCell<PlayerDisplay, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        TableCell cell = new TableCell<PlayerDisplay, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                setText("");
                                int row = getTableRow().getIndex() + 1;
                                String id = row > currentGameLogic.getNumberOfPlayersToInitialized() ?
                                        "" : "player" + row;
                                setId(id);
                            }

                            private String getString() {
                                return getItem() == null ? "" : getItem();
                            }
                        };

                        return cell;
                    }
                };

        LeftPanel_playerColour_TableColumn.setCellFactory(colorCellFactory);
    }

    private void createBoard() {
        CenterPanel_boardArea_GridPane = new GridPane();
        int cols = currentGameLogic.getCols();
        int rows = currentGameLogic.getRows();

        CenterPanel_boardArea_GridPane.setAlignment(Pos.CENTER);
        CenterPanel_boardArea_GridPane.setHgap(0);
        CenterPanel_boardArea_GridPane.setVgap(0);

        addColButtons(cols);
        addBoardCells(cols, rows);
        if (currentGameLogic.isPopout()) {
            addPopoutButtons(cols, rows);
        }

        MainPanel_BorderPane.setCenter(CenterPanel_boardArea_GridPane);
    }

    private void addBoardCells(int cols, int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                Circle c = createNewDisc();

                CenterPanel_boardArea_GridPane.setRowIndex(c, i);
                CenterPanel_boardArea_GridPane.setColumnIndex(c, j);
                CenterPanel_boardArea_GridPane.setMargin(c, new Insets(10, 10, 0, 0));
                CenterPanel_boardArea_GridPane.getChildren().add(c);
            }
        }
    }

    private void addPopoutButtons(int cols, int rows) {
        for (int i = 0; i < cols; i++) {

            ImageView image = new ImageView("/desktopApp/resources/downArrow.png");
            image.setFitHeight(25);
            image.setFitWidth(25);

            Button b_out = createNewArrowButton(i, image);

            CenterPanel_boardArea_GridPane.setRowIndex(b_out, rows + 1);
            CenterPanel_boardArea_GridPane.setColumnIndex(b_out, i);
            CenterPanel_boardArea_GridPane.getChildren().add(b_out);
        }

    }

    private Button createNewArrowButton(int col, ImageView img) {return new Button("", img); }

    private void addColButtons(int cols) {
        for (int i = 0; i < cols; i++) {

            ImageView image = new ImageView("/desktopApp/resources/downArrow.png");
            image.setFitHeight(25);
            image.setFitWidth(25);
            Button b_in = createNewArrowButton(i, image);

            CenterPanel_boardArea_GridPane.setRowIndex(b_in, 0);
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

    public void setDiscInCol(int col, int playerId) {

    }

    @FXML
    public void endRound_onButtonAction(javafx.event.ActionEvent actionEvent) {

    }

    @FXML
    public void exitGame_onButtonAction(javafx.event.ActionEvent actionEvent) {
        TopPanel_welcome_Label.setText("Bye Bye");
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { }
        System.exit(0);

    }

    void setPrimarySatge(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    public void playRound_onButtonAction(javafx.event.ActionEvent actionEvent) { playComputerIfNeeded(); }

    private void playComputerIfNeeded() {
        while ( currentGameLogic.getTypeOfCurrentPlayer() == PlayerTypes.COMPUTER ) {
            currentGameLogic.play(0,currentGameLogic.isPopout());
            //TODO: update board
        }
    }

    @FXML
    public void playerResign_onButtonAction(javafx.event.ActionEvent actionEvent) {

    }

    @FXML
    public void loadNewSettingFile_onButtonAction(javafx.event.ActionEvent actionEvent) {
        File settingsFile;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Settings File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));

        while (true) {
            try {
                settingsFile = fileChooser.showOpenDialog(primaryStage);
                if (settingsFile == null) { throw new Exception(); }
                if (createLoadingTask(settingsFile)) { break; }
                else { throw new Exception(); }
            } catch (Exception e) {
                System.out.println("Error loading file");
                break;
                //TODO: popout message of invalid settings file
            }
        }

        CenterPanel_boardArea_GridPane.visibleProperty().setValue(true);
        isValidXML.setValue(true);
    }

    private boolean createLoadingTask(File settingsFile) {
        try {
            loadXMLTask XMLLoader = new loadXMLTask(gameFactory, settingsFile.getAbsolutePath());
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApp/resources/xmlLoading.fxml");
            fxmlLoader.setLocation(url);
            VBox xmlLoading = fxmlLoader.load(url.openStream());

            Scene secondScene = new Scene(xmlLoading, 800, 350);
            Stage xmlLoadingWindow = new Stage();
            xmlLoadingWindow.setTitle("XML Loading");
            xmlLoadingWindow.setScene(secondScene);
            xmlLoadingWindow.initModality(Modality.WINDOW_MODAL);
            xmlLoadingWindow.initOwner(primaryStage);
            xmlLoadingWindow.setX(primaryStage.getX());
            xmlLoadingWindow.setY(primaryStage.getY() + 200);

            xmlLoadingController controller = fxmlLoader.getController();
            controller.updateFileName(settingsFile.getAbsolutePath());

            Thread loadXMLThread = new Thread(XMLLoader);
            controller.getProgressHbox_Progress_progressBar().progressProperty().bind(XMLLoader.progressProperty());
            controller.getStatusHbox_statusLabel_label().textProperty().bind(XMLLoader.messageProperty());
            xmlLoadingWindow.show();
            loadXMLThread.start();

            XMLLoader.setOnSucceeded((event) ->{
                xmlLoadingWindow.close();
                currentGameLogic = XMLLoader.getValue();
                if (null != currentGameLogic) {

                    currentGameLogic.setRoundFromSettings(true);
                    createBoard();
                    createPlayers();
                }
                else {
                    //todo: show alert
                    //return false;
                }
            });
        }
        catch (Exception e) { return false; }

        return true;
    }

    private void createPlayers() {
        List<Player> players = currentGameLogic.getPlayers();

        LeftPanel_playersTable_TableView.setId("playerOne");

        for (Player p : players) {
            PlayerDisplay playerDisplay = new PlayerDisplay(p);
            LeftPanel_playersTable_TableView.getItems().add(playerDisplay);
        }
    }
}