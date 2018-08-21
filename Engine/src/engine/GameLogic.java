package engine;

import common.PlayerTypes;

import java.util.List;
import java.util.Set;

/**
 * Created by user on 27/07/2018.
 */
public interface GameLogic {
    boolean play(int col, boolean popout);
    int playerTurns(int player);
    String timeFromBegining();
    int getCols();
    void loadSettingsFile(String filePath) throws Exception;
    char[][] boardReadyToPrint();
    void initPlayer(PlayerTypes playerType, int id, String name);
    PlayerTypes getTypeOfCurrentPlayer();
    boolean undoLastMove(); // returns false if there are no moves to undo, otherwise returns true
    int getNumberOfInitializedPlayers();
    int getIdOfCurrentPlayer();
    List<Move> getMovesHistory();
    boolean getHasWinner();
    Set<Integer> getWinners();
    boolean getIsBoardFull();
    void setBoardFromSettings(boolean restartPlayers);
    int getSequenceLength();
    int getNumberOfPlayersToInitialized();
}
