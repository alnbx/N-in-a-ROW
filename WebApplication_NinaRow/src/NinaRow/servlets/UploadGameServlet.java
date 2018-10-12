package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.GameSettings;
import common.GameVariant;
import webEngine.gamesList.GameListManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static NinaRow.constants.Constants.*;

public class UploadGameServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        UploadGameResponse uploadGameResponse = new UploadGameResponse();
        String settingsFileFromParameter = request.getParameter(SETTINGS_FILE);
        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        if (usernameFromSession != null) {
            uploadGameResponse.userName = usernameFromSession;
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
                            uploadGameResponse.gameId = gameListManager.addGame(gameSettings, usernameFromSession);
                            uploadGameResponse.gameName = gameSettings.getGameTitle();
                            uploadGameResponse.boardRows = gameSettings.getBoardNumRows();
                            uploadGameResponse.boardCols = gameSettings.getNumCols();
                            uploadGameResponse.target = gameSettings.getTarget();
                            uploadGameResponse.gameVariant = gameSettings.getGameVariant();
                            uploadGameResponse.totalPlayers = gameSettings.getNumOfPlayers();
                        }
                    }
                    catch (Exception e) {
                        uploadGameResponse.setMsg(e.toString());
                        uploadGameResponse.setResult(false);
                    }
                }
            }
        }
        else {
            uploadGameResponse.setResult(false);
            uploadGameResponse.setMsg(Constants.NO_USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, uploadGameResponse);
    }

    class UploadGameResponse extends ServeltResponse {
        private String gameName = "";
        private String userName;
        private int boardRows;
        private int boardCols;
        private int target;
        private GameVariant gameVariant;
        private int totalPlayers;
        private int gameId;

        public UploadGameResponse() {
            this.gameName = "";
            this.userName = "";
            this.boardCols = 0;
            this.boardRows = 0;
            this.target = 0;
            this.gameVariant = null;
            this.totalPlayers = 0;
            this.gameId = 0;
        }
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
