package engine;//import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import common.*;


import java.io.Serializable;
import java.util.*;
import java.lang.Exception;

/**
 * Created by user on 27/07/2018.
 */
public class Game implements GameLogic, Serializable {
    protected Board board;
    protected boolean hasWinner;
    protected boolean isBoardFull;
    protected Date startingTime;
    protected GameSettings gameSettings;
    protected List<Player> players;
    protected Player currentPlayer;
    protected List<Move> playedMoves;
    protected Move lastMovePlayed;
    protected int activePlayers;
    protected int roundsPlayed;

    public Game()
    {
        this.startingTime = null;
        //this.gameSettings = new GameSettings();
    }

    public Game(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        this.roundsPlayed = 0;
    }

    public void setRoundFromSettings(boolean restartPlayers) {
        this.board = new Board(gameSettings.getBoardNumRows(), gameSettings.getNumCols(), gameSettings.getGameVariant() == GameVariant.CIRCULAR);
        this.hasWinner = false;
        this.isBoardFull = false;
        this.currentPlayer = null;
        this.playedMoves = new ArrayList<Move>();
        this.lastMovePlayed = null;
        this.startingTime = null;
        restartPlayers(restartPlayers);
    }

    protected void restartPlayers(boolean isNewPlayers) {
        if (isNewPlayers) {
            this.players = new ArrayList<Player>();
                List<PlayerSettings> playersSettings = gameSettings.getPlayersSettings();
                for (PlayerSettings ps : playersSettings) {
                    players.add(new Player(ps));
                }
            }
        else {
            for (Player player : players)
                player.restart();
        }

        if (gameSettings.getGameType() != GameType.DynamicMultiPlayer) {
            this.currentPlayer = players.get(0);
            this.activePlayers = this.players.size();
        }
    }

    protected boolean playHumanPlayer(int col, boolean popout)
    {
        int playerID = this.currentPlayer.getId();
        col--;

        if (board.playMove(col, playerID)) {
            //record move
            this.lastMovePlayed = new Move(playerID, col, timeFromBegining(), MoveType.INSERT);
            playedMoves.add(lastMovePlayed);
            getPlayerById(playerID).increaseNumberOfTurnsPlayed();

            if (checkWinningMove(col, playerID)) {
                board.setWinner(playerID);
                board.setHasWinner(true);
                this.hasWinner = true;
            }
            return true;
        }

        return false;
    }

    @Override
    public Move getLastMove() {
        return lastMovePlayed;
    }

    protected boolean playComputerPlayer()
    {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(board.getCols());

        while(!board.playMove(rand, playerID)) { rand = r.nextInt(board.getCols()); }

        //record move
        this.lastMovePlayed = new Move(playerID, rand, timeFromBegining(), MoveType.INSERT);
        playedMoves.add(lastMovePlayed);
        getPlayerById(playerID).increaseNumberOfTurnsPlayed();

        if (checkWinningMove(rand, playerID)) {
            board.setWinner(playerID);
            board.setHasWinner(true);
            this.hasWinner = true;
        }

        return true;
    }

    @Override
    public int playerTurns(int player) { return getPlayerById(player).getNumMovesMade(); }

    @Override
    public String timeFromBegining()
    {
        Long diff = 0L;
        Date now = new Date();

        diff = ((null == this.startingTime) ? 0 : now.getTime() - this.startingTime.getTime());
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;

        return String.format("%02d:%02d", diffMinutes, diffSeconds);
    }

    @Override
    public int getCols() { return this.gameSettings.getNumCols(); }

    @Override
    public int getRows() { return gameSettings.getNumRows(); }

    @Override
    public boolean isPopout() {
        return gameSettings.isPopout();
    }

    @Override
    public boolean play(int col, boolean popout)
    {
        boolean ret = true;
        PlayerTypes player = this.currentPlayer.getPlayerType();

        if (this.currentPlayer.getPlayerType() == PlayerTypes.HUMAN) { ret = playHumanPlayer(col, popout); }
        else { ret = playComputerPlayer(); }

        if (ret) {
            //change current player after turn is completed succefully
            setNextPlayer();

            if (getLastMove().getMoveType() == MoveType.POPOUT) { board.increaseEmptySpace(); }
            else { board.decreaseEmptySpace(); }

            if (board.isFull()) { this.isBoardFull = true; }

            if (this.startingTime == null) { setStartingTime(); }
        }
        return ret;
    }

    private void setNextPlayer()
    {
        currentPlayer = players.get((getIndexOfCurrentPlayer() + 1) % players.size());
        while(!currentPlayer.isActive()) { currentPlayer = players.get((getIndexOfCurrentPlayer() + 1) % players.size()); }
    }

    private int getIndexOfCurrentPlayer() {
        int index = 0;
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).getId() == currentPlayer.getId()) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void setStartingTime() { this.startingTime = new Date(); }

    @Override
    public void loadSettingsFile(String filePath) throws Exception
    {
        try {
            gameSettings.initGameSettings(filePath);
        } catch (Exception e) {
            throw e;
        }
    }

    protected boolean checkWinningMove(int col, int player)
    {
        int targetSequence = this.gameSettings.getTarget();

        return  board.leftRightSequence(col, player) >= targetSequence    ||
                board.upDownSequence(col, player) >= targetSequence       ||
                board.diagonalDownSequence(col, player) >= targetSequence ||
                board.diagonalUpSequence(col, player) >= targetSequence;
    }

    public char[][] boardReadyToPrint() { return board.getBoardAsCharArray(); }

    public int getNumberOfInitializedPlayers() {
        return players.size();
    }

    public int getNumberOfPlayersToInitialized() {
        return gameSettings.getNumOfPlayers();
    }

    public int getSequenceLength() {
        return gameSettings.getTarget();
    }

    public boolean getHasWinner() { return this.hasWinner; }
    public boolean getIsBoardFull() { return this.isBoardFull; }

//    public void initPlayer(PlayerTypes playerType, int id, String name) {
//        Player player = new Player(id, playerType, name);
//        players.add(player);
//        if (null == currentPlayer) { currentPlayer = player; }
//    }

    public PlayerTypes getTypeOfCurrentPlayer() { return currentPlayer.getPlayerType(); }

    public boolean undoLastMove() {
        boolean isUndoAvailable = true;

        if (!playedMoves.isEmpty()) {
            //undo in moves list
            int indexOfLastPlayedMove = playedMoves.size() - 1;
            Move undoMove = playedMoves.get(indexOfLastPlayedMove);
            playedMoves.remove(indexOfLastPlayedMove);
            lastMovePlayed = playedMoves.get(playedMoves.size() - 1);

            //undo in board
            if (board.undoMove(undoMove.getCol()) ) {
                //update current player
                updatePlayerAfterUndo(undoMove);
            }
            else
                isUndoAvailable = false;
        }
        else
            isUndoAvailable = false;

        return isUndoAvailable;
    }

    private void updatePlayerAfterUndo(Move undoMove) {
        int idOfPlayerInUndoMove = undoMove.getPlayerId();
        Player player = getPlayerById(idOfPlayerInUndoMove);

        if (!player.decreaseNumberOfTurnsPlayed()) {
            System.out.println(player.getName() + " has no moves to undo");
        }

        currentPlayer = player;
    }

    protected Player getPlayerById(int playerId) {
        Player player = null;

        for (Player p : players) {
            if (p.getId() == playerId) {
                player = p;
                break;
            }
        }
        return player;
    }

    public List<Move> getMovesHistory() { return playedMoves; }

    public int getIdOfCurrentPlayer()
    {
        if (null == currentPlayer) { return 0; }
        return currentPlayer.getId();
    }

    public Set<Integer> getWinners() { return board.getWinner(); }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void resignPlayer()
    {
        currentPlayer.deactivatePlayer();
        this.board.removeAllDiscsofPlayer(currentPlayer.getId());
        setNextPlayer();
        this.activePlayers--;

        Set<Integer> winners = checkWinnersAllBoard();
        // check if there's a single active player left in the game
        int winner = getLastActivePlayerId();
        if (winner != -1)
            winners.add(winner);

        if (!winners.isEmpty()) {
            this.board.setWinner(winners);
            this.board.setHasWinner(true);
            this.hasWinner = true;
        }
    }

    // if there's a single player left in the game: returns player ID
    // otherwise: returns -1
    private int getLastActivePlayerId() {
        int lastPlayerId = - 1;
        boolean foundActivePlayer = false;

        for (Player player : players) {
            if (player.isActive()) {
                if (foundActivePlayer) {
                    lastPlayerId = -1;
                    break;
                }
                else {
                    foundActivePlayer = true;
                    lastPlayerId = player.getId();
                }
            }
        }

        return lastPlayerId;
    }

    public GameVariant getGameVariant() { return GameVariant.REGULAR; }

    public int getNumberOfActivePlayers() { return this.activePlayers; }

    private Set<Integer> checkWinnersAllBoard()
    {
        int targetSequence = this.gameSettings.getTarget();
        Set<Integer> res = new HashSet<>();

        for (int col = board.getCols() - 1; col >= 0; col--) {
            for (int i = board.getRows() - 1; i >= 0; i--) {
                if (board.leftRightSequencePopout(col, i) >= targetSequence ||
                        board.upDownSequencePopout(col, i) >= targetSequence ||
                        board.diagonalDownSequencePopout(col, i) >= targetSequence ||
                        board.diagonalUpSequencePopout(col, i) >= targetSequence) {
                    res.add(board.getPlayerInDisc(col, i));
                }
            }
        }

        return res;
    }

    public int getNumberOfRoundsPlayed() { return this.roundsPlayed; }
    public void increaseRoundPlayed()    { this.roundsPlayed++; }
}
