package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.MoveType;
import engine.GameLogic;
import webEngine.gamesList.GameListManager;
import webEngine.gamesList.GameStatus;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static NinaRow.constants.Constants.INT_PARAMETER_ERROR;

public class PlayMoveServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PlayMoveResponse playMoveResponse = new PlayMoveResponse();
        int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
        if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
            GameLogic gameLogic = gamesManager.getGameEntry(gameIdFromParam).getGameLogic();

            // get player id
            String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (userNameFromSession != null) {
                if (gamesManager.getGameStatus(gameIdFromParam) == GameStatus.PLAYING) {
                    Integer playerId = userManager.getPlayerID(userNameFromSession);
                    if (playerId != null && playerId == gameLogic.getIdOfCurrentPlayer()) {
                        int colParameter = ServletUtils.getIntParameter(request, Constants.MOVE_COL);
                        if (colParameter != INT_PARAMETER_ERROR) {
                            String moveTypeParameter = request.getParameter(Constants.MOVE_TYPE);
                            MoveType moveType = null;
                            if (playMoveResponse.getResult()) {
                                if (moveTypeParameter != null) {
                                    // get type of move
                                    if (moveTypeParameter.equalsIgnoreCase("insert")) {
                                        moveType = MoveType.INSERT;
                                    } else if (moveTypeParameter.equalsIgnoreCase("popout")) {
                                        moveType = MoveType.POPOUT;
                                    } else {
                                        playMoveResponse.setResult(false);
                                        playMoveResponse.setMsg(Constants.MOVE_TYPE_ERROR);
                                    }

                                    if (playMoveResponse.getResult()) {
                                        synchronized (this) {
                                            if (gameLogic.play(colParameter, moveType.equals(MoveType.POPOUT))) {
                                                gamesManager.setIsTie(gameIdFromParam, gameLogic.isTie());
                                                gamesManager.setWinners(gameIdFromParam, userManager.getWinnersNames(gameLogic.getWinners()));
                                                if (gamesManager.isGameEnded(gameIdFromParam)) {
                                                    gamesManager.enableGameForRegistration(gameIdFromParam);
                                                }
                                            } else {
                                                playMoveResponse.setResult(false);
                                                playMoveResponse.setMsg(Constants.INVALID_MOVE_ERROR);
                                            }
                                        }
                                    }
                                }
                                else {
                                    playMoveResponse.setResult(false);
                                    playMoveResponse.setMsg(Constants.MOVE_TYPE_PARAMETER_ERROR);
                                }
                            }
                        }
                        else {
                            playMoveResponse.setResult(false);
                            playMoveResponse.setMsg(Constants.MOVE_COL_ERROR);
                        }
                    }
                    else {
                        playMoveResponse.setResult(false);
                        playMoveResponse.setMsg(Constants.PLAYER_ERROR);
                    }
                }
                else {
                    playMoveResponse.setResult(false);
                    playMoveResponse.setMsg(Constants.GAME_NOT_STARTED_ERROR);
                }
            }
            else {
                playMoveResponse.setResult(false);
                playMoveResponse.setMsg(Constants.NO_USER_SESSION_ERROR);
            }
        }
        else {
            playMoveResponse.setResult(false);
            playMoveResponse.setMsg(Constants.GAME_ID_ERROR);
        }

        ServletUtils.sendJsonResponse(response, playMoveResponse);
    }

    class PlayMoveResponse extends ServeltResponse { }

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
