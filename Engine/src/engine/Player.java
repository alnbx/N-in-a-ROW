package engine;

import common.PlayerTypes;
import common.PlayerSettings;
import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    private int numOfMovesMade;
    private String name;
    private int id;
    private PlayerTypes playerType;
    private boolean isActive;

    Player(int id, PlayerTypes playerType, String name) {
        this.playerType = playerType;
        this.id = id;
        this.name = name;
        this.isActive = true;
        this.numOfMovesMade = 0;
    }

    Player(PlayerSettings player) {
        this.playerType = player.getPlayerType();
        this.id = player.getId();
        this.name = player.getName();
        this.isActive = true;
        this.numOfMovesMade = 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }


    public void increaseNumberOfTurnsPlayed() { numOfMovesMade++; }

    // for UNDO
    boolean decreaseNumberOfTurnsPlayed(){
        boolean isLegalDecrease = false;
        if (numOfMovesMade > 0) {
            numOfMovesMade++;
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

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public int getId() {
        return id;
    }

    public int getNumMovesMade() { return this.numOfMovesMade; }

    public void restart() { this.numOfMovesMade = 0; }
}
