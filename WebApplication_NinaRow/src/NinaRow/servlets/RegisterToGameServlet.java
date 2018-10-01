package NinaRow.servlets;

import NinaRow.constants.Constants;
import NinaRow.utils.ServeltResponse;
import NinaRow.utils.ServletUtils;
import NinaRow.utils.SessionUtils;
import common.PlayerSettings;
import webEngine.gamesList.GameListManager;
import webEngine.gamesList.GameStatus;
import webEngine.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterToGameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // create the response
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();

        String usernameFromSession = SessionUtils.getAttribute(request, Constants.USERNAME);
        if (usernameFromSession != null) {
            String gameNameFromParameter = request.getParameter(Constants.GAMENAME);
            registerUserResponse.gameName = gameNameFromParameter;
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            if (gameNameFromParameter != null) {
                GameListManager gameListManager = ServletUtils.getGamesListManager(getServletContext());
                synchronized (this) {
                    if (!gameListManager.isPlayersListFull(gameNameFromParameter)) {
                        if (!gameListManager.isGameActive(gameNameFromParameter))
                        {
                            PlayerSettings playerSettings = userManager.getUser(usernameFromSession);
                            gameListManager.registerUserToGame(gameNameFromParameter, playerSettings);
                            request.getSession(false).setAttribute(Constants.GAMENAME, gameNameFromParameter);
                        }
                        else {
                            registerUserResponse.setResult(false);
                            registerUserResponse.setMsg(Constants.REGISTER_TO_ACTIVE_GAME_ERROR);
                        }
                    }
                    else {
                        registerUserResponse.setResult(false);
                        registerUserResponse.setMsg(Constants.GAME_PLAYERS_LIST_IS_FULL_ERROR);
                    }
                }

                // if this player makes the num of registered players match the requirement - init the game
                if (gameListManager.isPlayersListFull(gameNameFromParameter)) {
                    gameListManager.initGame(gameNameFromParameter);
                    registerUserResponse.gameStatus = GameStatus.PLAYING;
                }
            }
            else {
                registerUserResponse.setResult(false);
                registerUserResponse.setMsg(Constants.GAME_NAME_NOT_APPLICABLE_ERROR);
            }
        }
        else {
            registerUserResponse.setResult(false);
            registerUserResponse.setMsg(Constants.USER_SESSION_ERROR);
        }

        ServletUtils.sendJsonResponse(response, registerUserResponse);
    }

    public class RegisterUserResponse extends ServeltResponse {
        String gameName;
        GameStatus gameStatus;

        public RegisterUserResponse() {
            gameStatus = GameStatus.PENDING_PLAYERS;
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
