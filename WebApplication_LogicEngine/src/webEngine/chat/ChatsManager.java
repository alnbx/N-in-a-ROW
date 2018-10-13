package webEngine.chat;

import common.UserSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsManager {
    private final Map<Integer, SingleChatManager> chats;

    public ChatsManager() {
        chats = new HashMap<>();
    }

    public SingleChatManager getChatManager(int gameId) {
        return chats.get(gameId);
    }

    public synchronized void addChatString(int gameId, String chatString, String username) {
        chats.get(gameId).addChatString(chatString, username);
    }

    public synchronized List<SingleChatEntry> getChatEntries(int gameId, int fromIndex) {
        return chats.get(gameId).getChatEntries(fromIndex);
    }

    public int getChatVersion(int gameId) {
        return chats.get(gameId).getVersion();
    }

    public boolean isUserExistsInChat(int gameId, String userName) {
        if (chats.get(gameId) == null) {
            return false;
        }

        return chats.get(gameId).isUserExistsInChat(userName);
    }

    public synchronized void addUserToChat(int gameId, UserSettings userSettings) {
        if (chats.get(gameId) == null) {
            chats.put(gameId, new SingleChatManager());
        }

        chats.get(gameId).addUserToChat(userSettings);
    }

    public List<UserSettings> getChatUsers(int gameId) {
        return chats.get(gameId).getChatUsers();
    }

    public void removeUserFromChat(int gameId, String userName) {
        chats.get(gameId).removeUserFromChat(userName);
    }
}

