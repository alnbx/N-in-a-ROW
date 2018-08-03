package engine;

import common.PlayersTypes;

import java.util.Date;
import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public interface GameLogic {
    boolean play(int col);
    boolean playHumanPlayer(int col, int player);
    boolean playComputerPlayer(int player);
    int getPlayerNumber();
    int playerTurns(int player);
    String timeFromBegining();
    int getCols();
    void load(String filePath) throws Exception;
    char[][] boardReadyToPrint();
    void initPlayer(PlayersTypes playerType, int id, String name);
    PlayersTypes getTypeOfCurrentPlayer();
}
