package engine;

import common.PlayerTypes;
import common.UserSettings;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    private UserSettings userSettings;
//    private String name;
//    private int id;
//    private PlayerTypes playerType;
    private boolean isActive;
    private int gameId;
    private int numOfMovesMade;

    public Player(UserSettings player) {
        this.userSettings = player;
        this.isActive = true;
        this.numOfMovesMade = 0;
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

    public int getGame() {
        return this.gameId;
    }

    public void clearGame() {
        this.gameId = 0;
    }

    public void setGame(int gameId) {
        this.gameId = gameId;
    }
}