package desktopApp;

import common.PlayerTypes;
import engine.Player;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TogglePlayerDisplayActive extends AbstractToggleTableItem {
    private String name;
    private int playerId;
    private PlayerTypes type;
    private SimpleIntegerProperty numMoves;
    private String color;
    public SimpleBooleanProperty isActive;
    private Player player;

    public TogglePlayerDisplayActive(Player p) {
        super(true);
        this.name = p.getName();
        this.playerId = p.getId();
        this.type = p.getPlayerType();
        this.numMoves = new SimpleIntegerProperty(0);
        this.isActive = new SimpleBooleanProperty(true);
        this.color = "dummy";
        this.player = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setType(PlayerTypes type) {
        this.type = type;
    }

    public void setNumMoves(int numMoves) {
        this.numMoves.set(numMoves);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public PlayerTypes getType() {
        return type;
    }

    public int getNumMoves() {
        return numMoves.get();
    }

    public SimpleIntegerProperty numMovesProperty() {
        return numMoves;
    }

    public String getColor() {
        return color;
    }

    public boolean isIsActive() {
        return isActive.get();
    }

    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }

    public Player getPlayer() {
        return player;
    }
}

