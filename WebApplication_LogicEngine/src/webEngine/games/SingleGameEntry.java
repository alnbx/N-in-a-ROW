package webEngine.games;

import engine.GameLogic;

public class SingleGameEntry {
    final private String gameName;
    final private String userName;
    final private GameLogic gameLogic;
    private GameStatus gameStatus;
    private int numRegisteredPlayers;

    SingleGameEntry(GameLogic gameLogic, String userName) {
        this.gameLogic = gameLogic;
        this.gameName = gameLogic.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.numRegisteredPlayers = 0;
    }

    public String getUserName() {
        return userName;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public int getNumRegisteredPlayers() {
        return numRegisteredPlayers;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameName() {
        return gameName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void increaseNumRegisteredPlayers() {
        numRegisteredPlayers++;
    }

    public void zeroizeNumRegisteredPlayers() {
        numRegisteredPlayers = 0;
    }
}
