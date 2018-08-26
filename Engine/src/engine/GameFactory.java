package engine;

import common.*;

public class GameFactory {
    private String gameSettingsFullPath;
    private GameSettings gameSettings;



    public void loadSettingsFile(String filePath) throws Exception
    {
        try {
            gameSettings = new GameSettings(filePath);
        } catch (Exception e) {
            throw e;
        }
    }

    // play new game from a fresh game settings file
    public GameLogic getNewGame() throws Exception {
        GameLogic game = null;

        try {
            //loadSettingsFile(settingsFilePath);
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
        }
        catch (Exception e) {
            throw e;
        }

        return game;
    }

    // replaying game from game settings previously loaded
    /*
    public GameLogic getNewGame() {
        GameLogic game = null;

        try {
            game = getNewGame(gameSettingsFullPath);
        }
        catch (Exception e) { }

        return game;
    }
    */
}
