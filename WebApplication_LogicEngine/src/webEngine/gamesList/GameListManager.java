package webEngine.gamesList;

import common.GameSettings;
import common.UserSettings;
import engine.GameFactory;
import engine.Move;

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
        SingleGameEntry gameEntry = new SingleGameEntry(gameFactory.getNewGame(gameSettings), userName, ++gamesCount);
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

    public void startGame(int gameId) {
        gameEntriesMap.get(gameId).startNewGame();
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

    public List<UserSettings> getActiveViewrs(int gameId) {
        return gameEntriesMap.get(gameId).getActiveViewers();
    }

    public void registerViewerToGame(int gameId, UserSettings user) {
        gameEntriesMap.get(gameId).registerViewer(user);
    }

    public void viewerResign(int gameId, String userName) {
        gameEntriesMap.get(gameId).viewerResign(userName);
    }

    public GameStatus getGameStatus(int gameId) {
        return gameEntriesMap.get(gameId).getGameStatus();
    }

    public int getGameId(String gameName) {
        return gameEntriesMap.get(gameName).getGameId();
    }

    public void setIsTie(int gameId, Boolean isTie) {
        gameEntriesMap.get(gameId).setIsTie(isTie);
    }

    public void setWinners(int gameId, Set<String> winners) {
        gameEntriesMap.get(gameId).setWinners(winners);
    }

    public List<String> getAllGamePlayersAndViewers(int gameId) {
        return gameEntriesMap.get(gameId).getAllGamePlayersAndViewers();
    }

    public boolean isGameEnded(int gameId) {
        return gameEntriesMap.get(gameId).isGameEnded();
    }

    public boolean getGameHasWinner(int gameId) {
        return gameEntriesMap.get(gameId).getHasWinner();
    }

    public boolean getGameIsTie(int gameId) {
        return gameEntriesMap.get(gameId).getIsTie();
    }

    public Set<String> getGameWinners(int gameId) {
        return gameEntriesMap.get(gameId).getWinners();
    }

    public List<Move> getMovesHistory(int gameId) {
        return gameEntriesMap.get(gameId).getMovesHistory();
    }

    public List<UserSettings> getActivePlayers(int gameId) {
        return gameEntriesMap.get(gameId).getActivePlayers();
    }

    public List<UserSettings> getRegisteredPlayers(int gameId) {
        return gameEntriesMap.get(gameId).getRegisteredPlayers();
    }

    public int getGameRows(int gameId) {
        return gameEntriesMap.get(gameId).getRows();
    }

    public int getGameCols(int gameId) {
        return gameEntriesMap.get(gameId).getCols();
    }

    public int[][] getGameBoardData(int gameId) {
        return gameEntriesMap.get(gameId).getBoardData();
    }

    public int getIdOfCurrentPlayer(int gameId) {
        return gameEntriesMap.get(gameId).getIdOfCurrentPlayer();
    }

    public int[][] getEmptyGameBoardData(int gameId) {
        return gameEntriesMap.get(gameId).getEmptyGameBoardData();
    }

    public List<UserSettings> getRegisteredViewers(int gameId) {
        return gameEntriesMap.get(gameId).getRegisteredViewers();
    }
}
