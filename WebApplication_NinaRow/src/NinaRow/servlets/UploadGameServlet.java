package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import com.google.gson.Gson;
import common.PlayerTypes;
import webEngine.games.GameListManager;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static NinaRow.constants.Constants.*;

public class UploadGameServlet extends HttpServlet {
    private final String GAMES_LIST_URL = "/pages/gameslist/gameslist.html"; // must start with '/' since will be used in request dispatcher
    private final String GAME_UPLOAD_ERROR_URL = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String settingsFileFromParameter = request.getParameter(SETTINGS_FILE);
        String usernameFromSession = SessionUtils.getUsername(request);
        GameListManager gameListManager = ServletUtils.getGamesManager(getServletContext());

        // create the response
        UploadGameResponse uploadGameResponse = new UploadGameResponse();

        if (settingsFileFromParameter == null) {
            // no settings file in parameter
            uploadGameResponse.setMsg(SETTINGS_FILE_NOT_APPLICABLE_ERROR);
            uploadGameResponse.setSuccess(false);

        } else {
            // normalize the settings file path string
            settingsFileFromParameter = settingsFileFromParameter.trim();
            synchronized (this) {
                try {
                    if (gameListManager.isGameExists(settingsFileFromParameter)) {
                        uploadGameResponse.setMsg(SETTINGS_FILE_EXISTS_ERROR);
                        uploadGameResponse.setSuccess(false);
                    }
                    else {
                        gameListManager.addGame(settingsFileFromParameter, usernameFromSession);
                    }
                }
                catch (Exception e) {
                    uploadGameResponse.setMsg(e.getMessage());
                    uploadGameResponse.setSuccess(false);
                }
            }
        }

        sendJsonResponse(response, uploadGameResponse);
        getServletContext().getRequestDispatcher(GAMES_LIST_URL).forward(request, response);;
    }

    private void sendJsonResponse(HttpServletResponse response, UploadGameServlet.UploadGameResponse uploadGameResponse) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(uploadGameResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    class UploadGameResponse {
        private Boolean success = true;
        private String msg = "";
        private String gameName = "";

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
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
