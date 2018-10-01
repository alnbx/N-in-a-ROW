package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import engine.GameLogic;
import engine.Player;
import webEngine.gamesList.GameListManager;
import engine.Move;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GameDataServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String gameNameFromSession = SessionUtils.getAttribute(request, Constants.GAMENAME);
        GameDataResponse gameDataResponse = new GameDataResponse();
        gameDataResponse.gameName = gameNameFromSession;

        if (gameNameFromSession != null) {
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());

            synchronized (getServletContext()) {
                GameLogic gameLogic = gamesManager.getGameEntry(gameNameFromSession).getGameLogic();
                gameDataResponse.moves = gameLogic.getMovesHistory();
                gameDataResponse.players = gameLogic.getPlayers();
            }
        }
        else {
            gameDataResponse.setResult(false);
            gameDataResponse.setMsg(Constants.GAME_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, gameDataResponse);
    }

    class GameDataResponse extends ServeltResponse {
        List<Player> players;
        List<Move> moves;
        String gameName;
        Boolean isPlayer;

        public GameDataResponse() {
            this.players = null;
            this.moves = null;
            this.gameName = "";
            this.isPlayer = false;
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
