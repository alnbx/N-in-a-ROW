package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.GameSettings;
import webEngine.gamesList.GameListManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static NinaRow.constants.Constants.*;

public class UploadGameServlet extends HttpServlet {
    private final String GAMES_LIST_URL = "/pages/gameslist/gameslist.html"; // must start with '/' since will be used in request dispatcher
    private final String GAME_UPLOAD_ERROR_URL = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        UploadGameResponse uploadGameResponse = new UploadGameResponse();
        String settingsFileFromParameter = request.getParameter(SETTINGS_FILE);
        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        if (usernameFromSession != null) {
            GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());

            if (settingsFileFromParameter == null) {
                // no settings file in parameter
                uploadGameResponse.setMsg(SETTINGS_FILE_NOT_APPLICABLE_ERROR);
                uploadGameResponse.setResult(false);

            } else {
                // normalize the settings file path string
                settingsFileFromParameter = settingsFileFromParameter.trim();
                synchronized (this) {
                    try {
                        if (gameListManager.isGameExists(settingsFileFromParameter)) {
                            uploadGameResponse.setMsg(GAME_EXISTS_ERROR);
                            uploadGameResponse.setResult(false);
                        }
                        else {
                            GameSettings gameSettings = new GameSettings(settingsFileFromParameter, false);
                            gameListManager.addGame(gameSettings, usernameFromSession);
                            uploadGameResponse.gameName = gameListManager.getGameName(gameSettings.getGameTitle());
                            uploadGameResponse.gameSettings = gameSettings;
                        }
                    }
                    catch (Exception e) {
                        uploadGameResponse.setMsg(e.getMessage());
                        uploadGameResponse.setResult(false);
                    }
                }
            }
            //getServletContext().getRequestDispatcher(GAMES_LIST_URL).forward(request, response);
        }
        else {
            uploadGameResponse.setResult(false);
            uploadGameResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, uploadGameResponse);
    }

    class UploadGameResponse extends ServeltResponse {
        private String gameName = "";
        private GameSettings gameSettings = null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
