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

public class GameDataServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        String gameNameParameter = request.getParameter(Constants.GAMENAME);
        GameDataResponse gameDataResponse = new GameDataResponse();

        if (gameNameParameter != null) {
            gameDataResponse.gameName = gameNameParameter;
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());

            synchronized (getServletContext()) {
                GameLogic gameLogic = gamesManager.getGameEntry(gameNameParameter).getGameLogic();
                gameDataResponse.isPlayer = gamesManager.isUserPlayerInGame(gameNameParameter, usernameFromSession);
                gameDataResponse.moves = gameLogic.getMovesHistory();
                gameDataResponse.players = gameLogic.getPlayers();
                gameDataResponse.viewers = gamesManager.getGameViewrs(gameNameParameter);
                gameDataResponse.gameStatus = gamesManager.getGameStatus(gameNameParameter);
            }
        }
        else {
            gameDataResponse.setResult(false);
            gameDataResponse.setMsg(Constants.GAME_NAME_PARAMETER_ERROR);
        }

        ServletUtils.sendJsonResponse(response, gameDataResponse);
    }

    class GameDataResponse extends ServeltResponse {
        List<Player> players;
        List<UserSettings> viewers;
        List<Move> moves;
        String gameName;
        Boolean isPlayer;
        GameStatus gameStatus;

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
