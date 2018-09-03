package desktopApp;

import common.MoveType;
import engine.*;

public class MoveDisplay {
    private MoveType moveType;
    private  String playerName;
    private int playerid;
    private int col;
    private String timestamp;

    public MoveDisplay(Move move, String playerName) {
        this.moveType = move.getMoveType();
        this.playerName = playerName;
        this.col = move.getCol();
        this.timestamp = move.getTimeStamp();
        this.playerid = move.getPlayerId();

    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCol() {
        return col;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getPlayerid() {
        return playerid;
    }
}
