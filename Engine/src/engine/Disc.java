package engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 27/07/2018.
 */
public class Disc {

    private Position position;
    private int discOfPlayer;
    private Map<Directions, Disc> discsAround;

    public Disc(Position position, int discOfPlayer)
    {
        this. position = position;
        this.discOfPlayer = discOfPlayer;
        discsAround = new HashMap<>();
        initDiscsAround();
    }

    private void initDiscsAround()
    {
        discsAround.put(Directions.LEFT, null);
        discsAround.put(Directions.LEFTUP, null);
        discsAround.put(Directions.UP, null);
        discsAround.put(Directions.UPRIGHT, null);
        discsAround.put(Directions.RIGHT, null);
        discsAround.put(Directions.RIGHTDOWN, null);
        discsAround.put(Directions.DOWN, null);
        discsAround.put(Directions.LEFTDOWN, null);
    }

    public Position getPosition() { return this.position; }
    public void setPosition(Position x) { this.position.setPosition(x.getPositionRow(), x.getPositionCol()); }
    public int getPlayerDisc() { return discOfPlayer; }
    public Disc getDiscByDirection(Directions direction) { return discsAround.get(direction); }

    public void setDiscOfPlayer(int discOfPlayer) { this.discOfPlayer = discOfPlayer; }

    public void setDiscsAround(Directions direction, Disc disc) { this.discsAround.put(direction, disc); }
}
