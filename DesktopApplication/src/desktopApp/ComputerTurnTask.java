package desktopApp;

import engine.GameLogic;
import engine.Move;
import javafx.concurrent.Task;

/**
 * Created by user on 01/09/2018.
 */
public class ComputerTurnTask extends Task<Move>
{
    private GameLogic gameLogic;
    private desktopAppController controller;

    ComputerTurnTask(GameLogic gameLogic, desktopAppController controller){
        this.gameLogic = gameLogic;
        this.controller = controller;
    }

    @Override
    protected Move call() throws Exception {
        gameLogic.play(0, gameLogic.isPopout());
        Thread.sleep(150);
        return gameLogic.getLastMove();
    }
}
