package NinaRow.constants;

public class Constants {
    public static final String USERNAME = "username";
    public static final String IS_COMPUTER_USER = "isComputer";
    public static final String USER_NAME_EXISTS_ERROR = "user already exists";
    public static final String USER_NAME_NOT_APPLICABLE_ERROR = "no username in session and no username in parameter";

    public static final String SETTINGS_FILE = "settingsfile";
    public static final String GAME_EXISTS_ERROR = "game already exists";
    public static final String SETTINGS_FILE_NOT_APPLICABLE_ERROR = "no settings file in parameter";

    public static final String GAME_ID = "gameId";
    public static final String GAME_ID_ERROR = "gameId parameter is not valid";
    public static final String REGISTER_TO_ACTIVE_GAME_ERROR = "game has already started";

    public static final String NO_USER_SESSION_ERROR = "no user is defined for session";
    public static final String INVALID_SESSION_ERROR = "no session is define";
    public static final String INVALID_USER_SESSION_ERROR = "user defined in session is invalid";

    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;
    public static final String MOVE_COL = "moveCol";
    public static final String MOVE_COL_ERROR = "invalid col parameter";
    public static final String MOVE_TYPE = "moveType";
    public static final String MOVE_TYPE_ERROR = "invalid move type parameter";
    public static final String MOVE_TYPE_PARAMETER_ERROR = "no move type parameter";
    public static final String PLAYER_ERROR = "It's not your turn yet, please be patient";
    public static final String GAME_NOT_STARTED_ERROR = "The game hasn't started yet, waiting for additional players";
    public static final String INVALID_MOVE_ERROR = "requested move is invalid";
    public static final String CHAT_VERSION_PARAMETER = "chatversion";
    public static final String CHAT_VERSION_ERROR = "invalid chat version parameter";
    public static final String CHAT_PARAMETER = "userstring";

    public static final String GAME_STATUS = "gameStatus";
    public static final String USER_EXISTS_IN_CAHT_ERROR = "user is already logged in to chat";
    public static final String USER_NOT_IN_CAHT_ERROR = "user is not logged in to chat";
    public static final String VIEWER_PLAYING_ERROR = "viewers can't make moves";
}
