package desktopApp;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private ObservableList<PlayerDisplay> players = FXCollections.observableArrayList();
    private SimpleIntegerProperty currentPlayerID;
    private boolean tieAlert;
    private boolean winAlert;
    private ArrayList<ArrayList<ArrayList<String>>> savedGridPaneStyles;
    private int moveIndexForReplay;

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
        gameFactory = new GameFactory();
        xmlLoadedSuccessfully = false;
        isValidXML = new SimpleBooleanProperty(false);
        isReplayMode = new SimpleBooleanProperty(false);
        isRoundOn = new SimpleBooleanProperty(false);
        currentPlayerID = new SimpleIntegerProperty();
        tieAlert = false;
        winAlert = false;
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

        LeftPanel_playerID_TableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        LeftPanel_playerName_TableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        LeftPanel_playerColour_TableColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        LeftPanel_playerMoves_TableColumn.setCellValueFactory(new PropertyValueFactory<>("numMoves"));
        LeftPanel_playerType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        LeftPanel_movePlayer_TableColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        LeftPanel_moveType_TableColumn.setCellValueFactory(new PropertyValueFactory<>("moveType"));
        LeftPanel_moveColumn_TableColumn.setCellValueFactory(new PropertyValueFactory<>("col"));
        LeftPanel_moveTimeStamp_TableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        Callback<TableColumn<PlayerDisplay, String>, TableCell<PlayerDisplay, String>> colorCellFactory =
                new Callback<TableColumn<PlayerDisplay, String>, TableCell<PlayerDisplay, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        TableCell cell = new TableCell<PlayerDisplay, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item != null) {
                                    setText("");
                                    int row = getTableRow().getIndex();
                                    String styleClass = row >= gameLogic.getNumberOfPlayersToInitialized() ?
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
        LeftPanel_replayRightArrow_Button.getStyleClass().add("replayButton");
        LeftPanel_replayLeftArrow_Button.setId("leftReplayButton");
        LeftPanel_replayLeftArrow_Button.getStyleClass().add("replayButton");

//        LeftPanel_playersTable_TableView.setRowFactory(new Callback<TableView<PlayerDisplay>, TableRow<PlayerDisplay>>() {
//            @Override
//            public TableRow<PlayerDisplay> call(TableView<PlayerDisplay> tableView) {
//                TableRow<PlayerDisplay> row = new TableRow<PlayerDisplay>() {
//                    @Override
//                    protected void updateItem(PlayerDisplay playerDisplay, boolean empty) {
//                        super.updateItem(playerDisplay, empty);
//                        if (playerDisplay != null)
//                            disableProperty().bind(playerDisplay.isActive.not());
//                    }
//                };
//
//                return row;
//            }
//        });

        LeftPanel_playersTable_TableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void showComputerIsPlayingAlert()
    {
        String message = "Computer is making a move!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Interrupting to Computer's turn");
        alert.setHeaderText("Please let the computer finish\nand then try again.");
        Optional<ButtonType> result = alert.showAndWait();
    }

    private Node getDiscInRowCol(int row, int col) {
        ObservableList<Node> gridPaneCells = CenterPanel_boardArea_GridPane.getChildren();
        Node res = null;

        for (Node cell : gridPaneCells) {
            if (GridPane.getRowIndex(cell) == row && GridPane.getColumnIndex(cell) == col) {
                res = cell;
                break;
            }
        }

        return res;
    }

    /********************* THEMES *********************/

    @FXML
    void draculaTheme_onButtonAction(javafx.event.ActionEvent event) { }

    @FXML
    void funTheme_onButtonAction(javafx.event.ActionEvent event) { }

    @FXML
    void lightTheme_onButtonAction(javafx.event.ActionEvent event) { }

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
    }

    @FXML
    public void exitGame_onButtonAction(javafx.event.ActionEvent actionEvent) {
        //TopPanel_welcome_Label.setText("Bye Bye?");
        //try { Thread.sleep(1000); }
        //catch (InterruptedException e) { }
        //System.exit(0);

        VBox xmlLoading = null;

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/desktopApp/resources/ExitConfirmBox.fxml");
        fxmlLoader.setLocation(url);
        try                 { xmlLoading = fxmlLoader.load(url.openStream()); }
        catch (Exception e) { System.exit(0); }

        ExitConfirmController exitController = fxmlLoader.getController();
        Scene secondScene = new Scene(xmlLoading, 200, 110);
        Stage ExitConfirm = new Stage();
        exitController.setStage(ExitConfirm);

        ExitConfirm.setTitle("Exit Confirmation");
        ExitConfirm.setScene(secondScene);
        ExitConfirm.initModality(Modality.WINDOW_MODAL);
        ExitConfirm.initOwner(primaryStage);
        ExitConfirm.setX(primaryStage.getX() + 300);
        ExitConfirm.setY(primaryStage.getY() + 200);
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
        gameLogic.increaseRoundPlayed();
        TopPanel_RoundsPlayedLabel_Label.setText(String.valueOf(gameLogic.getNumberOfRoundsPlayed()));
    }

    private void clearBoard() {
        ObservableList<Node> gridPaneCells = CenterPanel_boardArea_GridPane.getChildren();

        for (Node cell : gridPaneCells) {
            if (cell instanceof Circle) {
                cell.getStyleClass().clear();
                cell.getStyleClass().add("emptyDisc");
            }
        }
    }

    /****************** PLAYER RESIGN *****************/

    @FXML
    public void playerResign_onButtonAction(javafx.event.ActionEvent actionEvent) {
        gameLogic.resignPlayer();

        players.get(playerIdToPlayerIndex.get(currentPlayerID.get())).setActive(false);
        removePlayerDiscsFromBoard(currentPlayerID.get());

        // TODO: remove/draken row from players table

        if (gameLogic.getHasWinner()) {
            showWinAlert();
            endRound();
        }

        selectNextPlayer();
    }

    private void removePlayerDiscsFromBoard(int playerId) {
        String playerStyle = "player" + Integer.toString(playerIdToPlayerIndex.get(playerId));

        ObservableList<Node> gridCells = CenterPanel_boardArea_GridPane.getChildren();

        for (Node cell : gridCells) {
            if (cell instanceof Circle && cell.getStyleClass().contains(playerStyle)) {
                int bottomRow = GridPane.getRowIndex(cell);
                int col = GridPane.getColumnIndex(cell);
                removeSingleDiscFromBoard(gridCells, col, bottomRow);
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
            PlayerDisplay playerDisplay = new PlayerDisplay(p);
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
        c.setRadius(25);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.BLACK);

        return c;
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
                Circle c = createNewDisc();
                c.getStyleClass().addAll("emptyDisc");
                GridPane.setRowIndex(c, i);
                GridPane.setColumnIndex(c, j);
                GridPane.setMargin(c, new Insets(7, 10, 7, 0));
                CenterPanel_boardArea_GridPane.getChildren().add(c);
            }
        }
    }

    // columns counting statrs from 1
    // as ComputerPlayer makes a pseudo move in column 0
    private void addColButtons(int cols, int row, MoveType buttonType) {
        for (int i = 0; i < cols; i++) {
            Button b = new ColumnButton(i + 1, buttonType);
            b.getStyleClass().add("colButton");
            b.setOnAction((ActionEvent) -> {
                playSingleMove((ColumnButton) b, buttonType);
            });
            b.disableProperty().bind(Bindings.or(isReplayMode, isRoundOn.not()));

            GridPane.setRowIndex(b, row);
            GridPane.setColumnIndex(b, i);
            CenterPanel_boardArea_GridPane.getChildren().add(b);
        }

    }

    /****************** MAKING MOVES ******************/

    private void playSingleMove(ColumnButton b, MoveType buttonType) {
        PlayerDisplay currentPlayer = players.get(
                playerIdToPlayerIndex.get(
                        gameLogic.getIdOfCurrentPlayer()));

        boolean isValidMove = gameLogic.play(b.getCol(), buttonType == MoveType.POPOUT);
        if (isValidMove) {
            if (b.getButtonType() == MoveType.INSERT)
                playInsertMove(b.getCol() - 1, currentPlayer.getId());
            else
                playPopoutMove(b.getCol() - 1);

            currentPlayer.setNumMoves(currentPlayer.getNumMoves() + 1);
            addMoveToTable(gameLogic.getLastMove());
            LeftPanel_playersTable_TableView.refresh();

            if (gameLogic.getHasWinner()) {
                this.winAlert = true;
                showWinAlert();
                endRound();
            } else if (!gameLogic.isPopout() && gameLogic.getIsBoardFull()) {
                this.tieAlert = true;
                showTieAlert();
                endRound();
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

    private void playComputerIfNeeded() {
        if (gameLogic.getTypeOfCurrentPlayer() == PlayerTypes.COMPUTER && isRoundOn.get() == true) {
            ComputerTurnTask turn = new ComputerTurnTask(this.gameLogic, this);
            Thread computerTurn = new Thread(turn);
            PlayerDisplay currentPlayer = players.get(
                    playerIdToPlayerIndex.get(
                            gameLogic.getIdOfCurrentPlayer())
            );

            computerTurn.start();

            turn.setOnSucceeded(e -> {
                //gameLogic.play(0, gameLogic.isPopout());
                //Move move = gameLogic.getLastMove();
                Move move = turn.getValue();
                if (move.getMoveType() == MoveType.INSERT)
                    playInsertMove(move.getCol() - 1, currentPlayer.getId());
                else
                    playPopoutMove(move.getCol() - 1);

                currentPlayer.setNumMoves(currentPlayer.getNumMoves() + 1);
                addMoveToTable(gameLogic.getLastMove());

                if (gameLogic.getHasWinner()) {
                    winAlert = true;
                } else if (!gameLogic.isPopout() && gameLogic.getIsBoardFull()) {
                    tieAlert = true;
                }

                selectNextPlayer();

                if (tieAlert) {
                    showTieAlert();
                    endRound();
                }

                if (winAlert) {
                    showWinAlert();
                    endRound();
                }

                playComputerIfNeeded();
            });
        }
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
        endRound();
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
        endRound();
    }

    /******************* XML LOADING ******************/

    private void loadXmlFailed()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loading File Failed");
        alert.setHeaderText("Loading File Failed");
        alert.setContentText("Loading XML file failed. \n Please try again");
        this.xmlLoadedSuccessfully = false;
        alert.show();
        try { Thread.sleep(2000); }
        catch (Exception e) {}
        alert.close();
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
                        this.TopPanel_GameFileLabel_Label.setText(settingsFile.getAbsolutePath());
                        this.TopPanel_welcome_Label.setText("NinA is Ready to Play!");
                        this.TopPanel_GameVariantLabel_Label.setText(getGameVariantAsText(gameLogic.getGameVariant()));
                    }
                    catch (Exception e) { loadXmlFailed(); }
                }
                else {
                    loadXmlFailed();
                }
            });
        }
        catch (Exception e) { loadXmlFailed(); }

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
                else { isValidXML.setValue(false); return; }
            } catch (Exception e) {
                System.out.println("Error loading file");
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