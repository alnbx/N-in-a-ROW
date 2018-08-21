package engine;

import java.io.Serializable;

/**
 * Created by user on 27/07/2018.
 */
public class Position implements Serializable {

    private int row;
    private int col;

    public Position()
    {
        this.row = 0;
        this.col = 0;
    }

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getPositionRow() { return this.row; }
    public int getPositionCol() { return this.col; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
