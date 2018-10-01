package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import engine.Board;
import engine.GameLogic;
import engine.Player;
import webEngine.gamesList.GameListManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GameStatusServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String gameNameFromSession = SessionUtils.getAttribute(request, Constants.GAMENAME);
        GameStatusResponse gameStatusResponse = new GameStatusResponse();

        if (gameNameFromSession != null) {
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
            //GameLogic gameLogic = null;
            synchronized (getServletContext()) {
                GameLogic gameLogic = gamesManager.getGameEntry(gameNameFromSession).getGameLogic();
                gameStatusResponse.players = gameLogic.getPlayers();
                gameStatusResponse.board = gameLogic.getBoardAsIntArr();
                int currentPlayerId = gameLogic.getIdOfCurrentPlayer();
                gameStatusResponse.currentPlayer = getCurrentPlayer(gameStatusResponse, currentPlayerId);
            }
        }
        else {
            gameStatusResponse.setSuccess(false);
            gameStatusResponse.setMsg(Constants.GAME_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, gameStatusResponse);
    }

    private Player getCurrentPlayer(GameStatusResponse gameStatusResponse, int playerId) {
        Player currentPlayer = null;
        for (Player player : gameStatusResponse.players) {
            if (player.getId() == playerId) {
                currentPlayer = player;
                break;
            }
        }

        return currentPlayer;
    }

    class GameStatusResponse extends ServeltResponse {
        List<Player> players;
        // matrix of game board:
        // int is 0 = no disc in position
        // int is not 0 = disc of player id is in position
        int[][] board;
        Player currentPlayer;

        public GameStatusResponse() {
            this.players = null;
            this.board = null;
            this.currentPlayer = null;
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
