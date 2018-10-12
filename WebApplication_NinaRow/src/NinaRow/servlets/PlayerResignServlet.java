package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import engine.GameLogic;
import webEngine.gamesList.GameListManager;
import webEngine.gamesList.GameStatus;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerResignServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        // create the response
        PlayerResignResponse playerResignResponse = new PlayerResignResponse();
        int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
        if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());

            // get player id
            String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (userNameFromSession != null) {
                Integer playerId = userManager.getPlayerID(userNameFromSession);
                if (playerId != null) {
                    if (gameListManager.getGameStatus(gameIdFromParam) == GameStatus.PLAYING ) {
                        // only the current player can quit the game
                        if (playerId == gameListManager.getIdOfCurrentPlayer(gameIdFromParam)) {
                            synchronized (this) {
                                gameListManager.resignPlayerFromGame(gameIdFromParam, playerId);
                                // check if the player's resignation lead to the game's end
                                gameListManager.setIsTie(gameIdFromParam);
                                gameListManager.setWinners(gameIdFromParam);
                                if (gameListManager.isGameEnded(gameIdFromParam)) {
                                    gameListManager.enableGameForRegistration(gameIdFromParam);
                                }
                            }
                        }
                        else {
                            playerResignResponse.setResult(false);
                            playerResignResponse.setMsg(Constants.PLAYER_ERROR);
                        }
                    }
                    else {
                        gameListManager.removePlayerFromRegisteredPlayers(gameIdFromParam, playerId);
                    }
                }
                else {
                    playerResignResponse.setResult(false);
                    playerResignResponse.setMsg(Constants.INVALID_USER_SESSION_ERROR);
                }
            }
            else {
                playerResignResponse.setResult(false);
                playerResignResponse.setMsg(Constants.NO_USER_SESSION_ERROR);
            }
        }
        else {
            playerResignResponse.setResult(false);
            playerResignResponse.setMsg(Constants.GAME_ID_ERROR);
        }

        ServletUtils.sendJsonResponse(response, playerResignResponse);
    }

    class PlayerResignResponse extends ServeltResponse { }

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
