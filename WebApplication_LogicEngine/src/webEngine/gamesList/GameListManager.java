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
        return gameEntriesMap.containsKey(gameSettings.getGameTitle());
    }

//    public void registerPlayerToGame(String gameName, UserSettings user) {
//        gameEntriesMap.get(gameName).registerPlayer(user);
//    }
//
//    public boolean isPlayersListFull(String gameName) {
//        SingleGameEntry sge = gameEntriesMap.get(gameName);
//        return sge.isPlayerListFull();
//    }
//
//    public void setGameStatus(String gameName, GameStatus gameStatus) {
//        gameEntriesMap.get(gameName).setGameStatus(gameStatus);
//    }
//
//    public SingleGameEntry getGameEntry(String gameName) {
//        return gameEntriesMap.get(gameName);
//    }
//
//    public String getGameName(String gameFile) {
//        return gameEntriesMap.get(gameFile).getGameName();
//    }
//
//    public void initGame(String gameName) {
//        SingleGameEntry gameEntry = gameEntriesMap.get(gameName);
//        if (gameEntry != null) {
//            gameEntry.setGameLogic(gameFactory.getNewGame(gameEntry.getGameSettings()));
//            gameEntry.setGameStatus(GameStatus.PLAYING);
//        }
//    }
//
//    public boolean isGameActive(String gameName) {
//        return gameEntriesMap.get(gameName).getGameStatus() == GameStatus.PLAYING;
//    }
//
//    public void enableGameForRegistration(String gameName) {
//        gameEntriesMap.get(gameName).enableGameForRegistration();
//    }
//
//    public Boolean isUserPlayerInGame(String gameName, String username) {
//        return gameEntriesMap.get(gameName).isUserPlayerInGame(username);
//    }
//
//    public List<UserSettings> getGameViewrs(String gameName) {
//        return gameEntriesMap.get(gameName).getGameViewers();
//    }
//
//    public void registerViewerToGame(String gameName, UserSettings user) {
//        gameEntriesMap.get(gameName).registerViewer(user);
//    }
//
//    public void viewerResigne(String gameName, String userName) {
//        gameEntriesMap.get(gameName).viewerResign(userName);
//    }
//
//    public GameStatus getGameStatus(String gameName) {
//        return gameEntriesMap.get(gameName).getGameStatus();
//    }
//
//    public int getGameId(String gameName) {
//        return gameEntriesMap.get(gameName).getGameId();
//    }
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
