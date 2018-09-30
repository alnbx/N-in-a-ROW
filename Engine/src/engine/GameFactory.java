package engine;

import common.*;
import java.util.List;

public class GameFactory {
    private String gameSettingsFullPath;
    private GameSettings gameSettings;
    private final Boolean isGameSettingsFilePath;

    public GameFactory(boolean isGameSettingsFilePath) {
        this.isGameSettingsFilePath = isGameSettingsFilePath;
    }

    public void loadSettingsFile(String filePath) throws Exception
    {
        gameSettingsFullPath = filePath;
        gameSettings = new GameSettings(filePath, isGameSettingsFilePath);
    }

    // play new game from a fresh game settings file
    public GameLogic getNewGame() {
        return getGameLogicFromSettings(this.gameSettings);
    }

    public GameLogic getGameLogicFromSettings(GameSettings gameSettings) {
        GameLogic game = null;

        switch (gameSettings.getGameVariant()) {
            case POPOUT:
                game = new PopoutGame(gameSettings);
                break;
            case REGULAR:
                game = new BasicGame(gameSettings);
                break;
            case CIRCULAR:
                game = new CircularGame(gameSettings);
                break;
            default:
                break;
        }
        return game;
    }

    public GameLogic getNewGame(GameSettings gameSettings, List<PlayerSettings> playersSettings) {
        GameLogic game = getGameLogicFromSettings(gameSettings);
        for (PlayerSettings ps: playersSettings) {
            game.addPlayer(ps);
        }

        return game;
    }
}
