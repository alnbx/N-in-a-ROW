package webEngine.gamesList;

import common.GameSettings;
import common.GameVariant;
import common.UserSettings;
import engine.GameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SingleGameEntry {
    final private String userName;
    // players register to the game by being added to the gameSettings
    final private GameSettings gameSettings;
    final private int gameId;
    private GameLogic game;
    private String gameName;
    private GameStatus gameStatus;
    private List<UserSettings> viewers;
    private boolean hasWinner;
    private boolean isTie;
    private Set<String> winners;

    SingleGameEntry(GameSettings gameSettings, String userName, int gameId) {
        this.game = null;
        this.gameName = gameSettings.getGameTitle();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.gameSettings = gameSettings;
        this.viewers = new ArrayList<>();
        this.gameId = gameId;
        this.hasWinner = false;
        this.isTie = false;
        this.winners = null;
    }

    public boolean isPlayerListFull() {
        return gameSettings.isPlayerListFull();
    }

    public String getUserName() {
        return userName;
    }

    public int getNumRegisteredPlayers() {
        return gameSettings.getNumRegisteredPlayers();
    }

    public int getNumRequiredPlayers() {
        return gameSettings.getNumOfPlayers();
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

    public void registerPlayer(UserSettings player) {
        gameSettings.addPlayer(player);
    }

    public int getRows() {
        return gameSettings.getNumRows();
    }

    public int getCols() {
        return gameSettings.getNumCols();
    }

    public int getSequenceLength() {
        return gameSettings.getTarget();
    }

    public GameVariant getGameVariant() {
        return gameSettings.getGameVariant();
    }

    public GameLogic getGameLogic() {
        return game;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.game = gameLogic;
    }

    public void enableGameForRegistration() {
        this.game = null;
        this.gameSettings.clearPlayers();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.hasWinner = false;
        this.isTie = false;
        this.winners = null;

    }

    public Boolean isUserPlayerInGame(String username) {
        return this.gameSettings.isUserPlayerInGame(username);
    }

    public List<UserSettings> getGameViewers() {
        return viewers;
    }

    public void registerViewer(UserSettings user) {
        viewers.add(user);
    }

    public void viewerResign(String userName) {
        viewers.removeIf(player -> player.getName().equalsIgnoreCase(userName));
    }

    public int getGameId() {
        return gameId;
    }

    public void setIsTie(Boolean isTie) {
        this.isTie = isTie;
    }

    public void setWinners(Set<String> winners) {
        this.winners = winners;
        this.hasWinner = winners.size() > 0;
    }

    public List<String> getAllGamePlayersAndViewers() {
        List<String> usersNames = new ArrayList<>();
        for (UserSettings player : gameSettings.getPlayersSettings()) {
            usersNames.add(player.getName());
        }

        for (UserSettings viewer : viewers) {
            usersNames.add(viewer.getName());
        }

        return usersNames;
    }

    public boolean isGameEnded() {
        return isTie || hasWinner;
    }

    public boolean getHasWinner() {
        return hasWinner;
    }

    public boolean getIsTie() {
        return isTie;
    }

    public Set<String> getWinners() {
        return winners;
    }
}
