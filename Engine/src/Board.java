/**
 * Created by user on 27/07/2018.
 */
public class Board {

    private int rows;
    private int cols;
    private Col[] board;
    private boolean hasWinner;
    private int winningPlayer;

    public Board(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        board = new Col[cols];
        this.hasWinner = false;
        this.winningPlayer = 0;

        initBoard();
    }

    private void initBoard()
    {
        for (int i = 0; i < this.cols; i++) { board[i] = new Col(i, this.rows); }

        connectBoard();
    }

    private void connectBoard()
    {
        for (int i = 0; i < this.cols; i++)
        {
            connectDiscsToLeft(i);
            connectDiscsToRight(i);
            connectDiscsToDownUp(i);
        }
    }

    private void connectDiscsToLeft(int loc)
    {
        if (loc != 0)
        {
            board[loc].connectDiscsLeft(board[loc - 1]);
            board[loc].connectDiscsLeftDown(board[loc - 1]);
            board[loc].connectDiscsLeftUp(board[loc - 1]);
        }
    }

    private void connectDiscsToRight(int loc)
    {
        if (loc != this.cols - 1)
        {
            board[loc].connectDiscsRight(board[loc + 1]);
            board[loc].connectDiscsRightUp(board[loc + 1]);
            board[loc].connectDiscsRightDown(board[loc + 1]);
        }
    }

    private void connectDiscsToDownUp(int loc)
    {
        board[loc].connectDiscsDown();
        board[loc].connectDiscsUp();
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }

    public void setHasWinner(boolean hasWinner) { this.hasWinner = hasWinner; }

    public boolean playMove(int col, int player)
    {
        if (board[col].getFreeSpace() == 0) { return false; }

        board[col].playMove(player);

        return true;
    }

    //TODO: check if necessary! Delete it if not!
    public Col[] getBoard() { return this.board; }
    public void setWinner(int winningPlayer) { this.hasWinner = true; this.winningPlayer = winningPlayer; }
    public boolean isHasWinner() { return this.hasWinner; }
    public int getWinner() { return this.winningPlayer; }
    public char[][] getBoardAsCharArray()
    {
        char[][] res = new char[this.rows][this.cols];
        for (int i  = 0; i < this.cols; i++)
        {
            for (int j = 0; j < this.rows; j++)
            {
                res[i][j] = Character.forDigit(board[i].getDiscInCol(j).getPlayerDisc(), 10);
            }
        }

        return res;
    }

    public int leftRightSequence(int col, int player)
    {
        int res = 0;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());

        while (player != d.getPlayerDisc())
        {
            d = d.getDiscByDirection(Directions.LEFT);
            res++;
        }

        d = board[col].getDiscInCol(board[col].getLastRowInserted());

        while (player != d.getPlayerDisc())
        {
            d = d.getDiscByDirection(Directions.RIGHT);
            res++;
        }

        return res;
    }

    public int upDownSequence(int col, int player)
    {
        //TODO: do something
        return 0;
    }

    public int diagonalUpSequence(int col, int player)
    {
        //TODO: do something
        return 0;
    }

    public int diagonalDownSequence(int col, int player)
    {
        //TODO: do something
        return 0;
    }
}
