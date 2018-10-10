package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.UserSettings;
import engine.GameLogic;
import engine.Player;
import webEngine.gamesList.GameListManager;
import engine.Move;
import webEngine.gamesList.GameStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            gameDataResponse.gameName = gamesManager.getGameName(gameIdFromParam);

            synchronized (this) {
                GameLogic gameLogic = gamesManager.getGameEntry(gameIdFromParam).getGameLogic();
                gameDataResponse.isPlayer = gamesManager.isUserPlayerInGame(gameIdFromParam, usernameFromSession);
                gameDataResponse.moves = gameLogic.getMovesHistory();
                gameDataResponse.players = gameLogic.getPlayers();
                gameDataResponse.viewers = gamesManager.getGameViewrs(gameIdFromParam);
                gameDataResponse.gameStatus = gamesManager.getGameStatus(gameIdFromParam);
                gameDataResponse.hasWinner = gamesManager.getGameHasWinner(gameIdFromParam);
                gameDataResponse.isTie = gamesManager.getGameIsTie(gameIdFromParam);
                gameDataResponse.winners = gamesManager.getGameWinners(gameIdFromParam);
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
        private boolean hasWinner;
        private boolean isTie;
        private Set<String> winners;


        public GameDataResponse() {
            this.players = null;
            this.viewers = null;
            this.moves = null;
            this.gameName = "";
            this.isPlayer = false;
            this.gameStatus = GameStatus.PENDING_PLAYERS;
            this.hasWinner = false;
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
