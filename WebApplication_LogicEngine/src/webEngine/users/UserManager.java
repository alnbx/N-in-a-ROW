package webEngine.users;

import common.PlayerTypes;

import java.util.*;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final HashMap<String, SingleUserEntry> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, PlayerTypes playerType) {
        usersMap.put(username, new SingleUserEntry(username, playerType));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Set<String> getUsersNames() {
        Set<String> usersNames = new HashSet<>();

        for (SingleUserEntry sue : usersMap.values()) {
            usersNames.add(sue.getName());
        }

        return Collections.unmodifiableSet(usersNames);
    }

    public synchronized List<SingleUserEntry> getUsers() {
        List<SingleUserEntry> users = new ArrayList<>(usersMap.values());
        return Collections.unmodifiableList(users);
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }
}
