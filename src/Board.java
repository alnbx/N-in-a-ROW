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

    public boolean playMove(int row, int col, int player)
    {
        if (board[col].getFreeSpace() == 0) { return false; }

        board[col].playMove(row, player);

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

    static public void main(String[] args)
    {
        int x = 4;
        int y = 5;
        Board b = new Board(x, y);
    }
}
