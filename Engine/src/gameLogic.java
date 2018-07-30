import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public interface gameLogic {
    boolean play();
    boolean playHumanPlayer(int col, int player);
    boolean playComputerPlayer(int player);
    Map<Integer, Integer> getGameStats();
    void load();
    char[][] boardReadyToPrint();
}
