package webEngine.users;

import common.PlayerTypes;

public class SingleUserEntry {
    private String name;
    private int ID;
    private String gameRegisteredTo;
    private PlayerTypes playerType;

    public SingleUserEntry(String userName, int id, PlayerTypes playerType) {
        this.name = userName;
        this.gameRegisteredTo = ""; // assuming no game will be named with an empty string ""
        this.playerType = playerType;
        this.ID = id;
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

    public String getGameRegisteredTo() {
        return gameRegisteredTo;
    }

    public PlayerTypes getPlayerType() {

        return playerType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return this.ID;
    }
}
