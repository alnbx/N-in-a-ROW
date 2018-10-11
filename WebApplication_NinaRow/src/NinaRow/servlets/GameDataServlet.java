package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.UserSettings;
import engine.Player;
import webEngine.gamesList.GameListManager;
import engine.Move;
import webEngine.gamesList.GameStatus;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameDataServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        GameDataResponse gameDataResponse = new GameDataResponse();
        int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
        if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            gameDataResponse.gameName = gamesManager.getGameName(gameIdFromParam);

            String gameStatus = request.getParameter(Constants.GAME_STATUS);
            synchronized (this) {
                gameDataResponse.isPlayer = gamesManager.isUserPlayerInGame(gameIdFromParam, usernameFromSession);
                gameDataResponse.gameStatus = gamesManager.getGameStatus(gameIdFromParam);
                if (gameStatus.equalsIgnoreCase("PENDING_PLAYERS")) {
                    gameDataResponse.moves = new ArrayList<>();
                    gameDataResponse.players = gamesManager.getRegisteredPlayers(gameIdFromParam);
                    gameDataResponse.viewers = gamesManager.getRegisteredViewers(gameIdFromParam);
                }
                else {
                    gameDataResponse.moves = gamesManager.getMovesHistory(gameIdFromParam);
                    gameDataResponse.players = gamesManager.getActivePlayers(gameIdFromParam);
                    gameDataResponse.viewers = gamesManager.getActiveViewrs(gameIdFromParam);
                }
            }
        }
        else {
            gameDataResponse.setResult(false);
            gameDataResponse.setMsg(Constants.GAME_ID_ERROR);
        }

        ServletUtils.sendJsonResponse(response, gameDataResponse);
    }

    class GameDataResponse extends ServeltResponse {
        private List<Player> players;
        private List<UserSettings> viewers;
        private List<Move> moves;
        private String gameName;
        private Boolean isPlayer;
        private GameStatus gameStatus;

        public GameDataResponse() {
            this.players = null;
            this.viewers = null;
            this.moves = null;
            this.gameName = "";
            this.isPlayer = false;
            this.gameStatus = GameStatus.PENDING_PLAYERS;
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
