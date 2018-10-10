package engine;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 27/07/2018.
 */
public class Board implements Serializable {

    private int rows;
    private int cols;
    private Col[] board;
    private boolean hasWinner;
    private Set<Integer> winningPlayer;
    private int emptySpaces;
    private boolean isCircular;

    public Board(int rows, int cols, boolean isCircular)
    {
        this.rows = rows;
        this.cols = cols;
        board = new Col[cols];
        this.hasWinner = false;
        this.winningPlayer = new HashSet<Integer>();
        this.emptySpaces = this.rows * this.cols;
        this.isCircular = isCircular;

        initBoard();
    }

    private void initBoard()
    {
        for (int i = 0; i < this.cols; i++) {
            board[i] = new Col(i, this.rows);
        }

        connectBoard();
    }

    public void restart()
    {
        this.emptySpaces = this.rows * this.cols;

        for (int i = 0; i < this.cols; i++) { board[i].restartCol(); }
    }

    public int[][] getBoardAsIntArray() {
        int[][] res = new int[this.rows][this.cols];
        for (int row  = 0; row < this.rows; row++)
        {
            for (int col = 0; col < this.cols; col++) {
                res[row][col] = getPlayerInDisc(col, row);
            }
        }

        return res;
    }

    private void connectBoard()
    {
        for (int i = 0; i < this.cols; i++)
        {
            connectDiscsToLeft(i);
            connectDiscsToRight(i);
            connectDiscsToDownUp(i);
        }

        if (this.isCircular) { connectDiscsCircular(); }
    }

    private void connectDiscsCircular() {
        board[0].connectDiscsLeftRightCircular(board[this.cols - 1]);

        //connect UpDown
        for (int i = 0; i < this.cols; i++) { board[i].connectDiscsUpDownCircular(); }
    }

    private void connectDiscsToLeft(int col)
    {
        if (col != 0) {
            board[col].connectDiscsLeft(board[col - 1]);
            board[col].connectDiscsLeftDown(board[col - 1]);
            board[col].connectDiscsLeftUp(board[col - 1]);
        }
    }

    private void connectDiscsToRight(int col)
    {
        if (col != this.cols - 1) {
            board[col].connectDiscsRight(board[col + 1]);
            board[col].connectDiscsRightUp(board[col + 1]);
            board[col].connectDiscsRightDown(board[col + 1]);
        }
    }

    private void connectDiscsToDownUp(int col)
    {
        board[col].connectDiscsDown();
        board[col].connectDiscsUp();
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
        if (board[col].getLastRowInserted() == board[col].getColLength()) { return false; }

        board[col].undoMove();

        return true;
    }

    //TODO: check if necessary! Delete it if not!
    public Col[] getBoard() { return this.board; }
    public void setWinner(int winningPlayer)
    {
        this.hasWinner = true;
        this.winningPlayer.add(winningPlayer);
    }

    public void setWinner(Set<Integer> winningPlayer)
    {
        this.hasWinner = true;
        this.winningPlayer = winningPlayer;
    }

    public boolean isHasWinner() { return this.hasWinner; }
    public Set<Integer> getWinner() { return this.winningPlayer; }

    public char[][] getBoardAsCharArray()
    {
        char[][] res = new char[this.rows][this.cols];
        for (int i  = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++) {
                res[i][j] = Character.forDigit(board[j].getDiscInCol(i).getPlayerDisc(), 10);
            }
        }

        return res;
    }

    private int getSequenceByDirection(Disc d, int playerDisc, Directions direction)
    {
        int res = 0;

        while ((null != d) && (playerDisc == d.getPlayerDisc()))
        {
            d = d.getDiscByDirection(direction);
            res++;
        }

        return res;
    }

    public int leftRightSequence(int col, int player)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFT), player, Directions.LEFT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHT), player, Directions.RIGHT);

        return res;
    }

    public int leftRightSequencePopout(int col, int row)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(row);
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFT), temp.getPlayerDisc(), Directions.LEFT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHT), temp.getPlayerDisc(), Directions.RIGHT);

        return res;
    }

    public int upDownSequence(int col, int player)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        //Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(d.getDiscByDirection(Directions.UP), player, Directions.UP);
        Disc temp = d;
        res += getSequenceByDirection(d.getDiscByDirection(Directions.DOWN), player, Directions.DOWN);

        return res;
    }

    public int upDownSequencePopout(int col, int row)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(row);
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.UP), temp.getPlayerDisc(), Directions.UP);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.DOWN), temp.getPlayerDisc(), Directions.DOWN);

        return res;
    }

    public int diagonalUpSequence(int col, int player)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.UPRIGHT), player, Directions.UPRIGHT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTDOWN), player, Directions.LEFTDOWN);

        return res;
    }

    public int diagonalUpSequencePopout(int col, int row)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(row);
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.UPRIGHT), temp.getPlayerDisc(), Directions.UPRIGHT);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTDOWN), temp.getPlayerDisc(), Directions.LEFTDOWN);

        return res;
    }

    public int diagonalDownSequence(int col, int player)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(board[col].getLastRowInserted());
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTUP), player, Directions.LEFTUP);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHTDOWN), player, Directions.RIGHTDOWN);

        return res;
    }

    public int diagonalDownSequencePopout(int col, int row)
    {
        int res = 1;
        Disc d = board[col].getDiscInCol(row);
        Disc temp = d;

        if (0 == d.getPlayerDisc()) { return 0; }

        res += getSequenceByDirection(temp.getDiscByDirection(Directions.LEFTUP), temp.getPlayerDisc(), Directions.LEFTUP);
        temp = d;
        res += getSequenceByDirection(temp.getDiscByDirection(Directions.RIGHTDOWN), temp.getPlayerDisc(), Directions.RIGHTDOWN);

        return res;
    }

    public void decreaseEmptySpace() { this.emptySpaces--; }
    public void increaseEmptySpace() { this.emptySpaces++; }
    public boolean isFull() {return this.emptySpaces == 0; }

    public boolean isPopoutAvaliableForPlayer(int playerID)
    {
        for (int i = 0; i < board.length; i++) {
            if (board[i].getDiscInCol(this.rows - 1).getPlayerDisc() == playerID) { return true; }
        }

        return false;
    }

    public boolean playPopoutMove(int col, int playerID)
    {
        if (board[col].getDiscInCol(rows - 1).getPlayerDisc() != playerID) {
            return false;
        }

        board[col].dropDiscsDown();
        this.emptySpaces++;

        return true;
    }

    public int getPlayerInDisc(int col, int row) { return board[col].getDiscInCol(row).getPlayerDisc(); }

    public void removeAllDiscsofPlayer(int playerID)
    {
        for (int i = 0; i < board.length; i++) {
            this.emptySpaces += board[i].removeDiscsFromCol(playerID);
        }
    }

    public int[][] getEmptyBoardAsIntArr() {
        int[][] res = new int[this.rows][this.cols];
        for (int row  = 0; row < this.rows; row++)
        {
            for (int col = 0; col < this.cols; col++) {
                res[row][col] = 0;
            }
        }

        return res;
    }
}
