package engine;

import common.PlayersTypes;
import java.util.Objects;
import java.lang.Exception;

public class Player {
    private int numOfTurnsPlayed;
    private String name;
    private int id;
    private PlayersTypes playerType;

    Player(int id, PlayersTypes playerType, String name) {
        this.playerType = playerType;
        this.id = id;
        this.name = name;
        numOfTurnsPlayed = 0;
    }

    public void increaseNumberOfTurnsPlayed() { numOfTurnsPlayed++; }

    // for UNDO implementaion?
    public void decreaseNumberOfTurnsPlayed() throws Exception {
        if (numOfTurnsPlayed > 0)
            numOfTurnsPlayed++;
        else
            throw new Exception("player didn't play any turns, so can't decrease turns count");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public PlayersTypes getPlayerType() {
        return playerType;
    }

    public int getId() {
        return id;
    }
}
