package common;

public class PlayerSettings {
    private String name;
    private int id;
    private PlayerTypes playerType;

    public PlayerSettings(String name, int id, PlayerTypes playerType) {
        this.name = name;
        this.id = id;
        this.playerType = playerType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }
}
