package desktopApp;

import common.PlayerTypes;
import engine.Player;

import javafx.scene.paint.Color;

public class PlayerDisplay {
    private String name;
    private int id;
    private PlayerTypes type;
    private int numMoves;
    private Color color;
    private boolean isActive;

    public PlayerDisplay(Player p) {
        this.name = p.getName();
        this.id = p.getId();
        this.type = p.getPlayerType();
        this.numMoves = p.getNumMovesMade();
        this.isActive = true;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public PlayerTypes getType() {
        return type;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public Color getColor() {
        return color;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(PlayerTypes type) {
        this.type = type;
    }

    public void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
