package engine;

import common.GameSettings;
import common.GameVariant;

public class BasicGame extends Game {
    public BasicGame(GameSettings gameSettings) {
        super(gameSettings);
    }

    @Override
    public GameVariant getGameVariant() { return GameVariant.REGULAR; }
}
