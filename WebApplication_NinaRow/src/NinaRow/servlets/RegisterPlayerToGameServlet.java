package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.UserSettings;
import webEngine.gamesList.GameListManager;
import webEngine.gamesList.GameStatus;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterPlayerToGameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        RegisterPlayerResponse registerPlayerResponse = new RegisterPlayerResponse();
        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        if (usernameFromSession != null) {
            int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
            if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());
                registerPlayerResponse.gameId = gameIdFromParam;
                registerPlayerResponse.gameName = gameListManager.getGameName(gameIdFromParam);
                synchronized (this) {
                    if (!gameListManager.isGameActive(gameIdFromParam)) {
                        UserSettings playerSettings = userManager.getUser(usernameFromSession);
                        gameListManager.registerPlayerToGame(gameIdFromParam, playerSettings);
                    }
                    else {
                        registerPlayerResponse.setResult(false);
                        registerPlayerResponse.setMsg(Constants.REGISTER_TO_ACTIVE_GAME_ERROR);
                    }

                    // if this player makes the num of registered players match the requirement - init the game
                    if (gameListManager.isPlayersListFull(gameIdFromParam)) {
                        gameListManager.startGame(gameIdFromParam);
                        registerPlayerResponse.gameStatus = GameStatus.PLAYING;
                    }
                }
            }
            else {
                registerPlayerResponse.setResult(false);
                registerPlayerResponse.setMsg(Constants.GAME_ID_ERROR);
            }
        }
        else {
            registerPlayerResponse.setResult(false);
            registerPlayerResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, registerPlayerResponse);
    }

    public class RegisterPlayerResponse extends ServeltResponse {
        String gameName;
        GameStatus gameStatus;
        int gameId;

        public RegisterPlayerResponse() {
            gameStatus = GameStatus.PENDING_PLAYERS;
            gameName = "";
            gameId = 0;
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
