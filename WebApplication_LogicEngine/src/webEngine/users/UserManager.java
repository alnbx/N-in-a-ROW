package webEngine.users;

import common.PlayerSettings;
import common.PlayerTypes;

import java.util.*;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {
    private final HashMap<String, PlayerSettings> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, PlayerTypes playerType) {
        usersMap.put(username, new PlayerSettings(username, usersMap.size() + 1, playerType));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Set<String> getUsersNames() {
        Set<String> usersNames = new HashSet<>();

        for (PlayerSettings player : usersMap.values()) {
            usersNames.add(player.getName());
        }

        return Collections.unmodifiableSet(usersNames);
    }

    public synchronized List<PlayerSettings> getUsers() {
        List<PlayerSettings> users = new ArrayList<>(usersMap.values());
        return Collections.unmodifiableList(users);
    }

    public synchronized int getNumUsers() {
        return usersMap.size();
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public PlayerSettings getUser(String username) {
        return usersMap.get(username);
    }

    public int getPlayerID(String playerName) {
        return usersMap.get(playerName).getId();
    }

    public PlayerTypes getPlayerType(String playerName) {
        PlayerSettings player = usersMap.get(playerName);
        PlayerTypes playerType = null;

        if (player != null){
            playerType = player.getPlayerType();
        }
        return playerType;
    }
}
