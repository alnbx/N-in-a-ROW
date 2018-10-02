package webEngine.users;

import common.UserSettings;
import common.PlayerTypes;

import java.util.*;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {
    private final HashMap<String, UserSettings> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, PlayerTypes playerType) {
        usersMap.put(username, new UserSettings(username, usersMap.size() + 1, playerType));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Set<String> getUsersNames() {
        Set<String> usersNames = new HashSet<>();

        for (UserSettings player : usersMap.values()) {
            usersNames.add(player.getName());
        }

        return Collections.unmodifiableSet(usersNames);
    }

    public synchronized List<UserSettings> getUsers() {
        List<UserSettings> users = new ArrayList<>(usersMap.values());
        return Collections.unmodifiableList(users);
    }

    public synchronized int getNumUsers() {
        return usersMap.size();
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public UserSettings getUser(String username) {
        return usersMap.get(username);
    }

    public int getPlayerID(String playerName) {
        return usersMap.get(playerName).getId();
    }

    public PlayerTypes getPlayerType(String playerName) {
        UserSettings player = usersMap.get(playerName);
        PlayerTypes playerType = null;

        if (player != null){
            playerType = player.getPlayerType();
        }
        return playerType;
    }

    public void clearGame(String userName) {
        usersMap.get(userName).clearGame();
    }

    public void setGameToUser(String username, String gameName) {
        usersMap.get(username).setGame(gameName);
    }
}
