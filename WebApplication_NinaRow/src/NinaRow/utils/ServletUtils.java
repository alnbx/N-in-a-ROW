package NinaRow.utils;

import com.google.gson.Gson;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static NinaRow.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String GAMES_LIST_MANAGER_ATTRIBUTE_NAME = "gamesListManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained unchronicled for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object gamesListManagerLock = new Object();
    private static final Object gamesManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static GameListManager getGamesListManager(ServletContext servletContext) {

        synchronized (gamesListManagerLock) {
            if (servletContext.getAttribute(GAMES_LIST_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(GAMES_LIST_MANAGER_ATTRIBUTE_NAME, new GameListManager());
            }
        }
        return (GameListManager) servletContext.getAttribute(GAMES_LIST_MANAGER_ATTRIBUTE_NAME);
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

    public static void sendJsonResponse(HttpServletResponse response, ServeltResponse reponse) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(reponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
