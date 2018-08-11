package engine;

import common.PlayersTypes;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public interface GameLogic {
    boolean play(int col);
    int playerTurns(int player);
    String timeFromBegining();
    int getCols();
    void loadSettingsFile(String filePath) throws Exception;
    char[][] boardReadyToPrint();
    void initPlayer(PlayersTypes playerType, int id, String name);
    PlayersTypes getTypeOfCurrentPlayer();
    boolean undoLastMove(); // returns false if there are no moves to undo, otherwise returns true
    int getNumberOfInitializedPlayers();
    int getIdOfCurrentPlayer();
    List<Move> getMovesHistory();
    boolean getHasWinner();
    boolean getIsBoardFull();
    void setBoardFromSettings(boolean restartPlayers);
    int getSequenceLength();
    int getNumberOfPlayersToInitialized();
}
