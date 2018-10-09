package webEngine.gamesList;

import common.GameSettings;
import common.UserSettings;
import engine.GameFactory;

import java.util.*;

public class GameListManager {
    private static int gamesCount = 0;
    private final Map<Integer, SingleGameEntry> gameEntriesMap;
    GameFactory gameFactory;

    public GameListManager() {
        this.gameFactory = new GameFactory(false);
        gameEntriesMap = new HashMap<>();
    }

    // returns the newly created game Id, which identifies the game that was added
    public synchronized int addGame(GameSettings gameSettings, String userName) throws Exception {
        SingleGameEntry gameEntry = new SingleGameEntry(gameSettings, userName, ++gamesCount);
        gameEntry.setGameLogic(gameFactory.getNewGame(gameSettings));
        gameEntriesMap.put(gamesCount, gameEntry);
        return gamesCount;
    }

    public synchronized List<SingleGameEntry> getGames() {
        List<SingleGameEntry> gamesList = new ArrayList<>(gameEntriesMap.values());
        return Collections.unmodifiableList(gamesList);
    }

    public boolean isGameExists(String gameSettingsXml) throws Exception {
        GameSettings gameSettings = new GameSettings(gameSettingsXml, false);
        for (SingleGameEntry sge : gameEntriesMap.values()) {
            if (sge.getGameName().equalsIgnoreCase(gameSettings.getGameTitle()))
                return true;
        }
        return false;
    }

    public void registerPlayerToGame(int gameId, UserSettings user) {
        gameEntriesMap.get(gameId).registerPlayer(user);
    }

    public boolean isPlayersListFull(int gameId) {
        SingleGameEntry sge = gameEntriesMap.get(gameId);
        return sge.isPlayerListFull();
    }

    public void setGameStatus(int gameId, GameStatus gameStatus) {
        gameEntriesMap.get(gameId).setGameStatus(gameStatus);
    }

    public SingleGameEntry getGameEntry(int gameId) {
        return gameEntriesMap.get(gameId);
    }

    public String getGameName(int gameId) {
        return gameEntriesMap.get(gameId).getGameName();
    }

    public void initGame(int gameId) {
        SingleGameEntry gameEntry = gameEntriesMap.get(gameId);
        if (gameEntry != null) {
            gameEntry.getGameLogic().setPlayersFromSettings(true);
            gameEntry.setGameStatus(GameStatus.PLAYING);
        }
    }

    public boolean isGameActive(int gameId) {
        return gameEntriesMap.get(gameId).getGameStatus() == GameStatus.PLAYING;
    }

    public void enableGameForRegistration(int gameId) {
        gameEntriesMap.get(gameId).enableGameForRegistration();
    }

    public Boolean isUserPlayerInGame(int gameId, String username) {
        return gameEntriesMap.get(gameId).isUserPlayerInGame(username);
    }

    public List<UserSettings> getGameViewrs(int gameId) {
        return gameEntriesMap.get(gameId).getGameViewers();
    }

    public void registerViewerToGame(int gameId, UserSettings user) {
        gameEntriesMap.get(gameId).registerViewer(user);
    }

    public void viewerResigne(int gameId, String userName) {
        gameEntriesMap.get(gameId).viewerResign(userName);
    }

    public GameStatus getGameStatus(int gameId) {
        return gameEntriesMap.get(gameId).getGameStatus();
    }

    public int getGameId(String gameName) {
        return gameEntriesMap.get(gameName).getGameId();
    }
}
