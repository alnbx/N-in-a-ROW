package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.SessionUtils;
import NinaRow.utils.ServletUtils;
import com.google.gson.Gson;
import common.PlayerTypes;
import webEngine.users.UserManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static NinaRow.constants.Constants.USERNAME;
import static NinaRow.constants.Constants.USER_TYPE;
import static NinaRow.constants.Constants.USER_NAME_EXISTS_ERROR;
import static NinaRow.constants.Constants.USER_NAME_NOT_APPLICABLE_ERROR;

public class LoginServlet extends HttpServlet {

    // urls that starts with forward slash '/' are considered absolute
    // urls that doesn't start with forward slash '/' are considered relative to the place where this servlet request comes from
    // you can use absolute paths, but then you need to build them from scratch, starting from the context path
    // ( can be fetched from request.getContextPath() ) and then the 'absolute' path from it.
    // Each method with it's pros and cons...
    private final String GAMES_LIST_URL = "../gameslist/gameslist.html";
    private final String SIGN_UP_URL = "../signup/singup.html";
    private final String LOGIN_ERROR_URL = "/pages/loginerror/login_attempt_after_error.jsp";
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
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        // create the response
        LoginResponse loginResponse = new LoginResponse();
        String redirectURL = GAMES_LIST_URL;
        String usernameFromParameter = request.getParameter(USERNAME);
        String playerTypeFromParameter = request.getParameter(USER_TYPE);

        if (usernameFromSession == null) {
            //user is not logged in yet
            if (usernameFromParameter == null) {
                //no username in session and no username in parameter
                loginResponse.setMsg(USER_NAME_NOT_APPLICABLE_ERROR);
                loginResponse.setSuccess(false);

                //redirect back to the index page
                redirectURL = SIGN_UP_URL;
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                loginResponse.setUsername(usernameFromParameter);
                PlayerTypes playerType = playerTypeFromParameter.equalsIgnoreCase("computer") ?
                        PlayerTypes.COMPUTER : PlayerTypes.HUMAN;
                loginResponse.setPlayerType(playerType);
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        // username already exists
                        loginResponse.setMsg(USER_NAME_EXISTS_ERROR);
                        loginResponse.setSuccess(false);
                        redirectURL = LOGIN_ERROR_URL;
                    } else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter, playerType);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                    }
                }
            }
        }
        else {
            loginResponse.setUsername(usernameFromSession);
        }

        sendJsonResponse(response, loginResponse);
        response.sendRedirect(redirectURL);
    }

    private void sendJsonResponse(HttpServletResponse response, LoginResponse loginResponse) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(loginResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    class LoginResponse {

        private Boolean success = true;
        private String msg = "";
        private String username = "";
        private PlayerTypes playerType = PlayerTypes.HUMAN;

        public void setPlayerType(PlayerTypes playerType) {
            this.playerType = playerType;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setUsername(String username) {
            this.username = username;
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
