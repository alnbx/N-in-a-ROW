import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public interface gameLogic {
    boolean playHumanPlayer(int row, int col, int player);
    boolean playComputerPlayer(int player);
    Map<Integer, Integer> getGameStats();
    void loadBoard();
    char[][] boardReadyToPrint();
}
