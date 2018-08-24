package engine;

import common.GameSettings;

import java.util.ArrayList;

public class CircularGame extends Game {
    public CircularGame(GameSettings gameSettings) {
        super(gameSettings);
    }

    @Override
    public void setRoundFromSettings(boolean restartPlayers) {
        this.board = new Board(gameSettings.getBoardNumRows(), gameSettings.getNumCols(), true);
        this.hasWinner = false;
        this.isBoardFull = false;
        this.currentPlayer = null;
        this.playedMoves = new ArrayList<Move>();
        this.startingTime = null;
        super.restartPlayers(restartPlayers);
    }
}
