package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import webEngine.chat.ChatsManager;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static NinaRow.constants.Constants.USERNAME;

public class LoginToChatServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, USERNAME);
        ChatsManager chatsManager = ServletUtils.getChatsManager(getServletContext());
        LoginToChatResponse loginToChatResponse = new LoginToChatResponse();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
            if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
                GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());
                if (!gameListManager.isUserViewer(gameIdFromParam,usernameFromSession)) {
                    synchronized (this) {
                        if (!chatsManager.isUserExistsInChat(gameIdFromParam, usernameFromSession)) {
                            chatsManager.addUserToChat(gameIdFromParam, userManager.getUserSettings(usernameFromSession));
                        } else {
                            loginToChatResponse.setResult(false);
                            loginToChatResponse.setMsg(Constants.USER_EXISTS_IN_CAHT_ERROR);
                        }
                    }
                }
                else {
                    loginToChatResponse.setResult(false);
                    loginToChatResponse.setMsg(Constants.VIEWER_CHAT_ERROR);
                }
            }
            else {
                loginToChatResponse.setResult(false);
                loginToChatResponse.setMsg(Constants.GAME_ID_ERROR);
            }
        } else {
            loginToChatResponse.setResult(false);
            loginToChatResponse.setMsg(Constants.NO_USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, loginToChatResponse);
    }

    class LoginToChatResponse extends ServeltResponse { }

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
