import java.util.Map;
import java.util.Random;

/**
 * Created by user on 27/07/2018.
 */
public class Game implements gameLogic{

    private Board board;

    public Game()
    {
        board = new Board(4,4);
    }


    @Override
    public boolean playHumanPlayer(int row, int col, int player)
    {
        //TODO: if last move is legal - record it.
        if (board.playMove(row - 1, col - 1, player)) { return true; }
        return false;
    }

    @Override
    public boolean playComputerPlayer(int player)
    {
        Random r = new Random();
        //TODO: Change the player number to something valid. We still do not know how to get the player number!
        board.playMove(r.nextInt(board.getRows()), r.nextInt(board.getCols()), player);
        return false;
    }

    @Override
    public Map<Integer, Integer> getGameStats()
    {
        return null;
    }

    @Override
    public void loadBoard()
    {

    }

    public char[][] boardReadyToPrint() { return board.getBoardAsCharArray(); }

    public static void main(String[] args)
    {
        Game g = new Game();
        g.playHumanPlayer(1,1,1);
        g.playHumanPlayer(2,2,2);
        g.playHumanPlayer(1,2,1);
        char[][] b = g.boardReadyToPrint();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                System.out.print(b[i][j]);
            }
            System.out.print("\n");
        }
    }
}
