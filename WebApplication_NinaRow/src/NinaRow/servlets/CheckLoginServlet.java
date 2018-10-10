package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckLoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        CheckLoginResponse checkLoginResponse = new CheckLoginResponse();
        if (SessionUtils.isSessionValid(request)) {
            String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
            if (usernameFromSession != null) {
                checkLoginResponse.user = usernameFromSession;
            }
            else {
                checkLoginResponse.setResult(false);
                checkLoginResponse.setMsg(Constants.USER_SESSION_ERROR);
            }
        }
        else {
            checkLoginResponse.setResult(false);
            checkLoginResponse.setMsg(Constants.INVALID_SESSION_ERROR);
        }


        ServletUtils.sendJsonResponse(response, checkLoginResponse);
    }

    class CheckLoginResponse extends ServeltResponse {
        private String user;

        public CheckLoginResponse() {
            this.user = "";
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
