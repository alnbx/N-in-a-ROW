
/**
 * Created by user on 27/07/2018.
 */
public class Col {

    private int colNumber;
    private int freeSpace;
    private Disc[] discs;

    public Col(int colNumber, int discsInCol) {
        this.colNumber = colNumber;
        this.discs = new Disc[discsInCol];
        this.freeSpace = discsInCol;
        initCol();
    }

    private void initCol()
    {
        for (int i = 0; i < discs.length; i++) { discs[i] = new Disc(new Position(colNumber, i), 0); }
    }

    public int getFreeSpace() { return this.freeSpace; }
    public Disc getDiscInCol(int x) { return this.discs[x]; }

    public void connectDiscsLeft(Col left)
    {
        for (int i = 0; i < this.discs.length; i++)
        {
            this.discs[i].setDiscsAround(Directions.LEFT, left.getDiscInCol(i));
        }
    }

    public void connectDiscsLeftUp(Col leftUp)
    {
        for (int i = 0; i < this.discs.length - 1; i++)
        {
            this.discs[i].setDiscsAround(Directions.LEFTUP, leftUp.getDiscInCol(i + 1));
        }
    }

    public void connectDiscsLeftDown(Col leftDown)
    {
        for (int i = 1; i < this.discs.length; i++)
        {
            this.discs[i].setDiscsAround(Directions.LEFTDOWN, leftDown.getDiscInCol(i - 1));
        }
    }

    public void connectDiscsRight(Col right)
    {
        for (int i = 0; i < this.discs.length; i++)
        {
            this.discs[i].setDiscsAround(Directions.RIGHT, right.getDiscInCol(i));
        }
    }

    public void connectDiscsRightUp(Col rightUp)
    {
        for (int i = 0; i < this.discs.length-1; i++)
        {
            this.discs[i].setDiscsAround(Directions.UPRIGHT, rightUp.getDiscInCol(i + 1));
        }
    }

    public void connectDiscsRightDown (Col rightDown)
    {
        for (int i = 1; i < this.discs.length; i++)
        {
            this.discs[i].setDiscsAround(Directions.LEFTDOWN, rightDown.getDiscInCol(i - 1));
        }
    }

    public void connectDiscsUp()
    {
        for (int i = 0; i < this.discs.length - 1; i++)
        {
            this.discs[i].setDiscsAround(Directions.UP, this.discs[i + 1]);
        }
    }

    public void connectDiscsDown()
    {
        for (int i = 1; i < this.discs.length; i++)
        {
            this.discs[i].setDiscsAround(Directions.DOWN, this.discs[i - 1]);
        }
    }
}
