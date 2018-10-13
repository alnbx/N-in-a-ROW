package webEngine.gamesList;

import common.GameVariant;
import common.UserSettings;
import engine.GameLogic;
import engine.Move;
import engine.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleGameEntry {
    final private String userName;
    final private int gameId;
    private GameLogic gameLogic;
    private String gameName;
    private GameStatus gameStatus;
    private List<UserSettings> viewers;
    private boolean hasWinner;
    private boolean isTie;
    private Set<String> winners;

    SingleGameEntry(GameLogic gamaLogic, String userName, int gameId) {
        this.gameLogic = gamaLogic;
        this.gameName = gameLogic.getGameName();
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        this.userName = userName;
        this.viewers = new ArrayList<>();
        this.gameId = gameId;
        this.hasWinner = false;
        this.isTie = false;
        this.winners = null;
    }

    /* Note (1):
        active players/viewers are displayed to clients that their game status is PLAYING
        registered players/viewers are displayed to clients that their game status is PENDING_PLAYERS
     */

    /* Note (2):
        activeViewrs = this.viewers
        activePlayers = gameLogic.Players
        registered viewers/players = viewers/players from gameLogic.gameSettings
     */

    public boolean isPlayerListFull() {
        return gameLogic.isPlayerListFull();
    }

    public String getUserName() {
        return userName;
    }

    public int getNumRegisteredPlayers() {
        return gameLogic.getNumRegisteredPlayers();
    }

    public int getNumRequiredPlayers() {
        return gameLogic.getNumberOfRequiredPlayers();
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
        gameLogic.addPlayer(player);
    }

    public int getRows() {
        return gameLogic.getRows();
    }

    public int getCols() {
        return gameLogic.getCols();
    }

    public int getSequenceLength() {
        return gameLogic.getSequenceLength();
    }

    public GameVariant getGameVariant() {
        return gameLogic.getGameVariant();
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void enableGameForRegistration() {
        this.gameStatus = GameStatus.PENDING_PLAYERS;
        gameLogic.clearRegisteredPlayers();
        gameLogic.clearRegisteredViewers();
    }

    public void startNewGame() {
        this.gameLogic.setRoundFromSettings(true);
        this.gameStatus = GameStatus.PLAYING;
        this.hasWinner = false;
        this.isTie = false;
        this.winners = null;
        this.viewers = gameLogic.getRegisteredViewers();
    }

    public Boolean isUserPlayerInGame(String username) {
        return gameLogic.isUserPlayerInGame(username);
    }

    public List<UserSettings> getActiveViewers() {
        return viewers;
    }

    public void registerViewer(UserSettings user) {
        viewers.add(user);
    }

    public void viewerResign(String userName) {
        viewers.removeIf(user -> user.getName().equalsIgnoreCase(userName));
    }

    public int getGameId() {
        return gameId;
    }

    public void setIsTie() {
        this.isTie = gameLogic.isTie();
    }

    public void setWinners() {
        Set<Integer> winnersIds = gameLogic.getWinners();
        this.winners = new HashSet<>();
        for (Integer winnerId : winnersIds) {
            this.winners.add(gameLogic.getPlayerName(winnerId));
        }

        this.hasWinner = this.winners.size() > 0;
    }

    public List<String> getAllGamePlayersAndViewers() {
        List<String> usersNames = new ArrayList<>();
        for (Player player : gameLogic.getPlayers()) {
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

    public List<Move> getMovesHistory() {
        return gameLogic.getMovesHistory();
    }

    public List<Player> getActivePlayers() {
        return gameLogic.getPlayers();
    }

    public List<Player> getRegisteredPlayers() {
        List<Player> registeredPlayers = new ArrayList();

        for (UserSettings user : gameLogic.getRegisteredPlayers()) {
            registeredPlayers.add(new Player(user));
        }

        registeredPlayers.get(0).setCurrent(true);

        return registeredPlayers;
    }

    public int[][] getBoardData() {
        return gameLogic.getBoardAsIntArr();
    }

    public int getIdOfCurrentPlayer() {
        return gameLogic.getIdOfCurrentPlayer();
    }

    public int[][] getEmptyGameBoardData() {
        return gameLogic.getEmptyBoardAsIntArr();
    }

    public List<UserSettings> getRegisteredViewers() {
        return gameLogic.getRegisteredViewers();
    }

    public void resignPlayerFromGame(Integer playerId) {
        gameLogic.resignPlayer();
    }

    public void removePlayerFromRegisteredPlayers(Integer playerId) {
        gameLogic.removePlayerFromRegisteredPlayers(playerId);
    }

    public boolean makeMoveInGame(int col, boolean isPopout) {
        return gameLogic.play(col, isPopout);
    }

    public boolean isUserViewer(String userName) {
        for (UserSettings user : viewers) {
            if (user.getName().equalsIgnoreCase(userName)) {
                return true;
            }
        }
        return false;
    }
}
