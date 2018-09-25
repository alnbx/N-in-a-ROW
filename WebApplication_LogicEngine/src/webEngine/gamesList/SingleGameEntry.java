package webEngine.gamesList;

import common.GameSettings;
import common.GameVariant;
import common.PlayerSettings;
import engine.GameLogic;

import java.util.ArrayList;
import java.util.List;

public class SingleGameEntry {
    final private GameLogic game;
    final private String gameName;
    final private String userName;
    //final private String settingsFile;
    //final private GameSettings gameSettings;
    private GameStatus gameStatus;
    //private List<PlayerSettings> registeredPlayers;

    SingleGameEntry(GameLogic game, String userName) throws Exception {
        //this.settingsFile = gameSettingsXml;
        //this.gameSettings = new GameSettings(gameSettingsXml, false);
        //this.gameName = gameSettings.getGameTitle();
        this.game = game;
        this.gameName = game.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        //this.registeredPlayers = new ArrayList<>();
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
}
