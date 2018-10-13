package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.UserSettings;
import webEngine.chat.ChatsManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import static NinaRow.constants.Constants.USERNAME;

public class GetChatUsersServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, USERNAME);
        ChatsManager chatsManager = ServletUtils.getChatsManager(getServletContext());
        GetChatUsersResponse getChatUsersResponse = new GetChatUsersResponse();

        if (usernameFromSession != null) {
            int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
            if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
                synchronized (this) {
                    getChatUsersResponse.chatUsers = chatsManager.getChatUsers(gameIdFromParam);
                }
            }
            else {
                getChatUsersResponse.setResult(false);
                getChatUsersResponse.setMsg(Constants.GAME_ID_ERROR);
            }
        } else {
            getChatUsersResponse.setResult(false);
            getChatUsersResponse.setMsg(Constants.NO_USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, getChatUsersResponse);
    }

    class GetChatUsersResponse extends ServeltResponse {
        private List<UserSettings> chatUsers;

        public GetChatUsersResponse() {
            this.chatUsers = new ArrayList<>();
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
