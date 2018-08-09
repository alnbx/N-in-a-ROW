package engine;//import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import common.PlayersTypes;


import java.io.Serializable;
import java.util.*;
import java.lang.Exception;

/**
 * Created by user on 27/07/2018.
 */
public class Game implements GameLogic, Serializable {

    final int maxNumOfPlayers = 6;
    private Board board;
    private boolean hasWinner;
    private boolean isBoardFull;
    private Date startingTime;
    private boolean isFirstMove;
    private GameSettings gameSettings;
    private List<Player> players;
    private Player currentPlayer;
    private List<Move> playedMoves;

    public Game()
    {
        this.players = new ArrayList<Player>(maxNumOfPlayers);
        this.startingTime = null;
        this.gameSettings = new GameSettings();
    }

    private void setBoardFromSettings() {
        this.board = new Board(gameSettings.getBoardNumRows(), gameSettings.getBoardNumCols());
        this.hasWinner = false;
        this.isBoardFull = false;
        this.currentPlayer = null;
        this.playedMoves = new ArrayList<Move>();
    }

    @Override
    public boolean playHumanPlayer(int col)
    {
        int playerID = this.currentPlayer.getId();
        col--;

        if (board.playMove(col, playerID))
        {
            //record move
            playedMoves.add(new Move(playerID, col));
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
    public boolean playComputerPlayer()
    {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(board.getCols());

        while(!board.playMove(rand, playerID)) {
            rand = r.nextInt(board.getCols());
        }

        //record move
        playedMoves.add(new Move(playerID, rand));
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

        diff = ((null == this.startingTime) ? 0 : this.startingTime.getTime() - now.getTime());
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;

        return String.format("%d:%d", diffMinutes, diffSeconds);
    }

    @Override
    public int getCols() { return this.board.getCols(); }

    @Override
    public void restartGame()
    {
        board.restart();
        playedMoves.clear();

        for (Player player : players) { player.restart(); }

        this.hasWinner = false;
        this.isBoardFull = false;

        this.currentPlayer = players.get(0);
    }

    @Override
    public boolean play(int col)
    {
        boolean ret = true;

        if (this.currentPlayer.getPlayerType() == PlayersTypes.HUMAN) { ret = playHumanPlayer(col); }
        else { playComputerPlayer(); }
        //change current player after turn is completed succefully
        currentPlayer = players.get(currentPlayer.getId() % players.size());

        board.decreaseEmptySpace();
        if (board.isFull()) { this.isBoardFull = true; }

        if (!this.isFirstMove) {
            this.isFirstMove = true;
            setStartingTime();
        }

        return ret;
    }

    private void setStartingTime() { this.startingTime = new Date(); }

    @Override
    public void loadSettingsFile(String filePath) throws Exception
    {
        try {
            gameSettings.initGameSettings(filePath);
            setBoardFromSettings();
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean checkWinningMove(int col, int player)
    {
        int targetSequence = this.gameSettings.getTarget();

        return  board.leftRightSequence(col, player) == targetSequence    ||
                board.upDownSequence(col, player) == targetSequence       ||
                board.diagonalDownSequence(col, player) == targetSequence ||
                board.diagonalUpSequence(col, player) == targetSequence;
    }

    public char[][] boardReadyToPrint() { return board.getBoardAsCharArray(); }

    public int getNumberOfPlayers() {
        return gameSettings.getNumOfPlayers();
    }

    public int getSequenceLength() {
        return gameSettings.getTarget();
    }

    public boolean getHasWinner() { return this.hasWinner; }
    public boolean getIsBoardFull() { return this.isBoardFull; }

    public void initPlayer(PlayersTypes playerType, int id, String name) {
        Player player = new Player(id, playerType, name);
        players.add(player);
        if (null == currentPlayer)
            currentPlayer = player;
    }

    public PlayersTypes getTypeOfCurrentPlayer() {
        return currentPlayer.getPlayerType();
    }

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

    private Player getPlayerById(int playerId) {
        Player player = null;

        for (Player p : players) {
            if (p.getId() == playerId) {
                player = p;
                break;
            }
        }
        return player;
    }

    public List<Move> getMovesHistory() {
        return playedMoves;
    }

    public int getIdOfCurrentPlayer() {
        return currentPlayer.getId();
    }

}
