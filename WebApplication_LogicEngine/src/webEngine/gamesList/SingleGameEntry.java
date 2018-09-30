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

    SingleGameEntry(GameSettings gameSettings, String userName) {
        this.game = null;
        this.gameName = gameSettings.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.gameSettings = gameSettings;
    }

    public boolean isPlayerListFull() {
        return gameSettings.isPlayerListFull();
    }

    public String getUserName() {
        return userName;
    }

    public int getNumRegisteredPlayers() {
        return gameSettings.getNumRegisteredPlayers();
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

    public void registerPlayer(PlayerSettings player) {
        gameSettings.addPlayer(player);
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

    public GameLogic getGameLogic() {
        return game;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.game = gameLogic;
    }
}
