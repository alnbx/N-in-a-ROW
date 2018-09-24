package webEngine.gamesList;

import common.GameSettings;
import common.GameVariant;
import webEngine.users.SingleUserEntry;

import java.util.ArrayList;
import java.util.List;

public class SingleGameEntry {
    final private String gameName;
    final private String userName;
    final private String settingsFile;
    final private GameSettings gameSettings;
    private GameStatus gameStatus;
    private List<SingleUserEntry> registeredPlayers;

    SingleGameEntry(String settingsFile, String userName) throws Exception {
        this.settingsFile = settingsFile;
        this.gameSettings = new GameSettings(settingsFile);
        this.gameName = gameSettings.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.registeredPlayers = new ArrayList<>();
    }


    public String getUserName() {
        return userName;
    }

    public int getNumRegisteredPlayers() {
        return registeredPlayers.size();
    }

    public int getNumRequiredPlayers() {
        return gameSettings.getNumOfPlayers();
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameName() {
        return gameName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void registerPlayer(SingleUserEntry player) {
        registeredPlayers.add(player);
    }

    public int getRows() {
        return gameSettings.getNumRows();
    }

    public int getCols() {
        return gameSettings.getNumCols();
    }

    public int getSequenceLength() {
        return gameSettings.getTarget();
    }

    public GameVariant getGameVariant() {
        return gameSettings.getGameVariant();
    }

    public int getNumberOfRequiredPlayers() {
        return gameSettings.getNumOfPlayers();
    }

    public List<SingleUserEntry> getRegisteredUsers() {
        return registeredPlayers;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
}
