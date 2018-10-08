package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.UserSettings;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterViewerToGameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        RegisterViewerResponse registerUserResponse = new RegisterViewerResponse();

        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        if (usernameFromSession != null) {
            int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
            if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());
                registerUserResponse.gameName = gameListManager.getGameName(gameIdFromParam);
                synchronized (this) {
                    UserSettings playerSettings = userManager.getUser(usernameFromSession);
                    gameListManager.registerViewerToGame(gameIdFromParam, playerSettings);
                    userManager.setGameToUser(usernameFromSession, gameIdFromParam);
                }
            }
            else {
                registerUserResponse.setResult(false);
                registerUserResponse.setMsg(Constants.GAME_ID_ERROR);
            }
        }
        else {
            registerUserResponse.setResult(false);
            registerUserResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, registerUserResponse);
    }

    public class RegisterViewerResponse extends ServeltResponse {
        String gameName;

        public RegisterViewerResponse() {
            gameName = "";
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
