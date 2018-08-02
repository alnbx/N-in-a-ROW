import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by user on 27/07/2018.
 */
public class Game implements gameLogic{

    private Board board;
    private int sequenceNumber;
    private boolean hasWinner;
    private boolean isFull;
    private Date startingTime;
    private boolean isFirstMove;

    public Game()
    {
        board = new Board(4,4);
        this.sequenceNumber = 4;
        this.hasWinner = false;
        this.isFull = false;
        this.startingTime = null;
    }


    @Override
    public boolean playHumanPlayer(int col, int player)
    {
        //TODO: if last move is legal - record it.
        if (board.playMove(col, player))
        {
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
    public boolean playComputerPlayer(int player)
    {
        Random r = new Random();
        int rand = r.nextInt(board.getCols());
        //TODO: Change the player number to something valid. We still do not know how to get the player number!

        while(!board.playMove(rand, player)) { rand = r.nextInt(board.getCols()); }

            if (checkWinningMove(rand, player)) {
                board.setWinner(player);
                board.setHasWinner(true);
            }

        return true;
    }

    @Override
    //TODO: return real player number!
    public int getPlayerNumber() { return 1; }

    @Override
    //TODO: return real player turns!
    public int playerTurns(int player) { return 1; }

    @Override
    public String timeFromBegining()
    {
        Long diff = 0L;
        Date now = new Date();

        diff = this.startingTime.getTime() - now.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;

        return String.format("%d:%d", diffMinutes, diffSeconds);
    }

    @Override
    public int getCols() { return this.board.getCols(); }

    @Override
    public boolean play(int col)
    {
        //TODO: do something

        board.decreaseEmptySpace();
        if (board.isFull()) { this.isFull = true; }

        if (!this.isFirstMove) {
            this.isFirstMove = true;
            setStartingTime();
        }

        return false;
    }

    private void setStartingTime() { this.startingTime = new Date(); }

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
    public boolean isBoardFull() { return this.isFull; }
}
