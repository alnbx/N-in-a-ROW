package NinaRow.servlets;

import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import webEngine.gamesList.GameListManager;
import webEngine.gamesList.GameStatus;
import webEngine.gamesList.SingleGameEntry;
import common.GameVariant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamesListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");

        GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
        List<SingleGameEntry> gamesList = gamesManager.getGames();
        List<SingleGameResponse> games = new ArrayList<>();

        for (SingleGameEntry seg : gamesList) {
            games.add(new SingleGameResponse(seg));
        }

        ServletUtils.sendJsonResponse(response, new GamesListResponse((games)));
    }

    public class GamesListResponse extends ServeltResponse {
        final List<SingleGameResponse> games;
        final int totalgames;

        public GamesListResponse(List<SingleGameResponse> games) {
            this.totalgames = games.size();
            this.games = games;
        }
    }

    public class SingleGameResponse {
        private String name;
        private GameStatus status;
        private String userName;
        private int boardRows;
        private int boardCols;
        private int target;
        private GameVariant gameVariant;
        private int totalPlayers;
        private int registeredPlayers;

        public SingleGameResponse(SingleGameEntry gameEntry) {
            this.name = gameEntry.getGameName();
            this.status = gameEntry.getGameStatus();
            this.userName = gameEntry.getUserName();
            this.registeredPlayers = gameEntry.getNumRegisteredPlayers();
            this.boardRows = gameEntry.getRows();
            this.boardCols = gameEntry.getCols();
            this.target = gameEntry.getSequenceLength();
            this.gameVariant = gameEntry.getGameVariant();
            this.totalPlayers = gameEntry.getNumRequiredPlayers();
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
