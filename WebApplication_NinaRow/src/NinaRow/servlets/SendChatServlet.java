package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import webEngine.chat.ChatsManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class SendChatServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        SendChatResponse sendChatResponse = new SendChatResponse();
        ChatsManager chatsManager = ServletUtils.getChatsManager(getServletContext());
        String userNameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);


        if (userNameFromSession != null) {
            String gameName = request.getParameter(Constants.GAMENAME);
            if (gameName != null) {
                String userChatString = request.getParameter(Constants.CHAT_PARAMETER);
                if (userChatString != null && !userChatString.isEmpty()) {
                    synchronized (getServletContext()) {
                        chatsManager.addChatString(gameName, userChatString, userNameFromSession);
                    }
                }
            }
            else {
                sendChatResponse.setResult(false);
                sendChatResponse.setMsg(Constants.GAME_NAME_PARAMETER_ERROR);
            }
        }
        else {
            sendChatResponse.setResult(false);
            sendChatResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, sendChatResponse);
    }

    class SendChatResponse extends ServeltResponse { }

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
