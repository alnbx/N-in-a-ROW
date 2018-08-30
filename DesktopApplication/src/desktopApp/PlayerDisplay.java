package desktopApp;

import common.PlayerTypes;
import engine.Player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

public class PlayerDisplay {
    private String name;
    private int id;
    private PlayerTypes type;
    //private int numMoves;
    private SimpleIntegerProperty numMoves;
    private String color;
    private boolean isActive;
    private Player player;

    public PlayerDisplay(Player p) {
        this.name = p.getName();
        this.id = p.getId();
        this.type = p.getPlayerType();
        //this.numMoves = p.getNumMovesMade();
        this.numMoves = new SimpleIntegerProperty();
        this.numMoves.set(0);
        this.isActive = true;
        this.player = p;
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

//    public int getNumMoves() {
//        return numMoves;
//    }
    public int getNumMoves() {
        return numMoves.get();
    }

    public String getColor() {
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

//    public void setNumMoves(int numMoves) {
//        this.numMoves = numMoves;
//    }
    public void setNumMoves(int numMoves) {
        this.numMoves.set(numMoves);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}