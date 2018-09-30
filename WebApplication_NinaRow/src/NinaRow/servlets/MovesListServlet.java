package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.SessionUtils;
import NinaRow.utils.ServletUtils;
import engine.GameLogic;
import engine.Move;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MovesListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        MovesListResponse movesListResponse = new MovesListResponse();
        String gameName = SessionUtils.getAttribute(request, Constants.GAMENAME);
        if (gameName != null) {
            int clientNumMoves = ServletUtils.getIntParameter(request, Constants.LAST_MOVE);
            if (clientNumMoves == Constants.INT_PARAMETER_ERROR) {
                movesListResponse.setSuccess(false);
                movesListResponse.setMsg(Constants.MOVE_COL_ERROR);
            }
            else {
                GameLogic game = ServletUtils.getGamesListManager(getServletContext()).
                        getGameEntry(gameName).getGameLogic();
                List<Move> moves = null;
                synchronized (getServletContext()) {
                    moves = game.getMovesHistory();
                }
                if (clientNumMoves < 0 || clientNumMoves >= moves.size()) {
                    clientNumMoves = 0;
                }
                movesListResponse.serverNumMoves = moves.size();
                movesListResponse.movesDelta = moves.subList(clientNumMoves, moves.size());
            }
        }
        else {
            movesListResponse.setSuccess(false);
            movesListResponse.setMsg(Constants.GAME_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, movesListResponse);
    }

    class MovesListResponse extends ServeltResponse {
        private List<Move> movesDelta;
        private int serverNumMoves;

        public MovesListResponse() {
            this.movesDelta = null;
            this.serverNumMoves = 0;
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
