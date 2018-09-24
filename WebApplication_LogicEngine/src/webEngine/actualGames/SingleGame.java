package webEngine.actualGames;

import common.MoveType;
import engine.GameLogic;
import java.util.Set;

public class SingleGame {
    private GameLogic game;

    public SingleGame(GameLogic game) {
        this.game = game;
    }

    public int getIndexLastMove() {
        return game.getMovesHistory().size() - 1;
    }

    public String getGameName() {
        return game.getGameTitle();
    }

    public Set<Integer> getWinners() {
        return game.getWinners();
    }

    public Boolean isTie() {
        return game.isTie();
    }

    public Boolean isValidMove(int col, MoveType moveType) {
        return game.play(col, moveType == MoveType.POPOUT);
    }

    public int getCurrentPlayer() {
        return game.getIdOfCurrentPlayer();
    }
}
