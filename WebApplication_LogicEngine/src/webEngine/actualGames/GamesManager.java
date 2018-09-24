package webEngine.actualGames;

import engine.GameFactory;
import engine.GameLogic;
import engine.Move;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GamesManager {
    Map<String, SingleGame> gamesMap;
    GameFactory gameFactory;

    public GamesManager() {
        this.gamesMap = new HashMap<>();
        this.gameFactory = new GameFactory();
    }

    public void addGame(String settingsFile){
        try {
            GameLogic game = gameFactory.getNewGame(settingsFile);
            gamesMap.put(game.getGameTitle(), new SingleGame(game));
        }
        catch (Exception e) {
            // this should not happen, as gamesMap are initialized only after they were
            // successfully added to te gamesMap list
        }
    }

    public void removeGame(String gameName) {
        gamesMap.remove(gameName);
    }

    public SingleGame getGame(String gameName) {
        return gamesMap.get(gameName);
    }

}
