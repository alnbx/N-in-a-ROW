package webEngine.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsManager {
    private final Map<String, SingleChatManager> chats;

    public ChatsManager() {
        chats = new HashMap<>();
    }

    public SingleChatManager getChatManager(String gameName) {
        return chats.get(gameName);
    }

    public synchronized void addChatString(String gameName, String chatString, String username) {
        SingleChatManager singleChatManager = chats.get(gameName);

        if (singleChatManager == null) {
            chats.put(gameName, new SingleChatManager());
        }
        chats.get(gameName).addChatString(chatString, username);
    }

    public synchronized List<SingleChatEntry> getChatEntries(String gameName, int fromIndex) {
        return chats.get(gameName).getChatEntries(fromIndex);
    }

    public int getChatVersion(String gameName) {
        return chats.get(gameName).getVersion();
    }
}

