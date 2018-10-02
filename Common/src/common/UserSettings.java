package common;

public class UserSettings {
    private String name;
    private int id;
    private PlayerTypes playerType;
    private String gameName;

    public UserSettings(String name, int id, PlayerTypes playerType) {
        this.name = name;
        this.id = id;
        this.playerType = playerType;
        this.gameName = null;
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

    public String getGame() {
        return this.gameName;
    }

    public void clearGame() {
        this.gameName = null;
    }

    public void setGame(String gameName) {
        this.gameName = gameName;
    }
}
