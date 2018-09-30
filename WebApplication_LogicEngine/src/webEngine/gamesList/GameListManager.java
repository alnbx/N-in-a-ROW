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

    public synchronized void addGame(GameSettings gameSettings, String userName) throws Exception {
        SingleGameEntry game = new SingleGameEntry(gameSettings, userName);
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
        GameSettings gameSettings = new GameSettings(gameSettingsXml, false);
        return gameEntriesMap.containsKey(gameSettings.getGameTitle());
    }

    public void registerUserToGame(String gameName, PlayerSettings user) {
        gameEntriesMap.get(gameName).registerPlayer(user);

    }

    public boolean isPlayersListFull(String gameName) {
        SingleGameEntry sge = gameEntriesMap.get(gameName);
        return sge.isPlayerListFull();
    }

    public void setGameStatus(String gameName, GameStatus gameStatus) {
        gameEntriesMap.get(gameName).setGameStatus(gameStatus);
    }

    public SingleGameEntry getGameEntry(String gameName) {
        return gameEntriesMap.get(gameName);
    }

    public GameSettings getGameSettings(String gameFile) {
        return gameEntriesMap.get(gameFile).getGameSettings();
    }

    public String getGameName(String gameFile) {
        return gameEntriesMap.get(gameFile).getGameName();
    }

    private void setGameLogic(SingleGameEntry gameEntry) {
        gameEntry.setGameLogic(gameFactory.
                getNewGame(gameEntry.getGameSettings(), gameEntry.getRegisteredPlayers()));
    }

    public void initGame(String gameName) {
        SingleGameEntry gameEntry = gameEntriesMap.get(gameName);
        if (gameEntry != null) {
            setGameLogic(gameEntry);
            gameEntry.setGameStatus(GameStatus.PLAYING);
        }
    }
}
