package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import com.google.gson.Gson;
import common.PlayerTypes;
import webEngine.games.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static NinaRow.constants.Constants.*;

public class UploadGameServlet  extends HttpServlet {
    private final String GAMES_LIST_URL = "/pages/gameslist/gameslist.html"; // must start with '/' since will be used in request dispatcher
    private final String GAME_UPLOAD_ERROR_URL = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String settingsFileFromParameter = request.getParameter(SETTINGS_FILE);
        String usernameFromSession = SessionUtils.getUsername(request);
        GameListManager gameListManager = ServletUtils.getGamesManager(getServletContext());

        // create the response
        UploadGameResponse uploadGameResponse = new UploadGameResponse();

        if (settingsFileFromParameter == null) {
            // no settings file in parameter
            uploadGameResponse.setMsg(SETTINGS_FILE_NOT_APPLICABLE_ERROR);
            uploadGameResponse.setSuccess(false);

        } else {
            // normalize the settings file path string
            settingsFileFromParameter = settingsFileFromParameter.trim();
            synchronized (this) {
                try {
                    if (gameListManager.isGameExists(settingsFileFromParameter)) {
                        uploadGameResponse.setMsg(SETTINGS_FILE_EXISTS_ERROR);
                        uploadGameResponse.setSuccess(false);
                    }
                    else {
                        gameListManager.addGame(settingsFileFromParameter, usernameFromSession);
                    }
                }
                catch (Exception e) {
                    uploadGameResponse.setMsg(e.getMessage());
                    uploadGameResponse.setSuccess(false);
                }
            }
        }

        sendJsonResponse(response, uploadGameResponse);
        getServletContext().getRequestDispatcher(GAMES_LIST_URL).forward(request, response);;
    }

    private void sendJsonResponse(HttpServletResponse response, UploadGameServlet.UploadGameResponse uploadGameResponse) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(uploadGameResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    class UploadGameResponse {
        private Boolean success = true;
        private String msg = "";
        private String gameName = "";

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

    }
}
