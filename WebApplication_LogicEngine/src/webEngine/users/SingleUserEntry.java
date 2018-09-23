package webEngine.users;

import common.PlayerTypes;

public class SingleUserEntry {
    private String name;
    private String gameRegisteredTo;
    private PlayerTypes playerType;

    public SingleUserEntry(String userName, PlayerTypes playerType) {
        this.name = userName;
        this.gameRegisteredTo = ""; // assuming no game will be named with an empty string ""
        this.playerType = playerType;
    }

    public boolean isUserRegisteredToGame() {
        return gameRegisteredTo.equals("");
    }

    public void unRegisterUserFromGame() {
        gameRegisteredTo = "";
    }

    public void registerUserToGame(String gameName) {
        gameRegisteredTo = gameName;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public String getName() {
        return name;
    }
}
