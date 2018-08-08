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
        //board = new Board(4,5);
        //this.sequenceNumber = 4;
        this.players = new ArrayList<Player>(maxNumOfPlayers);
        this.startingTime = null;
        this.gameSettings = new GameSettings();
        //this.gameSettings = new GameSettings(xmlPath);
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
        int player = this.currentPlayer.getId();
        col--;

        if (board.playMove(col, player))
        {
            playedMoves.add(new Move(currentPlayer.getId(), col));
            if (checkWinningMove(col, player)) {
                board.setWinner(player);
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
        int player = currentPlayer.getId();
        int rand = r.nextInt(board.getCols());

        while(!board.playMove(rand, player)) { rand = r.nextInt(board.getCols()); }

            if (checkWinningMove(rand, player)) {
                board.setWinner(player);
                board.setHasWinner(true);
            }

        return true;
    }

    @Override
    //TODO: Needed?
    public int getPlayerNumber() { return this.currentPlayer.getId(); }

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
        int test = currentPlayer.getId();
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
    /*
        public static void main(String[] args)
        {

            engine.Game g = new engine.Game();
            g.playHumanPlayer(1,1);
            g.playHumanPlayer(2,2);
            g.playHumanPlayer(2,1);
            char[][] b = g.boardReadyToPrint();

            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    System.out.print(b[i][j]);
                }
                System.out.print("\n");
            }

            try {
                g.loadSettingsFile("./ex1-small.xml");
                System.out.println((g.getGameSettings()));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    */
    public int getNumberOfPlayers() {
        return gameSettings.getNumOfPlayers();
    }

    // for debug - should be removed
    public GameSettings getGameSettings() {
        return gameSettings;
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
        if (playedMoves.isEmpty())
            return false;

        int indexOfLastPlayedMove = playedMoves.size() - 1;
        Move undoMove = playedMoves.get(indexOfLastPlayedMove);

        playedMoves.remove(indexOfLastPlayedMove);
        board.undoMove(undoMove.getCol());
        updatePlayerAfterUndo(undoMove);

        return true;
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
