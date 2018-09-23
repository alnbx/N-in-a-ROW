package NinaRow.utils;

import NinaRow.servlets.GamesListServlet;
import webEngine.games.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static NinaRow.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String GAME_MANAGER_ATTRIBUTE_NAME = "gameManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained unchronicled for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object gamesManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameListManager getGamesManager(ServletContext servletContext) {

        synchronized (gamesManagerLock) {
            if (servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(GAME_MANAGER_ATTRIBUTE_NAME, new GameListManager());
            }
        }
        return (GameListManager) servletContext.getAttribute(GAME_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
