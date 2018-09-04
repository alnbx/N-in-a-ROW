package desktopApp;

import common.Lock;
import engine.GameLogic;
import engine.Move;
import javafx.concurrent.Task;

/**
 * Created by user on 01/09/2018.
 */
public class ComputerTurnTask extends Task<Void>
{
    private GameLogic gameLogic;
    private desktopAppController controller;

    ComputerTurnTask(GameLogic gameLogic, desktopAppController controller)
    {
        this.gameLogic = gameLogic;
        this.controller = controller;
    }

    @Override
    protected Void call() throws Exception
    {
        //gameLogic.play(0, gameLogic.isPopout());
        Thread.sleep(1200);
        //Move move = gameLogic.getLastMove();
        //return move;
        return null;
    }
}
