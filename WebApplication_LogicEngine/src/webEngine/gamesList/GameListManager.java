package webEngine.gamesList;

import common.GameSettings;
import common.PlayerSettings;
import engine.GameFactory;
import engine.GameLogic;

import java.util.*;

public class GameListManager {
    private final Map<String, SingleGameEntry> gameEntriesMap;
    GameFactory gameFactory;

    public GameListManager() {
        this.gameFactory = new GameFactory(false);
        gameEntriesMap = new HashMap<>();
    }

    public synchronized void addGame(String gameFile, String userName) throws Exception {
        SingleGameEntry game = new SingleGameEntry(gameFactory.getNewGame(gameFile), userName);
        gameEntriesMap.put(game.getGameName(), game);
    }

    public synchronized void removeGame(String gameName) {
        gameEntriesMap.remove(gameName);
    }

    public synchronized List<SingleGameEntry> getGames() {
        List<SingleGameEntry> gamesList = new ArrayList<SingleGameEntry>();
        return Collections.unmodifiableList(gamesList);
    }

    public boolean isGameExists(String gameSettingsXml) throws Exception {
        Boolean isExists = false;

        try {
            GameSettings gameSettings = new GameSettings(gameSettingsXml, false);
            isExists = gameEntriesMap.containsKey(gameSettings.getGameTitle());
        }
        catch (Exception e) {
            throw e;
        }

        return isExists;
    }

    public void registerUserToGame(String gameNameFromParameter, PlayerSettings user) {
        gameEntriesMap.get(gameNameFromParameter).registerPlayer(user);

    }

    public boolean isPlayersListFull(String gameName) {
        SingleGameEntry sge = gameEntriesMap.get(gameName);
        return sge.getNumRegisteredPlayers() == sge.getNumRequiredPlayers();
    }

    public void setGameStatus(String gameName, GameStatus gameStatus) {
        gameEntriesMap.get(gameName).setGameStatus(gameStatus);
    }

    public GameLogic getGameLogic(String gameName) {
        return gameEntriesMap.get(gameName).getGameLogic();
    }
}
