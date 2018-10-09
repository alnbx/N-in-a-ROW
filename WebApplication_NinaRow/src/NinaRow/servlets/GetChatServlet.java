package NinaRow.servlets;

import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import NinaRow.constants.Constants;
import webEngine.chat.ChatsManager;
import webEngine.chat.SingleChatEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static NinaRow.constants.Constants.USERNAME;

public class GetChatServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, USERNAME);
        ChatsManager chatsManager = ServletUtils.getChatsManager(getServletContext());
        GetChatResponse getChatResponse = new GetChatResponse();

        if (usernameFromSession != null) {
            int gameIdFromParam = ServletUtils.getIntParameter(request, Constants.GAME_ID);
            if (gameIdFromParam != Constants.INT_PARAMETER_ERROR) {
                int chatVersionClient = ServletUtils.getIntParameter(request, Constants.CHAT_VERSION_PARAMETER);
                if (chatVersionClient == Constants.INT_PARAMETER_ERROR) {
                    getChatResponse.setResult(false);
                    getChatResponse.setMsg(Constants.CHAT_VERSION_ERROR);
                }
                else {
                    synchronized (this) {
                        getChatResponse.version = chatsManager.getChatVersion(gameIdFromParam);
                        getChatResponse.entries = chatsManager.getChatEntries(gameIdFromParam, chatVersionClient);
                    }
                }
            }
            else {
                getChatResponse.setResult(false);
                getChatResponse.setMsg(Constants.GAME_ID_ERROR);
            }
        }
        else {
            getChatResponse.setResult(false);
            getChatResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, getChatResponse);

    }

    class GetChatResponse extends ServeltResponse {
        private List<SingleChatEntry> entries;
        private int version;

        public GetChatResponse() {
            this.entries = null;
            this.version = 0;
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
