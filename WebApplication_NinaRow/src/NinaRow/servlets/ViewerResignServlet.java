package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewerResignServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String gameName = request.getParameter(Constants.GAMENAME);
        response.setContentType("application/json");

        // create the response
        ViewerResignResponse viewerResignResponse = new ViewerResignResponse();
        if (gameName != null) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            // get player id
            String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (userNameFromSession != null) {
                Integer playerId = userManager.getPlayerID(userNameFromSession);
                if (playerId != null) {
                    GameListManager gamesManager = ServletUtils.getGamesListManager(getServletContext());
                    gamesManager.viewerResigne(gameName, userNameFromSession);
                    userManager.setGameToUser(userNameFromSession, gameName);
                }
                else {
                    viewerResignResponse.setResult(false);
                    viewerResignResponse.setMsg(Constants.USER_NAME_NOT_APPLICABLE_ERROR);
                }
            }
            else {
                viewerResignResponse.setResult(false);
                viewerResignResponse.setMsg(Constants.USER_SESSION_ERROR);
            }
        }
        else {
            viewerResignResponse.setResult(false);
            viewerResignResponse.setMsg(Constants.GAME_NAME_PARAMETER_ERROR);
        }

        ServletUtils.sendJsonResponse(response, viewerResignResponse);
    }

    class ViewerResignResponse extends ServeltResponse { }

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
