import java.util.Date;
import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public interface gameLogic {
    boolean play(int col);
    boolean playHumanPlayer(int col, int player);
    boolean playComputerPlayer(int player);
    int getPlayerNumber();
    int playerTurns(int player);
    String timeFromBegining();
    int getCols();
    void load();
    char[][] boardReadyToPrint();
}
