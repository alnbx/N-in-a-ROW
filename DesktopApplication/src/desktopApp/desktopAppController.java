package desktopApp;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import common.GameVariant;
import common.MoveType;
import common.PlayerTypes;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import engine.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.*;
import javafx.util.Callback;
import java.util.concurrent.atomic.AtomicBoolean;
import common.Lock;
import javafx.application.Platform;


public class desktopAppController {
    private GameLogic gameLogic;
    private GameFactory gameFactory;
    private SimpleBooleanProperty isValidXML;
    private SimpleBooleanProperty isRoundOn;
    private SimpleBooleanProperty isReplayMode;
    private Stage primaryStage;
    private int[] topDiscInCols;
    private Map<Integer, Integer> playerIdToPlayerIndex;
    private Boolean xmlLoadedSuccessfully;
    private ObservableList<MoveDisplay> moves = FXCollections.observableArrayList();
    private ObservableList<TogglePlayerDisplayActive> players = FXCollections.observableArrayList();
    private SimpleIntegerProperty currentPlayerID;
    private SimpleBooleanProperty isCurrentPlayerComputer;
    private boolean tieAlert;
    private boolean winAlert;
    private ArrayList<ArrayList<ArrayList<String>>> savedGridPaneStyles;
    private int moveIndexForReplay;
    private HashMap<String, Button> myButtons = new HashMap<>();
    private boolean computerFirst;
    private Stage computerAlert;

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
    private TableView<TogglePlayerDisplayActive> LeftPanel_playersTable_TableView;
    @FXML
    private TableColumn<TogglePlayerDisplayActive, Integer> LeftPanel_playerID_TableColumn;
    @FXML
    private TableColumn<TogglePlayerDisplayActive, String> LeftPanel_playerName_TableColumn;
    @FXML
    private TableColumn<TogglePlayerDisplayActive, Integer> LeftPanel_playerMoves_TableColumn;
    @FXML
    private TableColumn<TogglePlayerDisplayActive, String> LeftPanel_playerColour_TableColumn;
    @FXML
    private TableColumn<TogglePlayerDisplayActive, PlayerTypes> LeftPanel_playerType_TableColumn;

    @FXML
    private Label LeftPanel_movesHistory_Label;
    @FXML
    private TableView<MoveDisplay> LeftPanel_movesHistory_TableView;
    @FXML
    private TableColumn<MoveDisplay, String> LeftPanel_movePlayer_TableColumn;
    @FXML
    private TableColumn<MoveDisplay, MoveType> LeftPanel_moveType_TableColumn;
    @FXML
    private TableColumn<MoveDisplay, Integer> LeftPanel_moveColumn_TableColumn;
    @FXML
    private TableColumn<MoveDisplay, String> LeftPanel_moveTimeStamp_TableColumn;

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
    private HBox TopPanel_MainLabel_HBox;
    @FXML
    private Label TopPanel_welcome_Label;
    @FXML
    private HBox TopPanel_buttons_HBox;
    @FXML
    private Button TopPanel_loadXML_Button;
    @FXML
    private Button TopPanel_playRound_Button;
    @FXML
    private Button TopPanel_endRound_Button;
    @FXML
    private Button TopPanel_resignPlayer_Button;
    @FXML
    private Button TopPanel_exitGame_Button;
    @FXML
    private MenuButton TopPanel_Themes_Button;
    @FXML
    private MenuItem TopPanel_Themes_funTheme_Button;
    @FXML
    private MenuItem TopPanel_Themes_draculaTheme_Button;
    @FXML
    private MenuItem TopPanel_Themes_lightTheme_Button;
    @FXML
    private HBox TopPanel_Info_HBox;
    @FXML
    private HBox TopPanel_GameInfo_HBox;
    @FXML
    private Label TopPanel_GameFile_Label;
    @FXML
    private Label TopPanel_GameFileLabel_Label;
    @FXML
    private HBox TopPanel_GameVariant_HBox;
    @FXML
    private Label TopPanel_GameVariant_Label;
    @FXML
    private Label TopPanel_GameVariantLabel_Label;
    @FXML
    private HBox TopPanel_RoundsPlayed_HBox;
    @FXML
    private Label TopPanel_RoundsPlayed_Label;
    @FXML
    private Label TopPanel_RoundsPlayedLabel_Label;
    @FXML
    void initialize() { TopPanel_RoundsPlayedLabel_Label.setText("0"); }

    public desktopAppController() {
        this.gameFactory = new GameFactory(true);
        this.xmlLoadedSuccessfully = false;
        this.isValidXML = new SimpleBooleanProperty(false);
        this.isReplayMode = new SimpleBooleanProperty(false);
        this.isRoundOn = new SimpleBooleanProperty(false);
        this.isCurrentPlayerComputer = new SimpleBooleanProperty(false);
        this.currentPlayerID = new SimpleIntegerProperty();
        this.tieAlert = false;
        this.winAlert = false;
        this.computerFirst = false;
        this.computerAlert = showComputerIsPlayingAlert();
    }

    public void setApplication() {
        TopPanel_loadXML_Button.setDisable(false);
        TopPanel_playRound_Button.setDisable(true);
        TopPanel_endRound_Button.setDisable(true);
        TopPanel_resignPlayer_Button.setDisable(true);
        TopPanel_exitGame_Button.setDisable(false);

        TopPanel_loadXML_Button.disableProperty().bind(Bindings.selectBoolean(isRoundOn));
        TopPanel_playRound_Button.disableProperty().bind(Bindings.or(isValidXML.not(), isRoundOn));
        TopPanel_endRound_Button.disableProperty().bind(isRoundOn.not());
        LeftPanel_toggleReplay_Button.disableProperty().bind(isRoundOn.not());
        TopPanel_resignPlayer_Button.disableProperty().bind(Bindings.or(isRoundOn.not(), isReplayMode));
        LeftPanel_replayLeftArrow_Button.disableProperty().bind(isReplayMode.not());
        LeftPanel_replayRightArrow_Button.disableProperty().bind(isReplayMode.not());

        LeftPanel_playerID_TableColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        LeftPanel_playerName_TableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LeftPanel_playerColour_TableColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        LeftPanel_playerMoves_TableColumn.setCellValueFactory(new PropertyValueFactory<>("numMoves"));
        LeftPanel_playerType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        LeftPanel_movePlayer_TableColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        LeftPanel_moveType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("moveType"));
        LeftPanel_moveColumn_TableColumn.setCellValueFactory(new PropertyValueFactory<>("col"));
        LeftPanel_moveTimeStamp_TableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        Callback<TableColumn<TogglePlayerDisplayActive, String>, TableCell<TogglePlayerDisplayActive, String>> colorCellFactory =
                new Callback<TableColumn<TogglePlayerDisplayActive, String>, TableCell<TogglePlayerDisplayActive, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        TableCell cell = new TableCell<TogglePlayerDisplayActive, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item != null) {
                                    setText("");
                                    int row = getTableRow().getIndex();
                                    String styleClass = row >= gameLogic.getNumberOfRequiredPlayers() ?
                                            "" : "player" + row;
                                    setId(styleClass);
                                    getStyleClass().add(styleClass);
                                }
                                else {
                                    getStyleClass().clear();
                                }
                            }
                        };

                        return cell;
                    }
                };

        LeftPanel_playerColour_TableColumn.setCellFactory(colorCellFactory);
        playerIdToPlayerIndex = new HashMap<>();
        LeftPanel_replayRightArrow_Button.setId("rightReplayButton");
        LeftPanel_replayLeftArrow_Button.setId("leftReplayButton");
        LeftPanel_playersTable_TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        LeftPanel_playersTable_TableView.setEditable(true);
        LeftPanel_playersTable_TableView.setRowFactory(p -> new ToggleTableRow<>());
    }

    private Stage showComputerIsPlayingAlert()
    {
        Stage computerWindow = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApp/resources/computer.fxml");
            fxmlLoader.setLocation(url);
            AnchorPane computerPlaying = fxmlLoader.load(url.openStream());

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            Scene secondScene = new Scene(computerPlaying,500,500);
            computerWindow.setX(primaryScreenBounds.getMinX());
            computerWindow.setY(primaryScreenBounds.getMinY());
            computerWindow.setWidth(primaryScreenBounds.getWidth());
            computerWindow.setHeight(primaryScreenBounds.getHeight());
            //set window modal for all screen
            computerWindow.initStyle(StageStyle.UNDECORATED);
            computerWindow.initStyle(StageStyle.TRANSPARENT);
            secondScene.setFill(null);
            computerWindow.setScene(secondScene);
            computerWindow.initModality(Modality.APPLICATION_MODAL);
            computerWindow.initOwner(primaryStage);
            //xmlLoadingWindow.setX(primaryStage.getX());
            //xmlLoadingWindow.setY(primaryStage.getY() + 200);
        }
        catch (Exception ignored) { }

        return computerWindow;
    }

    private StackPane getDiscContainerInRowCol(int row, int col)
    {
        ObservableList<Node> gridPaneCells = CenterPanel_boardArea_GridPane.getChildren();
        StackPane res = null;

        for (Node cell : gridPaneCells) {
            if (GridPane.getRowIndex(cell) == row && GridPane.getColumnIndex(cell) == col) {
                res = (StackPane)cell;
                break;
            }
        }

        return res;
    }

    private Node getDiscInRowCol(int row, int col) {
        ObservableList<Node> gridPaneCells = CenterPanel_boardArea_GridPane.getChildren();
        Node res = null;
        StackPane container = null;

        container = getDiscContainerInRowCol(row, col);
        res = container.getChildren().get(0);

//        for (Node cell : gridPaneCells) {
//            if (GridPane.getRowIndex(cell) == row && GridPane.getColumnIndex(cell) == col) {
//                res = cell;
//                break;
//            }
//        }

        return res;
    }

    /********************* THEMES *********************/

    @FXML
    void draculaTheme_onButtonAction(javafx.event.ActionEvent event) {
        MainPanel_BorderPane.getStyleClass().clear();
        MainPanel_BorderPane.getStyleClass().addAll("main-container", "theme-dracula");
    }

    @FXML
    void funTheme_onButtonAction(javafx.event.ActionEvent event) {
        MainPanel_BorderPane.getStyleClass().clear();
        MainPanel_BorderPane.getStyleClass().addAll("main-container", "theme-ocean");
    }

    @FXML
    void lightTheme_onButtonAction(javafx.event.ActionEvent event) {

        MainPanel_BorderPane.getStyleClass().clear();
        MainPanel_BorderPane.getStyleClass().addAll("main-container", "theme-light");
    }

    /**************** CONTROLLER SETUP ****************/

    public void setPrimarySatge(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setOnXButtonPress() {
        this.primaryStage.setOnCloseRequest((WindowEvent e) ->  {
            exitGame_onButtonAction(new javafx.event.ActionEvent());
            e.consume();
        });
    }

    /******************** GAME FLOW *******************/

    @FXML
    public void playRound_onButtonAction(javafx.event.ActionEvent actionEvent) {
        initTopDiscInColsArr();
        this.moves.clear();
        this.isRoundOn.set(true);
        this.gameLogic.setRoundFromSettings(true);
        LeftPanel_playersTable_TableView.getSelectionModel().clearAndSelect(0);
        LeftPanel_playersTable_TableView.refresh();
        if (this.gameLogic.getTypeOfCurrentPlayer() == PlayerTypes.COMPUTER) { this.computerFirst = true; }
        playComputerIfNeeded();
    }

    @FXML
    public void exitGame_onButtonAction(javafx.event.ActionEvent actionEvent) {
        //TopPanel_welcome_Label.setText("Bye Bye?");
        //try { Thread.sleep(1000); }
        //catch (InterruptedException e) { }
        //System.exit(0);

        VBox exitWindow = null;

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/desktopApp/resources/ExitConfirmBox.fxml");
        fxmlLoader.setLocation(url);
        try                 { exitWindow = fxmlLoader.load(url.openStream()); }
        catch (Exception e) { System.exit(0); }

        ExitConfirmController exitController = fxmlLoader.getController();
        Scene secondScene = new Scene(exitWindow, 200, 110);
        secondScene.getStylesheets().add(getClass().getResource("/desktopApp/resources/mainStyle.css").toExternalForm());
        Stage ExitConfirm = new Stage();
        exitController.setStage(ExitConfirm);

        ExitConfirm.setTitle("Exit Confirmation");
        ExitConfirm.setScene(secondScene);
        ExitConfirm.initModality(Modality.WINDOW_MODAL);
        ExitConfirm.initOwner(primaryStage);
        //ExitConfirm.setX(primaryStage.getX() + 300);
        //ExitConfirm.setY(primaryStage.getY() + 200);
        ExitConfirm.showAndWait();
        ExitConfirm.close();
        TopPanel_welcome_Label.setText("Welcome Back!");

    }

    @FXML
    public void endRound_onButtonAction(javafx.event.ActionEvent actionEvent) {
        endRound();
    }

    private void endRound() {
        LeftPanel_movesHistory_TableView.getItems().clear();
        isRoundOn.set(false);
        clearBoard();
        activateAllPlayers();
        zeroizeMovesCountAllPlayers();
        gameLogic.increaseRoundPlayed();
        TopPanel_RoundsPlayedLabel_Label.setText(String.valueOf(gameLogic.getNumberOfRoundsPlayed()));
        LeftPanel_playersTable_TableView.getSelectionModel().clearSelection();
        LeftPanel_playersTable_TableView.refresh();
    }

    private void zeroizeMovesCountAllPlayers() {
        for (TogglePlayerDisplayActive player : players) {
            player.setNumMoves(0);
        }
    }

    private void activateAllPlayers() {
        for (TogglePlayerDisplayActive player : players) {
            player.setActive(true);
        }
    }

    private void clearBoard() {
        ObservableList<Node> gridPaneCells = CenterPanel_boardArea_GridPane.getChildren();
        Node cell = null;

        for (Node sp : gridPaneCells) {
            if (sp instanceof StackPane) {
                cell = ((StackPane) sp).getChildren().get(0);
                if (cell instanceof Circle) {
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("emptyDisc");
                }
            }
        }
    }

    /****************** PLAYER RESIGN *****************/

    @FXML
    public void playerResign_onButtonAction(javafx.event.ActionEvent actionEvent) {
        gameLogic.resignPlayer();

        players.get(playerIdToPlayerIndex.get(currentPlayerID.get())).setActive(false);
        removePlayerDiscsFromBoard(currentPlayerID.get());

        if (gameLogic.getHasWinner()) {
            showWinAlert();
            endRound();
        }

        selectNextPlayer();
    }

    private void removePlayerDiscsFromBoard(int playerId) {
        String playerStyle = "player" + Integer.toString(playerIdToPlayerIndex.get(playerId));
        Node cell = null;
        ObservableList<Node> gridCells = CenterPanel_boardArea_GridPane.getChildren();

        for (Node sp : gridCells) {
            if (sp instanceof StackPane) {
                cell = ((StackPane) sp).getChildren().get(0);
                if (cell instanceof Circle && cell.getStyleClass().contains(playerStyle)) {
                    int bottomRow = GridPane.getRowIndex(cell);
                    int col = GridPane.getColumnIndex(cell);
                    removeSingleDiscFromBoard(gridCells, col, bottomRow);
                }
            }
        }
    }

    // assuming column has at least 1 disc in it (to be removed)
    private void removeSingleDiscFromBoard(ObservableList<Node> gridCells, int col, int bottomRow) {
        Node discAbove = null, discBelow = null;

        while (bottomRow >= topDiscInCols[col]) {
            if (bottomRow == topDiscInCols[col]) {
                Node topDisc = getDiscInRowCol(bottomRow, col);
                if (topDisc != null) {
                    topDisc.getStyleClass().clear();
                    topDisc.getStyleClass().add("emptyDisc");
                }
                break;
            }
            else {
                discAbove = getDiscInRowCol(bottomRow - 1, col);
                discBelow = getDiscInRowCol(bottomRow, col);

                if (discBelow != null) {
                    discBelow.getStyleClass().clear();
                    if (discAbove != null)
                        discBelow.getStyleClass().addAll(discAbove.getStyleClass());
                }

                bottomRow--;
            }
        }

        topDiscInCols[col] = topDiscInCols[col] == gameLogic.getRows() ? -1 : topDiscInCols[col] + 1;
    }

    /****************** INIT NEW GAME *****************/

    private String getGameVariantAsText(GameVariant gameVariant)
    {
        if      (gameVariant == GameVariant.REGULAR)  { return "Regular"; }
        else if (gameVariant == GameVariant.CIRCULAR) { return "Circular"; }
        else    { return "Popout"; }
    }

    private void createPlayers() {
        List<Player> players = gameLogic.getPlayers();
        int i = 0;

        for (Player p : players) {
            TogglePlayerDisplayActive playerDisplay = new TogglePlayerDisplayActive(p);
            this.players.add(playerDisplay);
            playerIdToPlayerIndex.put(p.getId(), i);
            i++;
        }

        LeftPanel_playersTable_TableView.setItems(this.players);
    }

    private void initTopDiscInColsArr() {
        int cols = gameLogic.getCols();
        topDiscInCols = new int[cols];

        // row index of top disc in column is set to -1 when a column is empty
        for (int i = 0; i < cols; ++i) {
            topDiscInCols[i] = -1;
        }
    }

    private Circle createNewDisc() {
        Circle c = new Circle();
        c.setRadius(20);
        c.setFill(Color.TRANSPARENT);

        return c;
    }

    private StackPane createNewDiscContainer(int row, int col) {
        StackPane sp = new StackPane();
        sp.getStyleClass().add("game-cell");
        Circle c = createNewDisc();
        GridPane.setRowIndex(c, row);
        GridPane.setColumnIndex(c, col);
        c.getStyleClass().addAll("emptyDisc");
        sp.getChildren().addAll(c);

        return sp;
    }

    private void createBoard() {
        CenterPanel_boardArea_GridPane = new GridPane();
        int cols = gameLogic.getCols();
        int rows = gameLogic.getRows();

        CenterPanel_boardArea_GridPane.setAlignment(Pos.CENTER);
        CenterPanel_boardArea_GridPane.setHgap(0);
        CenterPanel_boardArea_GridPane.setVgap(0);

        addColButtons(cols, 0, MoveType.INSERT);
        addBoardCells(cols, rows);
        if (gameLogic.isPopout()) {
            addColButtons(cols, rows + 1, MoveType.POPOUT);
        }

        MainPanel_BorderPane.setCenter(CenterPanel_boardArea_GridPane);
    }

    private void addBoardCells(int cols, int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                StackPane sp = createNewDiscContainer(i, j);
                //Circle c = createNewDisc();
                //c.getStyleClass().addAll("emptyDisc");
                //sp.getChildren().add(c);
                //GridPane.setRowIndex(c, i);
                GridPane.setRowIndex(sp, i);
                //GridPane.setColumnIndex(c, j);
                GridPane.setColumnIndex(sp, j);
                GridPane.setMargin(sp, new Insets(0, 0, 0, 0));
                //CenterPanel_boardArea_GridPane.getChildren().add(c);
                CenterPanel_boardArea_GridPane.getChildren().add(sp);
            }
        }
    }

    // columns counting statrs from 1
    // as ComputerPlayer makes a pseudo move in column 0
    private void addColButtons(int cols, int row, MoveType buttonType) {
        for (int i = 0; i < cols; i++) {
            Button b = new ColumnButton(i + 1, buttonType);
            b.getStyleClass().addAll("colButton", buttonType == MoveType.INSERT ? "insert" : "pop");
            String buttonId = Integer.toString(i) + (buttonType == MoveType.INSERT ? "I" : "P");
            b.setId(buttonId);
            b.setOnAction((ActionEvent) -> {
                playSingleMove((ColumnButton) b, buttonType);
            });
            b.disableProperty().bind(Bindings.or(isReplayMode, isRoundOn.not()));
            b.disableProperty().bind(isCurrentPlayerComputer);

            GridPane.setRowIndex(b, row);
            GridPane.setColumnIndex(b, i);
            if (buttonType == MoveType.INSERT) {
                GridPane.setMargin(b, new Insets(0, 0, 10, 0));
            } else {
                GridPane.setMargin(b, new Insets(10, 0, 0, 0));
            }
            CenterPanel_boardArea_GridPane.getChildren().add(b);
            this.myButtons.put(buttonId, b);
        }

    }

    private boolean checkWinner()
    {
        if (gameLogic.getHasWinner()) {
            this.winAlert = true;
            showWinAlert();
            endRound();
            return true;
        }

        return false;
    }

    private void checkTie()
    {
        if (!gameLogic.isPopout() && gameLogic.getIsBoardFull()) {
            this.tieAlert = true;
            showTieAlert();
            endRound();
        }
    }

    /****************** MAKING MOVES ******************/

    private void playSingleMove(ColumnButton b, MoveType buttonType) {
        TogglePlayerDisplayActive currentPlayer = players.get(
                playerIdToPlayerIndex.get(
                        gameLogic.getIdOfCurrentPlayer()));

        boolean isValidMove = gameLogic.play(b.getCol(), buttonType == MoveType.POPOUT);
        Move move = gameLogic.getLastMove();
        if (isValidMove) {
            if (move.getMoveType() == MoveType.INSERT)
                playInsertMove(move.getCol(), currentPlayer.getPlayerId());
            else
                playPopoutMove(move.getCol());

            currentPlayer.setNumMoves(currentPlayer.getNumMoves() + 1);
            addMoveToTable(move);
            LeftPanel_playersTable_TableView.refresh();
            this.computerAlert.close();
            if (!checkWinner()) {
                checkTie();
            }
        } else {
            showInvalidMoveAlert();
        }

        selectNextPlayer();
        playComputerIfNeeded();
    }

    private void selectNextPlayer() {
        this.currentPlayerID.setValue(gameLogic.getIdOfCurrentPlayer());
        int selectedIndex = LeftPanel_playersTable_TableView.getSelectionModel().getSelectedIndex();
        LeftPanel_playersTable_TableView.getSelectionModel().clearSelection(selectedIndex);

        LeftPanel_playersTable_TableView.
                getSelectionModel().
                select(playerIdToPlayerIndex.
                        get(currentPlayerID.get()));
        LeftPanel_playersTable_TableView.refresh();
    }

    private Button getButtonByMove(Move move) {
        String expectedId = move.getCol() + (move.getMoveType() == MoveType.INSERT ? "I" : "P");
        return myButtons.get(expectedId);
    }

    private void playComputerIfNeeded() {
        if (gameLogic.getTypeOfCurrentPlayer() == PlayerTypes.COMPUTER && isRoundOn.get() == true) {
            this.computerAlert.show();
            isCurrentPlayerComputer.set(true);
            ComputerTurnTask turn = new ComputerTurnTask(this.gameLogic, this);
            Thread computerTurn = new Thread(turn);
            TogglePlayerDisplayActive currentPlayer = players.get(
                    playerIdToPlayerIndex.get(
                            gameLogic.getIdOfCurrentPlayer())
            );

            computerTurn.start();

            turn.setOnSucceeded(e -> {
                //gameLogic.play(0, gameLogic.isPopout());
                //Move move = gameLogic.getLastMove();
                // Patch...
                Move move = null;
                if (this.computerFirst) {
                    move = new Move(currentPlayerID.get(), gameLogic.getPlayerName(currentPlayerID.get()),
                            1, this.gameLogic.timeFromBegining() ,MoveType.INSERT);
                    this.computerFirst = false;
                } else {
                    move = gameLogic.getLastMove();
                }
                Button b = getButtonByMove(move);
                if (b != null) {
                    b.fire();
                } else {
                    // Something went horribly wrong :(
                }
            });
        }
        isCurrentPlayerComputer.set(false);
    }

    private void playPopoutMove(int col) {
        removeDiscOfPopout(col);
        topDiscInCols[col] = topDiscInCols[col] == gameLogic.getRows() ?
                -1 : topDiscInCols[col] + 1;
    }

    private void playInsertMove(int col, int playerId) {
        addDiscOfInsert(col, playerId);
        topDiscInCols[col] = topDiscInCols[col] == -1 ?
                gameLogic.getRows() : topDiscInCols[col]  - 1;
    }

    private void removeDiscOfPopout(int col) {
        Node discBelow = null, discAbove = null;
        boolean reachedTop = false;
        int i = gameLogic.getRows();

        while (!reachedTop && i > 1) {
            discAbove = getDiscInRowCol(i - 1, col);
            discBelow = getDiscInRowCol(i, col);
            reachedTop = discAbove.getStyleClass().contains("emptyDisc") &&
                    !discBelow.getStyleClass().contains("emptyDisc");
            if (discBelow != null) {
                discBelow.getStyleClass().clear();
                if (discAbove != null) {
                    discBelow.getStyleClass().addAll(discAbove.getStyleClass());
                }
            }
            i--;
        }

        // adding top disc in column
        if (i == 1 && discAbove != null) {
            discAbove.getStyleClass().clear();
            discAbove.getStyleClass().add("emptyDisc");
        }
    }

    private void addDiscOfInsert(int col, int playerid) {
        Node discBelow = null, discAbove = null;
        String style = "player" + Integer.toString(playerIdToPlayerIndex.get(playerid));
        int i = 1;
        while (i < gameLogic.getRows()) {
            discAbove = getDiscInRowCol(i, col);
            discBelow = getDiscInRowCol(i + 1, col);
            if (discAbove != null && discBelow != null) {
                if (discAbove.getStyleClass().contains("emptyDisc") &&
                        !discBelow.getStyleClass().contains("emptyDisc")) {
                    discAbove.getStyleClass().clear();
                    discAbove.getStyleClass().addAll(style);
                    break;
                }
            }

            i++;
        }

        // adding bottom disc in column
        if (i == gameLogic.getRows() && discBelow != null) {
            discBelow.getStyleClass().clear();
            discBelow.getStyleClass().add(style);
        }
    }

    private void addMoveToTable(Move move) {
        String playerName = players.get(playerIdToPlayerIndex.get(move.getPlayerId())).getName();
        MoveDisplay moveDisplay= new MoveDisplay(move, playerName);
        moves.add(moveDisplay);
        LeftPanel_movesHistory_TableView.getItems().add(moveDisplay);
    }

    /******************* MOVE ALERTS ******************/

    private void showInvalidMoveAlert() {
        String message = "Invalid move, try again!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Invalid move");
        alert.setHeaderText("Invalid move");
        Optional<ButtonType> result = alert.showAndWait();
    }

    private void showTieAlert() {
        String message = "Round is over with a tie!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Round over");
        alert.setHeaderText("Round is over with a tie!");
        Optional<ButtonType> result = alert.showAndWait();
    }

    private void showWinAlert() {
        Set<Integer> winners = gameLogic.getWinners();
        String message = "{0} win" + (winners.size() == 1 ? "s " : " ") + "the round!";
        List<String> names = new ArrayList<>();

        for (Integer playerId : winners)
            names.add(players.get(playerIdToPlayerIndex.get(playerId)).getName());

        StringBuilder namesMsg = new StringBuilder();
        for (int i = 0; i < names.size() - 1; ++i) {
            namesMsg.append(names.get(i)).append(", ");
        }

        String lastName = names.get(names.size() - 1);
        namesMsg = new StringBuilder(names.size() == 1 ? lastName :
                (namesMsg.substring(0, namesMsg.length() - 2) + " and " + lastName));

        message = MessageFormat.format(message, namesMsg.toString());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("We have a winner!");
        alert.setHeaderText("We have a winner!");
        Optional<ButtonType> result = alert.showAndWait();
    }

    /******************* XML LOADING ******************/

    private void loadXmlFailed()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loading File Failed");
        alert.setHeaderText("Loading File Failed");
        alert.setContentText("Loading XML file failed.\nPlease try again");
        this.xmlLoadedSuccessfully = false;
        alert.showAndWait();
    }

    private void loadXmlFailed(Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loading File Failed");
        alert.setHeaderText("Loading File Failed");
        alert.setContentText("Loading XML file failed: " + e.getMessage() + "\nPlease try again");
        this.xmlLoadedSuccessfully = false;
        alert.showAndWait();
    }

    private boolean createLoadingTask(File settingsFile) {
        try {
            loadXMLTask XMLLoader = new loadXMLTask(gameFactory, settingsFile.getAbsolutePath());
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/desktopApp/resources/xmlLoading.fxml");
            fxmlLoader.setLocation(url);
            VBox xmlLoading = fxmlLoader.load(url.openStream());

            Scene secondScene = new Scene(xmlLoading, 450, 200);
            secondScene.getStylesheets().add(getClass().getResource("/desktopApp/resources/mainStyle.css").toExternalForm());
            Stage xmlLoadingWindow = new Stage();
            xmlLoadingWindow.setTitle("XML Loading");
            xmlLoadingWindow.setScene(secondScene);
            xmlLoadingWindow.initModality(Modality.WINDOW_MODAL);
            xmlLoadingWindow.initOwner(primaryStage);
            //xmlLoadingWindow.setX(primaryStage.getX());
            //xmlLoadingWindow.setY(primaryStage.getY() + 200);

            xmlLoadingController controller = fxmlLoader.getController();
            controller.updateFileName(settingsFile.getName());

            Thread loadXMLThread = new Thread(XMLLoader);
            controller.getProgressHbox_Progress_progressBar().progressProperty().bind(XMLLoader.progressProperty());
            controller.getStatusHbox_statusLabel_label().textProperty().bind(XMLLoader.messageProperty());
            xmlLoadingWindow.show();
            loadXMLThread.start();

            XMLLoader.setOnSucceeded((event) ->{
                xmlLoadingWindow.close();
                //this.gameLogic = XMLLoader.getValue();
                if (XMLLoader.getValue()) {
                    try {
                        LeftPanel_playersTable_TableView.getItems().clear();
                        this.gameLogic = gameFactory.getNewGame();
                        this.gameLogic.setRoundFromSettings(true);
                        createBoard();
                        createPlayers();
                        this.xmlLoadedSuccessfully = true;
                        this.isValidXML.set(true);
                        this.TopPanel_GameFileLabel_Label.setText(settingsFile.getName());
                        this.TopPanel_welcome_Label.setText("Ready to Play!");
                        this.TopPanel_GameVariantLabel_Label.setText(getGameVariantAsText(gameLogic.getGameVariant()));
                    }
                    catch (Exception e) { loadXmlFailed(e); }
                }
                else {
                    loadXmlFailed();
                }
            });
        }
        catch (Exception e) { loadXmlFailed(e); }

        return this.xmlLoadedSuccessfully;
    }

    @FXML
    public void loadNewSettingFile_onButtonAction(javafx.event.ActionEvent actionEvent) {
        File settingsFile;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Settings File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        //File settingsFile = fileChooser.showOpenDialog(primaryStage);
        //if (settingsFile == null)
        //   return;

        while (true) {
            try {
                settingsFile = fileChooser.showOpenDialog(primaryStage);
                if (settingsFile == null) { alertNoXMLFileWasChosen(); return; }
                if (createLoadingTask(settingsFile)) { break; }
                else {
                    isValidXML.setValue(false);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading file");
                loadXmlFailed();
                continue;
            }
        }

        CenterPanel_boardArea_GridPane.visibleProperty().setValue(true);
        isValidXML.setValue(true);
    }

    private void alertNoXMLFileWasChosen()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "File was not chosen. \n Please try again", ButtonType.OK);
        alert.setTitle("File was not chosen");
        alert.setHeaderText("File was not chosen");
        this.xmlLoadedSuccessfully = false;
        alert.showAndWait();
    }

    /********************* REPLAY *********************/

    private boolean isPlayerActive (int playerId) {
        return players.get(playerIdToPlayerIndex.get(playerId)).isActive();
    }

    private void addDiscOfPopout(int col, int playerId) {
        for (int i = 1; i <= gameLogic.getRows() - 1; ++i) {
            Node cellAbove = getDiscInRowCol(i, col);
            Node cellBelow = getDiscInRowCol(i + 1, col);
            if (cellAbove != null && cellBelow != null) {
                if (cellAbove.getStyleClass().contains("emptyDisc") &&
                        !cellBelow.getStyleClass().contains("emptyDisc")) {
                    moveDiscsUp(i + 1, col, playerId);
                    break;
                }
            }
        }
    }

    private void moveDiscsUp(int rowDiscBelow, int col, int playerId) {
        Node discBelow = null, discAbove = null;

        while (rowDiscBelow <= gameLogic.getRows()) {
            discAbove = getDiscInRowCol(rowDiscBelow - 1, col);
            discBelow = getDiscInRowCol(rowDiscBelow, col);

            if (discAbove != null && discBelow !=null ) {
                discAbove.getStyleClass().clear();
                discAbove.getStyleClass().addAll(discBelow.getStyleClass());
            }
            rowDiscBelow++;
        }

        int playerIndex = playerIdToPlayerIndex.get(playerId);
        String style = "player" + Integer.toString(playerIndex);
        if (discBelow != null) {
            discBelow.getStyleClass().clear();
            discBelow.getStyleClass().add(style);
        }
    }

    private void removeDiscOfInsert(int col) {
        for (int i = 1; i <= gameLogic.getRows(); ++i) {
            Node cell = getDiscInRowCol(i, col);
            if (cell != null && !cell.getStyleClass().contains("emptyDisc")) {
                cell.getStyleClass().clear();
                cell.getStyleClass().addAll("emptyDisc");
                break;
            }
        }
    }

    @FXML
    private void rightReplay_onButtonAction(javafx.event.ActionEvent actionEvent) {
        while (moveIndexForReplay < moves.size() - 1 &&
                !isPlayerActive(moves.get(moveIndexForReplay + 1).getPlayerid())) {
            moveIndexForReplay++;
        }

        if (moveIndexForReplay < moves.size() - 1) {
            moveIndexForReplay++;
            LeftPanel_movesHistory_TableView.getSelectionModel().select(moveIndexForReplay);
            MoveDisplay move = moves.get(moveIndexForReplay);
            if (move.getMoveType() == MoveType.INSERT) {
                addDiscOfInsert(move.getCol() - 1, move.getPlayerid());
            }
            else {
                removeDiscOfPopout(move.getCol() - 1);
            }
        }
    }

    @FXML
    private void leftReplay_onButtonAction(javafx.event.ActionEvent actionEvent) {
        if (moveIndexForReplay >= 0) {
            MoveDisplay move = moves.get(moveIndexForReplay);

            if (move.getMoveType() == MoveType.INSERT) {
                removeDiscOfInsert(move.getCol() - 1);
            }
            else {
                addDiscOfPopout(move.getCol() - 1, move.getPlayerid());
            }
        }

        moveIndexForReplay--;
        while (moveIndexForReplay >= 0 &&
                !isPlayerActive(moves.get(moveIndexForReplay).getPlayerid()))
            moveIndexForReplay--;

        if(moveIndexForReplay >= 0)
            LeftPanel_movesHistory_TableView.getSelectionModel().select(moveIndexForReplay);
        else
            LeftPanel_movesHistory_TableView.getSelectionModel().select(null);
    }

    @FXML
    public void toggleReplay_onButtonAction(javafx.event.ActionEvent actionEvent) {
        isReplayMode.set(!isReplayMode.get());
        if (isReplayMode.get() == true) {
            moveIndexForReplay = moves.size() - 1;
            LeftPanel_movesHistory_TableView.getSelectionModel().select(moveIndexForReplay);
            LeftPanel_toggleReplay_Button.setText("Replay OFF");
            saveGridPaneStyles();
        }
        else {
            LeftPanel_toggleReplay_Button.setText("Replay ON");
            LeftPanel_movesHistory_TableView.getSelectionModel().select(null);
            reconstructBoard();
        }
    }

    private void reconstructBoard() {
        Node discToUpdate = null;
        for (int i = 1; i <= gameLogic.getRows(); ++i) {
            ArrayList<ArrayList<String>> rowStyles = savedGridPaneStyles.get(i - 1);
            for (int j = 0; j < gameLogic.getCols(); ++j) {
                discToUpdate = getDiscInRowCol(i, j);
                ArrayList<String> cellStyles = rowStyles.get(j);
                if (discToUpdate != null) {
                    discToUpdate.getStyleClass().clear();
                    discToUpdate.getStyleClass().addAll(cellStyles);
                }
            }
        }
    }

    private void saveGridPaneStyles() {
        savedGridPaneStyles =  new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 1; i <= gameLogic.getRows(); ++i) {
            ArrayList<ArrayList<String>> rowStyles = new ArrayList<ArrayList<String>>();
            for (int j = 0; j < gameLogic.getCols(); ++j) {
                Node cell = getDiscInRowCol(i, j);
                ArrayList<String> cellStyles = getAllStyles(cell.getStyleClass());
                rowStyles.add(cellStyles);
            }
            savedGridPaneStyles.add(rowStyles);
        }
    }

    private ArrayList<String> getAllStyles(ObservableList<String> styleClass) {
        ArrayList<String> styles = new ArrayList<>();

        for (int i = 0; i < styleClass.size(); ++i) {
            String str = styleClass.get(i);
            styles.add(str);
        }

        return styles;
    }

}