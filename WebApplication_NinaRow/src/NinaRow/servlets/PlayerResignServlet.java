package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import engine.GameLogic;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class PlayerResignServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String gameName = request.getParameter(Constants.GAMENAME);
        response.setContentType("application/json");

        // create the response
        PlayerResignResponse playerResignResponse = new PlayerResignResponse();
        if (gameName != null) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            GameLogic game = ServletUtils.getGamesListManager(getServletContext()).
                    getGameEntry(gameName).getGameLogic();

            // get player id
            String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (userNameFromSession != null) {
                Integer playerId = userManager.getPlayerID(userNameFromSession);
                if (playerId != null) {
                    // only the current player can quit the game
                    if (playerId != game.getIdOfCurrentPlayer()) {
                        playerResignResponse.setResult(false);
                        playerResignResponse.setMsg(Constants.PLAYER_ERROR);
                    }
                }
                else {
                    playerResignResponse.setResult(false);
                    playerResignResponse.setMsg(Constants.PLAYER_ERROR);
                }

                if (playerResignResponse.getResult() == true) {
                    game.resignPlayer();
                    // check if the player's resignation lead to a win
                    playerResignResponse.winners = game.getWinners();
                    userManager.clearGame(userNameFromSession);
                }
            }
            else {
                playerResignResponse.setResult(false);
                playerResignResponse.setMsg(Constants.USER_SESSION_ERROR);
            }
        }
        else {
            playerResignResponse.setResult(false);
            playerResignResponse.setMsg(Constants.GAME_NAME_PARAMETER_ERROR);
        }

        ServletUtils.sendJsonResponse(response, playerResignResponse);
    }

    class PlayerResignResponse extends ServeltResponse {
        Set<Integer> winners;

        public PlayerResignResponse() {
            winners = null;
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
