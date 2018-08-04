package engine;

/**
 * Created by user on 27/07/2018.
 */
public class Board {

    private int rows;
    private int cols;
    private Col[] board;
    private boolean hasWinner;
    private int winningPlayer;
    private int emptySpaces;

    public Board(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        board = new Col[cols];
        this.hasWinner = false;
        this.winningPlayer = 0;
        this.emptySpaces = this.rows * this.cols;

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

    public boolean undoMove(int col)
    {
        if (board[col].getLastRowInserted() == -1) { return false; }

        board[col].undoMove();

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
        for (int i  = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                res[i][j] = Character.forDigit(board[j].getDiscInCol(i).getPlayerDisc(), 10);
            }
        }

        return res;
    }

    private int getSequenceByDirection(Disc d, int playerDisc, Directions direction)
    {
        int res = 0;

        while (playerDisc == d.getPlayerDisc())
        {
            d = d.getDiscByDirection(direction);
            res++;
            if (null == d) { break; }
        }

        return res;
    }

    public int leftRightSequence(int col, int player)
    {
        int res = 0;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFT), player, Directions.LEFT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHT), player, Directions.RIGHT);

        return res;
    }

    public int upDownSequence(int col, int player)
    {
        //TODO: do something
        int res = 0;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.UP), player, Directions.UP);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.DOWN), player, Directions.DOWN);

        return res;
    }

    public int diagonalUpSequence(int col, int player)
    {
        int res = 0;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.UPRIGHT), player, Directions.UPRIGHT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTDOWN), player, Directions.LEFTDOWN);

        return res;
    }

    public int diagonalDownSequence(int col, int player)
    {
        int res = 0;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTUP), player, Directions.LEFTUP);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHTDOWN), player, Directions.RIGHTDOWN);

        return res;
    }

    public void decreaseEmptySpace() { this.emptySpaces--; }
    public boolean isFull() {return this.emptySpaces == 0; }
}
