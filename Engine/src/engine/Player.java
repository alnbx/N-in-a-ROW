package engine;

import common.PlayerTypes;
import common.UserSettings;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    private UserSettings userSettings;
    private boolean isActive;
    private int numOfMovesMade;
    private boolean current;

    public Player(UserSettings player) {
        this.userSettings = player;
        this.isActive = true;
        this.numOfMovesMade = 0;
        this.current = false;
    }

    public int getMoves() {
        return numOfMovesMade;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return userSettings.getName();
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
        return userSettings.getId() == player.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSettings.getId());
    }

    public PlayerTypes getPlayerType() {
        return userSettings.getPlayerType();
    }

    public int getId() {
        return userSettings.getId();
    }

    public int getNumMovesMade() { return this.numOfMovesMade; }

    public void restart() { this.numOfMovesMade = 0; }

    public void deactivatePlayer() { this.isActive = false; }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}