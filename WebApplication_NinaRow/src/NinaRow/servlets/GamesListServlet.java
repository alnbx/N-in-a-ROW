package NinaRow.servlets;

import NinaRow.utils.ServletUtils;
import com.google.gson.Gson;
import webEngine.games.GameListManager;
import webEngine.games.GameStatus;
import webEngine.games.SingleGameEntry;
import common.GameVariant;
import engine.GameLogic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GamesListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            GameListManager gamesManager = ServletUtils.getGamesManager(getServletContext());
            List<SingleGameEntry> gamesList = gamesManager.getGames();
            List<SingleGameResponse> gamesListResponse = new ArrayList<>();

            for (SingleGameEntry seg : gamesList) {
                gamesListResponse.add(new SingleGameResponse(seg));
            }

            String json = gson.toJson(gamesListResponse);
            out.println(json);
            out.flush();
        }
    }

    public class SingleGameResponse {
        private String gameName;
        private GameStatus gameStatus;
        private String userName;
        private int boardRows;
        private int boardCols;
        private int target;
        private GameVariant gameVariant;
        private int numRequiredPlayers;
        private int numRegisteredPlayers;

        public SingleGameResponse(SingleGameEntry gameEntry) {
            this.gameName = gameEntry.getGameName();
            this.gameStatus = gameEntry.getGameStatus();
            this.userName = gameEntry.getUserName();
            this.numRegisteredPlayers = gameEntry.getNumRegisteredPlayers();
            GameLogic gameLogic = gameEntry.getGameLogic();
            this.boardRows = gameLogic.getRows();
            this.boardCols = gameLogic.getCols();
            this.target = gameLogic.getSequenceLength();
            this.gameVariant = gameLogic.getGameVariant();
            this.numRequiredPlayers = gameLogic.getNumberOfRequiredPlayers();
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
