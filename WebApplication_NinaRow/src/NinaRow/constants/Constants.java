package NinaRow.constants;

public class Constants {
    public static final String USERNAME = "username";
    public static final String USER_TYPE = "playertype";
    public static final String USER_NAME_EXISTS_ERROR = "user already exists";
    public static final String USER_NAME_NOT_APPLICABLE_ERROR = "no username in session and no username in parameter";

    public static final String SETTINGS_FILE = "settingsfile";
    public static final String GAME_EXISTS_ERROR = "game already exists";
    public static final String SETTINGS_FILE_NOT_APPLICABLE_ERROR = "no settings file in parameter";

    public static final String GAMENAME = "gamename";
    public static final String GAME_NAME_NOT_APPLICABLE_ERROR = "no game name in parameter";
    public static final String GAME_PLAYERS_LIST_IS_FULL_ERROR = "players list is full";

    public static final String GAME_SESSION_ERROR = "no game is defined for session";
    public static final String USER_SESSION_ERROR = "no user is defined for session";
    public static final String MOVE_SESSION_ERROR = "no move is defined for session";

    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;
    public static final String MOVE_COL = "movecol";
    public static final String MOVE_COL_ERROR = "invalid col parameter";
    public static final String MOVE_TYPE = "movetype";
    public static final String MOVE_TYPE_ERROR = "invalid move type parameter";
    public static final String PLAYER_ERROR = "player in parameter is not current player";
    public static final String INVALID_MOVE_ERROR = "requested move is invalid";
}
