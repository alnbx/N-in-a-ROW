package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.PlayerSettings;
import webEngine.gamesList.GameListManager;
import webEngine.users.UserManager;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterToGameServlet extends HttpServlet {
    private final String START_NEW_GAME = "/url/of/new/game/servlet";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();

        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        String gameNameFromParameter = request.getParameter(Constants.GAMENAME);

        if (usernameFromSession != null) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            if (gameNameFromParameter != null) {
                GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());
                synchronized (this) {
                    if (!gameListManager.isPlayersListFull(gameNameFromParameter)) {
                        PlayerSettings playerSettings = userManager.getUser(usernameFromSession);
                        gameListManager.registerUserToGame(gameNameFromParameter, playerSettings);
                        SessionUtils.setAttribute(request, Constants.GAMENAME, gameNameFromParameter);
                    }
                    else {
                        registerUserResponse.setSuccess(false);
                        registerUserResponse.setMsg(Constants.GAME_PLAYERS_LIST_IS_FULL_ERROR);
                    }
                }

                if (gameListManager.isPlayersListFull(gameNameFromParameter)) {
                    request.setAttribute(Constants.GAME_SETTINGS_FILE, gameListManager.getGameSettingsFile(gameNameFromParameter));
                    getServletContext().getRequestDispatcher(START_NEW_GAME).forward(request, response);
                    registerUserResponse.isGameStarted = true;
                }
            }
            else {
                registerUserResponse.setSuccess(false);
                registerUserResponse.setMsg(Constants.GAME_NAME_NOT_APPLICABLE_ERROR);
            }
        }
        else {
            registerUserResponse.setSuccess(false);
            registerUserResponse.setMsg(Constants.USER_NAME_NOT_APPLICABLE_ERROR);
        }

        ServletUtils.sendJsonResponse(response, registerUserResponse);
    }

    public class RegisterUserResponse extends ServeltResponse {
        String gameName;
        Boolean isGameStarted;

        public RegisterUserResponse() {
            isGameStarted = false;
            gameName = "";
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