import java.util.Map;
import java.util.Random;

/**
 * Created by user on 27/07/2018.
 */
public class Game implements gameLogic{

    private Board board;
    private int sequenceNumber;
    private boolean hasWinner;

    public Game()
    {
        board = new Board(4,4);
        this.sequenceNumber = 4;
        this.hasWinner = false;
    }


    @Override
    public boolean playHumanPlayer(int col, int player)
    {
        //TODO: if last move is legal - record it.
        if (board.playMove(col - 1, player))
        {
            if (checkWinningMove(col - 1, player)) {
                board.setWinner(player);
                board.setHasWinner(true);
                this.hasWinner = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean playComputerPlayer(int player)
    {
        Random r = new Random();
        int rand = r.nextInt(board.getCols());
        //TODO: Change the player number to something valid. We still do not know how to get the player number!
        board.playMove(rand, player);
        if (checkWinningMove(rand, player)) {
            board.setWinner(player);
            board.setHasWinner(true);
        }
        return false;
    }

    @Override
    public Map<Integer, Integer> getGameStats()
    {
        return null;
    }

    @Override
    public boolean play()
    {
        //TODO: do something
        return false;
    }

    @Override
    public void load()
    {

    }

    private boolean checkWinningMove(int col, int player)
    {
        //TODO: check how many discs is a win!
        return  board.leftRightSequence(col, player) == this.sequenceNumber ||
                board.upDownSequence(col, player) == this.sequenceNumber ||
                board.diagonalDownSequence(col, player) == this.sequenceNumber ||
                board.diagonalUpSequence(col, player) == this.sequenceNumber;
    }

    public char[][] boardReadyToPrint() { return board.getBoardAsCharArray(); }

    public static void main(String[] args)
    {
        Game g = new Game();
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
    }

    public boolean isHasWinner() { return this.hasWinner; }
}
