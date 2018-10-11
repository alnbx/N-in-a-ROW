package common;

public class UserSettings {
    private String name;
    private int id;
    private PlayerTypes playerType;
    //private int gameId;

    public UserSettings(String name, int id, PlayerTypes playerType) {
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
