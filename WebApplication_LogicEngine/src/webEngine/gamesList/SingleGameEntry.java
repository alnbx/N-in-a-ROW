package webEngine.gamesList;

import common.GameSettings;
import common.GameVariant;
import common.PlayerSettings;
import engine.GameLogic;

import java.util.ArrayList;
import java.util.List;

public class SingleGameEntry {
    final private String userName;
    final private GameSettings gameSettings;
    private GameLogic game;
    private String gameName;
    private GameStatus gameStatus;
    private List<PlayerSettings> registeredPlayers;

    SingleGameEntry(GameSettings gameSettings, String userName) {
        this.game = null;
        this.gameName = gameSettings.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.gameSettings = gameSettings;
        this.registeredPlayers = new ArrayList<>();
    }

    public boolean isPlayerListFull() {
        return gameSettings.getNumOfPlayers() == registeredPlayers.size();
    }

    public List<PlayerSettings> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public String getUserName() {
        return userName;
    }

    public int getNumRegisteredPlayers() {
        return game.getNumberOfInitializedPlayers();
    }

    public int getNumRequiredPlayers() {
        return game.getNumberOfRequiredPlayers();
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

    public void registerPlayer(PlayerSettings player) {
        game.addPlayer(player);
    }

    public int getRows() {
        return game.getRows();
    }

    public int getCols() {
        return game.getCols();
    }

    public int getSequenceLength() {
        return game.getSequenceLength();
    }

    public GameVariant getGameVariant() {
        return game.getGameVariant();
    }

    public GameLogic getGameLogic() {
        return game;
    }

    public GameSettings getGameSettings() {
        return game.getGameSettings();
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.game = gameLogic;
    }
}
