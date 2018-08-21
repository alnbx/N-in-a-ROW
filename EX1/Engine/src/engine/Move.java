package engine;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {
    static int movesCount;
    private int moveIndex;
    private int playerId;
    private int col;

    static { movesCount = 0;}

    public int getPlayerId() {
        return playerId;
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    public int getCol() {
        return col;
    }

    public Move(int playerId, int col) {
        this.moveIndex = movesCount++;
        this.playerId = playerId;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moveIndex == move.moveIndex &&
                playerId == move.playerId &&
                col == move.col;
    }

    @Override
    public int hashCode() {

        return Objects.hash(moveIndex, playerId, col);
    }
}
