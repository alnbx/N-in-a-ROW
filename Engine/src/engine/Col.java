package engine;

import java.io.Serializable;

/**
 * Created by user on 27/07/2018.
 */
public class Col implements Serializable {

    private int colNumber;
    private int freeSpace;
    private Disc[] discs;
    private int lastRowInserted;

    public Col(int colNumber, int discsInCol) {
        this.colNumber = colNumber;
        this.discs = new Disc[discsInCol];
        this.freeSpace = discsInCol;
        this.lastRowInserted = discs.length;

        initCol();
    }

    private void initCol()
    {
        for (int i = 0; i < discs.length; i++) {
            discs[i] = new Disc(new Position(i, this.colNumber), 0);
        }
    }

    public int getFreeSpace() { return this.freeSpace; }
    public int getColLength() { return discs.length; }
    public Disc getDiscInCol(int x) { return this.discs[x]; }

    public void connectDiscsLeft(Col left)
    {
        for (int i = 0; i < this.discs.length; i++) {
            this.discs[i].setDiscsAround(Directions.LEFT, left.getDiscInCol(i));
        }
    }

    public void restartCol() {
        for (int i = 0; i < this.discs.length; i++)
            this.discs[i].restartDisc();
    }

    public void connectDiscsLeftUp(Col leftUp)
    {
        for (int i = 1; i < this.discs.length; i++) {
            this.discs[i].setDiscsAround(Directions.LEFTUP, leftUp.getDiscInCol(i - 1));
        }
    }

    public void connectDiscsLeftDown(Col leftDown)
    {
        for (int i = 0; i < this.discs.length - 1; i++) {
            Disc temp = leftDown.getDiscInCol(i + 1);
            this.discs[i].setDiscsAround(Directions.LEFTDOWN, leftDown.getDiscInCol(i + 1));
        }
    }

    public void connectDiscsRight(Col right)
    {
        for (int i = 0; i < this.discs.length; i++) {
            this.discs[i].setDiscsAround(Directions.RIGHT, right.getDiscInCol(i));
        }
    }

    public void connectDiscsRightUp(Col rightUp)
    {
        for (int i = 1; i < this.discs.length; i++) {
            this.discs[i].setDiscsAround(Directions.UPRIGHT, rightUp.getDiscInCol(i - 1));
        }
    }

    public void connectDiscsRightDown (Col rightDown)
    {
        for (int i = 0; i < this.discs.length - 1; i++) {
            this.discs[i].setDiscsAround(Directions.RIGHTDOWN, rightDown.getDiscInCol(i + 1));
        }
    }

    public void connectDiscsUp()
    {
        for (int i = 1; i < this.discs.length; i++) {
            this.discs[i].setDiscsAround(Directions.UP, this.discs[i - 1]);
        }
    }

    public void connectDiscsDown()
    {
        for (int i = 0; i < this.discs.length - 1; i++) {
            this.discs[i].setDiscsAround(Directions.DOWN, this.discs[i + 1]);
        }
    }

    public void connectDiscsLeftRightCircular(Col edge)
    {
        for (int i = 0; i < this.discs.length - 1; i++)
        {
            this.discs[i].setDiscsAround(Directions.LEFT, edge.getDiscInCol(i));
            edge.discs[i].setDiscsAround(Directions.RIGHT, this.discs[i]);
        }
    }

    public void connectDiscsUpDownCircular()
    {
        this.discs[0].setDiscsAround(Directions.DOWN, this.discs[discs.length - 1]);
        this.discs[discs.length - 1].setDiscsAround(Directions.UP, this.discs[0]);
    }

    public void playMove(int player)
    {
        if (lastRowInserted > 0) {
            lastRowInserted--;
        }
        this.discs[lastRowInserted].setDiscOfPlayer(player);
        this.freeSpace--;
    }

    public int getLastRowInserted() { return this.lastRowInserted; }

    public void undoMove()
    {
        this.discs[lastRowInserted++].setDiscOfPlayer(0);
        this.freeSpace++;
    }

    public void dropDiscsDown()
    {
        for (int i = this.discs.length - 1; i > 0; i--) {
            this.discs[i].setDiscOfPlayer(this.discs[i - 1].getPlayerDisc());
        }

        this.discs[0].setDiscOfPlayer(0);
        this.lastRowInserted++;

        //TODO: call dropDiscsDown(this.discs.length - 1);
    }

    private void dropDiscsDown(int posInCol)
    {
        for (int i = posInCol; i > 0; i--) {
            this.discs[i].setDiscOfPlayer(this.discs[i-1].getPlayerDisc());
        }

        this.discs[0].setDiscOfPlayer(0);
        this.lastRowInserted++;
    }

    public int removeDiscsFromCol(int playerID)
    {
        int res = 0;

        for (int i = discs.length - 1; i >= 0; i--) {
            if (playerID == discs[i].getPlayerDisc()) {
                dropDiscsDown(i);
                res++;
            }
        }

        return res;
    }
}