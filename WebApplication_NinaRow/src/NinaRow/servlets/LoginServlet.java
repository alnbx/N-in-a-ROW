package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.SessionUtils;
import NinaRow.utils.ServletUtils;
import common.PlayerTypes;
import webEngine.users.UserManager;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static NinaRow.constants.Constants.USERNAME;
import static NinaRow.constants.Constants.USER_TYPE;
import static NinaRow.constants.Constants.USER_NAME_EXISTS_ERROR;
import static NinaRow.constants.Constants.USER_NAME_NOT_APPLICABLE_ERROR;

public class LoginServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getAttribute(request, USERNAME);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        // create the response
        LoginResponse loginResponse = new LoginResponse();
        String usernameFromParameter = request.getParameter(USERNAME);
        String playerTypeFromParameter = request.getParameter(USER_TYPE);

        if (usernameFromSession == null) {
            //user is not logged in yet
            if (usernameFromParameter == null) {
                //no user in session and no user in parameter
                loginResponse.setMsg(USER_NAME_NOT_APPLICABLE_ERROR);
                loginResponse.setResult(false);
            } else {
                //normalize the user value
                usernameFromParameter = usernameFromParameter.trim();
                loginResponse.user = usernameFromParameter;
                PlayerTypes playerType = PlayerTypes.HUMAN;
                if (playerTypeFromParameter != null &&
                        playerTypeFromParameter.trim().equalsIgnoreCase("computer")) {
                    playerType = PlayerTypes.COMPUTER;
                }
                loginResponse.playerType = playerType;
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        // user already exists
                        loginResponse.setMsg(USER_NAME_EXISTS_ERROR);
                        loginResponse.setResult(false);
                    } else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter, playerType);
                        //set the user in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                    }
                }
            }
        }
        else {
            loginResponse.user = usernameFromSession;
            loginResponse.playerType = userManager.getPlayerType(usernameFromSession);
        }

        ServletUtils.sendJsonResponse(response, loginResponse);
    }

    class LoginResponse extends ServeltResponse {
        private String user = "";
        private PlayerTypes playerType = PlayerTypes.HUMAN;
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
