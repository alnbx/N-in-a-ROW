package webEngine.games;

import common.GameSettings;
import engine.GameFactory;
import engine.GameLogic;

import java.util.*;

public class GameListManager {
    private final Map<String, SingleGameEntry> gamesMap;
    private final GameFactory gameFactory;

    public GameListManager() {
        gameFactory = new GameFactory();
        gamesMap = new HashMap<>();
    }

    public synchronized void addGame(String gameFile, String userName) throws Exception {
        GameLogic game = gameFactory.getNewGame(gameFile);
        gamesMap.put(game.getGameTitle(), new SingleGameEntry(game, userName));
    }

    public synchronized void removeUser(String gameName) {
        gamesMap.remove(gameName);
    }

    public synchronized List<SingleGameEntry> getGames() {
        List<SingleGameEntry> gamesList = new ArrayList<SingleGameEntry>();
        return Collections.unmodifiableList(gamesList);
    }

    public boolean isGameExists(String settingFile) throws Exception {
        Boolean isExists = false;

        try {
            GameSettings gameSettings = new GameSettings(settingFile);
            isExists = gamesMap.containsKey(gameSettings.getGameTitle());
        }
        catch (Exception e) {
            throw e;
        }

        return isExists;
    }
}
