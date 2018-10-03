package webEngine.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsManager {
    private final Map<String, SingleChatManager> chats;

    public ChatsManager() {
        chats = new HashMap<>();
    }

    public synchronized void addChat(String gameName, ChatsManager chat) {
        chats.put(gameName, new SingleChatManager());
    }

    public SingleChatManager getChatManager(String gameName) {
        return chats.get(gameName);
    }

    public synchronized void addChatString(String gameName, String chatString, String username) {
        chats.get(gameName).addChatString(chatString, username);
    }

    public synchronized List<SingleChatEntry> getChatEntries(String gameName, int fromIndex) {
        return chats.get(gameName).getChatEntries(fromIndex);
    }

    public int getChatVersion(String gameName) {
        return chats.get(gameName).getVersion();
    }
}

