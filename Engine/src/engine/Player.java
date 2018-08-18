package engine;

import common.PlayersTypes;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    private int numOfTurnsPlayed;
    private String name;
    private int id;
    private PlayersTypes playerType;

    Player(int id, PlayersTypes playerType, String name) {
        this.playerType = playerType;
        this.id = id;
        this.name = name;
        this.isActive = true;
        this.numOfTurnsPlayed = 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isActive;

    public String getName() {
        return name;
    }


    public void increaseNumberOfTurnsPlayed() { numOfTurnsPlayed++; }

    // for UNDO
    boolean decreaseNumberOfTurnsPlayed(){
        boolean isLegalDecrease = false;
        if (numOfTurnsPlayed > 0) {
            numOfTurnsPlayed++;
            isLegalDecrease = true;
        }
        return isLegalDecrease;
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

    public int getPlayerTurns() { return this.numOfTurnsPlayed; }

    public void restart() { this.numOfTurnsPlayed = 0; }
}
