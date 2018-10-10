package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import webEngine.gamesList.GameListManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDataServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        BoardDataResponse boardDataResponse = new BoardDataResponse();
        int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
        if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
            GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
            String gameStatus = request.getParameter(Constants.GAME_STATUS);
            synchronized (this) {
                boardDataResponse.boardRowSize = gamesManager.getGameRows(gameIdFromParam);
                boardDataResponse.boardColSize = gamesManager.getGameCols(gameIdFromParam);
                if (gameStatus.equalsIgnoreCase("PENDING_PLAYERS")) {
                    boardDataResponse.boardData = gamesManager.getEmptyGameBoardData(gameIdFromParam);
                    boardDataResponse.currentPlayer = "";
                }
                else {
                    boardDataResponse.boardData = gamesManager.getGameBoardData(gameIdFromParam);
                    boardDataResponse.currentPlayer = ServletUtils.getUserManager(getServletContext()).
                            getPlayerName(gamesManager.getIdOfCurrentPlayer(gameIdFromParam));
                }

            }
        }
        else {
            boardDataResponse.setResult(false);
            boardDataResponse.setMsg(Constants.GAME_ID_ERROR);
        }

        ServletUtils.sendJsonResponse(response, boardDataResponse);
    }


    class BoardDataResponse extends ServeltResponse {
        int boardRowSize;
        int boardColSize;
        // matrix of game board:
        // int is 0 = no disc in position
        // int is not 0 = disc of player id is in position
        int[][] boardData;
        String currentPlayer;

        public BoardDataResponse() {
            this.boardRowSize = 0;
            this.boardColSize = 0;
            this.boardData = null;
            this.currentPlayer = "";
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
