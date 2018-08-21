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

    public Game()
    {
        this.startingTime = null;
        //this.gameSettings = new GameSettings();
    }

    public Game(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public void setBoardFromSettings(boolean restartPlayers) {
        this.board = new Board(gameSettings.getBoardNumRows(), gameSettings.getBoardNumCols(), gameSettings.getGameVariant() == GameVariant.CIRCULAR);
        this.hasWinner = false;
        this.isBoardFull = false;
        this.currentPlayer = null;
        this.playedMoves = new ArrayList<Move>();
        this.startingTime = null;
        restartPlayers(restartPlayers);
    }

    protected void restartPlayers(boolean isRestart) {
        if (isRestart) {
            if (gameSettings.isDynamicPlayers()) {
                List<PlayerSettings> playersSettings = gameSettings.getPlayers();
                for (PlayerSettings pt : playersSettings) {
                    players.add(new Player(pt));
                }
            }
            else
                this.players = new ArrayList<Player>(2);
        }

        else {
            this.currentPlayer = players.get(0);
            for (Player player : players)
                player.restart();
        }
    }

    protected boolean playHumanPlayer(int col, boolean popout)
    {
        int playerID = this.currentPlayer.getId();
        col--;

        if (board.playMove(col, playerID)) {
            //record move
            playedMoves.add(new Move(playerID, col, timeFromBegining()));
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

    protected boolean playComputerPlayer()
    {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(board.getCols());

        while(!board.playMove(rand, playerID)) { rand = r.nextInt(board.getCols()); }

        //record move
        playedMoves.add(new Move(playerID, rand, timeFromBegining()));
        getPlayerById(playerID).increaseNumberOfTurnsPlayed();

        if (checkWinningMove(rand, playerID)) {
            board.setWinner(playerID);
            board.setHasWinner(true);
        }

        return true;
    }

    @Override
    public int playerTurns(int player) { return getPlayerById(player).getPlayerTurns(); }

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
    public int getCols() { return this.board.getCols(); }

    @Override
    public boolean play(int col, boolean popout)
    {
        boolean ret = true;

        if (this.currentPlayer.getPlayerType() == PlayerTypes.HUMAN) { ret = playHumanPlayer(col, popout); }
        else { ret = playHumanPlayer(col, popout); }

        if (ret) {
            //change current player after turn is completed succefully
            currentPlayer = players.get(currentPlayer.getId() % players.size());

            board.decreaseEmptySpace();
            if (board.isFull()) { this.isBoardFull = true; }

            if (this.startingTime == null) { setStartingTime(); }
        }
        return ret;
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

        return  board.leftRightSequence(col, player) == targetSequence    ||
                board.upDownSequence(col, player) == targetSequence       ||
                board.diagonalDownSequence(col, player) == targetSequence ||
                board.diagonalUpSequence(col, player) == targetSequence;
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

    public void initPlayer(PlayerTypes playerType, int id, String name) {
        Player player = new Player(id, playerType, name);
        players.add(player);
        if (null == currentPlayer) { currentPlayer = player; }
    }

    public PlayerTypes getTypeOfCurrentPlayer() { return currentPlayer.getPlayerType(); }

    public boolean undoLastMove() {
        boolean isUndoAvailable = true;

        if (!playedMoves.isEmpty()) {
            //undo in moves list
            int indexOfLastPlayedMove = playedMoves.size() - 1;
            Move undoMove = playedMoves.get(indexOfLastPlayedMove);
            playedMoves.remove(indexOfLastPlayedMove);

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

}
