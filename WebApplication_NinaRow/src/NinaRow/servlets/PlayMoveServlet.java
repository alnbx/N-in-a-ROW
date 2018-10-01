package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.MoveType;
import engine.GameLogic;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static NinaRow.constants.Constants.INT_PARAMETER_ERROR;

public class PlayMoveServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PlayMoveResponse playMoveResponse = new PlayMoveResponse();
        String gameNameFromSession = SessionUtils.getAttribute(request, Constants.GAMENAME);
        if (gameNameFromSession != null) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
            GameLogic gameLogic = gamesManager.getGameEntry(gameNameFromSession).getGameLogic();

            // get player id
            String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (userNameFromSession != null) {
                Integer playerId = userManager.getPlayerID(userNameFromSession);
                if (playerId == null || playerId != gameLogic.getIdOfCurrentPlayer()) {
                    playMoveResponse.setResult(false);
                    playMoveResponse.setMsg(Constants.PLAYER_ERROR);
                }
                else {
                    playMoveResponse.playerId = playerId;
                }

                // get col of move
                int colParameter = ServletUtils.getIntParameter(request, Constants.MOVE_COL);
                if (colParameter == INT_PARAMETER_ERROR) {
                    playMoveResponse.setResult(false);
                    playMoveResponse.setMsg(Constants.MOVE_COL_ERROR);
                }
                else {
                    playMoveResponse.col = colParameter;
                }

                String moveTypeParameter = request.getParameter(Constants.MOVE_TYPE);
                if (moveTypeParameter != null) {
                    // get type of move
                    if (moveTypeParameter.equalsIgnoreCase("insert")) {
                        playMoveResponse.moveType = MoveType.INSERT;
                    }
                    else if (moveTypeParameter.equalsIgnoreCase("popout")) {
                        playMoveResponse.moveType = MoveType.POPOUT;
                    }
                    else {
                        playMoveResponse.setResult(false);
                        playMoveResponse.setMsg(Constants.MOVE_TYPE_ERROR);
                    }

                    if (playMoveResponse.getResult()) {
                        synchronized (getServletContext()) {
                            if (gameLogic.play(colParameter, playMoveResponse.moveType.equals(MoveType.POPOUT))) {
                                playMoveResponse.winners = gameLogic.getWinners();
                                playMoveResponse.isTie = gameLogic.isTie();
                                if (playMoveResponse.isTie || playMoveResponse.winners.size() > 0) {
                                    gamesManager.enableGameForRegistration(gameNameFromSession);
                                }
                            }
                            else {
                                playMoveResponse.setResult(false);
                                playMoveResponse.setMsg(Constants.INVALID_MOVE_ERROR);
                            }
                        }
                    }
                }
                else {
                    playMoveResponse.setResult(false);
                    playMoveResponse.setMsg(Constants.MOVE_TYPE_ERROR);
                }
            }
            else {
                playMoveResponse.setResult(false);
                playMoveResponse.setMsg(Constants.USER_SESSION_ERROR);
            }
        }
        else {
            playMoveResponse.setResult(false);
            playMoveResponse.setMsg(Constants.GAME_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, playMoveResponse);
    }

    class PlayMoveResponse extends ServeltResponse {
        Set<Integer> winners;
        Boolean isTie;
        MoveType moveType;
        int playerId;
        int col;

        public PlayMoveResponse() {
            this.isTie = false;
            this.winners = null;
            this.moveType = null;
            this.playerId = -1;
            this.col = -1;
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
