package webEngine.chat;

import common.UserSettings;

import java.util.ArrayList;
import java.util.List;

public class SingleChatManager {
    private List<UserSettings> chatUsers;
    private List<SingleChatEntry> chatDataList;

    public SingleChatManager() {
        this.chatUsers = new ArrayList<>();
        this.chatDataList = new ArrayList<>();
    }

    public synchronized void addChatString(String chatString, String username) {
        chatDataList.add(new SingleChatEntry(chatString, username));
    }

    public synchronized List<SingleChatEntry> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex >= chatDataList.size()) {
            fromIndex = 0;
        }
        return chatDataList.subList(fromIndex, chatDataList.size());
    }

    public int getVersion() {
        return chatDataList.size();
    }

    public synchronized void addUserToChat(UserSettings userSettings) {
        chatUsers.add(userSettings);
    }

    public boolean isUserExistsInChat(String userName) {
        for (UserSettings userSettings : chatUsers) {
            if (userSettings.getName().equalsIgnoreCase(userName)) {
                return true;
            }
        }
        return false;
    }

    public List<UserSettings> getChatUsers() {
        return chatUsers;
    }

    public void removeUserFromChat(String userName) {
        chatUsers.removeIf(user -> user.getName().equalsIgnoreCase(userName));
    }
}
