package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.MoveType;
import engine.Move;
import webEngine.actualGames.GamesManager;
import webEngine.actualGames.SingleGame;
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
        String gameName = SessionUtils.getAttribute(request, Constants.GAMENAME);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        SingleGame game = ServletUtils.getGamesManager(getServletContext()).getGame(gameName);

        // get player id
        String userNameParameter = SessionUtils.getAttribute(request, Constants.USERNAME);
        int playerId = userManager.getPlayerID(userNameParameter);

        // get col of move
        int colParameter = ServletUtils.getIntParameter(request, Constants.MOVE_COL);
        if (colParameter == INT_PARAMETER_ERROR) {
            playMoveResponse.setSuccess(false);
            playMoveResponse.setMsg(Constants.MOVE_COL_ERROR);
        }
        String moveTypeParameter = SessionUtils.getAttribute(request, Constants.MOVE_TYPE);

        // get type of move
        MoveType moveType = MoveType.INSERT;
        if (moveTypeParameter.equalsIgnoreCase("insert"))
            moveType = MoveType.INSERT;
        else if (moveTypeParameter.equalsIgnoreCase("popout"))
            moveType = MoveType.POPOUT;
        else {
            playMoveResponse.setSuccess(false);
            playMoveResponse.setMsg(Constants.MOVE_TYPE_ERROR);
        }

        if (playMoveResponse.getSuccess()) {
            synchronized (getServletContext()) {
                if (game.isValidMove(colParameter, moveType)) {
                    playMoveResponse.winners = game.getWinners();
                    playMoveResponse.isTie = game.isTie();
                }
                else {
                    playMoveResponse.setSuccess(false);
                    playMoveResponse.setMsg(Constants.INVALID_MOVE_ERROR);
                }
            }
        }

        ServletUtils.sendJsonResponse(response, playMoveResponse);
    }

    class PlayMoveResponse extends ServeltResponse {
        Set<Integer> winners;
        Boolean isTie;

        public PlayMoveResponse() {
            this.isTie = false;
            this.winners = null;
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
