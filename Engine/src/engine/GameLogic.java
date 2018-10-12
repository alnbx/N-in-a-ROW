package engine;

import common.GameVariant;
import common.PlayerTypes;
import common.UserSettings;

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
    int getRows();
    void loadSettingsFile(String filePath) throws Exception;
    char[][] boardReadyToPrint();
    //void initPlayer(PlayerTypes playerType, int id, String name);
    PlayerTypes getTypeOfCurrentPlayer();
    boolean undoLastMove(); // returns false if there are no moves to undo, otherwise returns true
    int getNumberOfInitializedPlayers();
    int getIdOfCurrentPlayer();
    List<Move> getMovesHistory();
    boolean getHasWinner();
    Set<Integer> getWinners();
    boolean getIsBoardFull();
    void setRoundFromSettings(boolean restartPlayers);
    int getSequenceLength();
    int getNumberOfRequiredPlayers();
    boolean isPopout();
    List<Player> getPlayers();
    Move getLastMove();
    void resignPlayer();
    int getNumberOfActivePlayers();
    GameVariant getGameVariant();
    int getNumberOfRoundsPlayed();
    void increaseRoundPlayed();
    Boolean isTie();
    int[][] getBoardAsIntArr();
    int[][] getEmptyBoardAsIntArr();
    void setPlayersFromSettings(boolean isNewPlayers);
    String getPlayerName(int playerId);
    String getGameName();
    boolean isPlayerListFull();
    int getNumRegisteredPlayers();
    void addPlayer(UserSettings player);
    boolean isUserPlayerInGame(String userName);
    void clearRegisteredPlayers();
    List<UserSettings> getRegisteredPlayers();
    List<UserSettings> getRegisteredViewers();
    void clearRegisteredViewers();
    void removePlayerFromRegisteredPlayers(int playerId);
}

