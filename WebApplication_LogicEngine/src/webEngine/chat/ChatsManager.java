package webEngine.chat;

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
        SingleChatManager singleChatManager = chats.get(gameId);

        if (singleChatManager == null) {
            chats.put(gameId, new SingleChatManager());
        }
        chats.get(gameId).addChatString(chatString, username);
    }

    public synchronized List<SingleChatEntry> getChatEntries(int gameId, int fromIndex) {
        return chats.get(gameId).getChatEntries(fromIndex);
    }

    public int getChatVersion(int gameId) {
        return chats.get(gameId).getVersion();
    }
}

