package webEngine.actualGames;

import common.GameSettings;
import common.PlayerSettings;
import engine.GameFactory;
import engine.GameLogic;
import engine.Move;
import engine.Player;

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

    public String addGame(String settingsFile){
        String gameName = "";
        try {
            GameLogic game = gameFactory.getNewGame(settingsFile);
            gameName = game.getGameTitle();
            gamesMap.put(game.getGameTitle(), new SingleGame(game));
        }
        catch (Exception e) {
            // this should not happen, as gamesMap are initialized only after they were
            // successfully added to te gamesMap list
        }

        return gameName;
    }

    public void removeGame(String gameName) {
        gamesMap.remove(gameName);
    }

    public SingleGame getGame(String gameName) {
        return gamesMap.get(gameName);
    }

    public void addPlayerToGame(String gameName, PlayerSettings playerSettings) {
        gamesMap.get(gameName).addPlayer(playerSettings);
    }
}
