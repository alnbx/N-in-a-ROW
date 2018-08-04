import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.List;

import common.PlayersTypes;
import engine.GameLogic;
import engine.Game;
import engine.Move;

/**
 * Created by user on 30/07/2018.
 */

public class ui
{
    private String xmlPath;
    private Menu firstMenu;
    private Menu mainMenu;
    private Menu gameMenu;
    private PrintMessages winningMessage;
    private PrintMessages endGame;
    //private int playerAmmount = 2; //TODO: must be changed???
    private GameLogic gameLogic;


    public static Scanner scanner = new Scanner(System.in);
    public static final char[] playerDiscs = {'@', '#', '$', '%', '&', '+', '~'};


    public ui(String xmlPath)
    {
        this.xmlPath = xmlPath;
        this.firstMenu = new FirstMenu();
        this.mainMenu = new PrimaryMenu();
        this.gameMenu = new GameMenu();
        this.winningMessage = new WinnerMessage();
        this.endGame = new EndGameMessage();
        this.gameLogic = new Game();

        prepareGame();
    }

    private void prepareGame()
    {
        MenuChoice choice = MenuChoice.INVALIDCHOICE;
        firstMenu.showMenu();
        loadFirstXML();
        handleUserChoicePrimaryMenu(mainMenu.showMenu());
    }

    private void loadFirstXML()
    {
        boolean configurationLoaded = false;

        while (!configurationLoaded) {
            try {
                gameLogic.load(this.xmlPath);
                configurationLoaded = true;
            } catch (Exception e) {
                System.out.println("Sorry to interrupt but the configuration file is invalid:\n");
                System.out.println(e);
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        }
        /*
        do {
            if (!gameLogic.load(this.xmlPath)) {
                System.out.println("Sorry to interrupt but the configuration file is invalid");
                checkAndPrintWhyXMLInvalid();
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        } while (!gameLogic.configurationLoaded());
        */
    }

    private void handleUserChoicePrimaryMenu(MenuChoice userChoice)
    {
        switch (userChoice){
            case LOADXML:
                String path = scanner.nextLine();
                try {
                    gameLogic.load(path);
                    System.out.println("New configuration loaded successfully");
                } catch (Exception e){
                    System.out.println("Bad XML was loaded: " + e + "\nLoaded last legal configuration");
                }
                //gameLogic.load(path) ? System.out.println("New configuration loaded successfully") :
                //                    System.out.println("Bad XML was loaded.\nLoaded last legal configuration");
                break;

            case STARTGAME:
                startGame();
                break;

            case EXIT:
                System.out.println("Bye Bye :(");
                System.exit(0);
                break;
        }
    }

    private void startGame()
    {
        choosePlayersType();
        playGame();
    }

    private void playGame()
    {
        while ((false == gameLogic.getHasWinner()) && (false == gameLogic.getIsBoardFull())) {
            //TODO: printBoard
            handleUserChoiceGameMenu(gameMenu.showMenu());
        }

        //gameLogic.getHasWinner() ? winningMessage.printMessage(gameLogic.getWinner()) : endGame.printMessage(0);
        if (gameLogic.getHasWinner()) {
            winningMessage.printMessage(gameLogic.getIdOfCurrentPlayer());
        }
        else {
            endGame.printMessage(0);
        }


        //TODO: if Engine.getHasWinner we need to start a new game? show main menu? reset everything?
    }

    private void handleUserChoiceGameMenu(MenuChoice userChoice)
    {
        switch (userChoice){
            case GAMESTATS: showGameStats(); break;
            case MAKETURN: playTurn(); break;
            case HISTORY: showTurnsHistory(); break;
            case EXIT: System.out.println("Bye Bye"); System.exit(0);
        }
    }

    private void showTurnsHistory() {
        List<Move> movesHistory = gameLogic.getMovesHistory();
        //TODO: print moves history
    }

    private void showGameStats()
    {
        //int playerNumber = 0;

        //playerNumber = gameLogic.getPlayerNumber();
        int playerAmmount = gameLogic.getNumberOfPlayers();

        System.out.println("========== Game statistics ==========");
        System.out.println("=== Current Player ==");
        //System.out.println("Current player: " + gameLogic.getPlayerNumber());
        System.out.println("Current player: " + gameLogic.getIdOfCurrentPlayer());

        System.out.println("=== Players discs ===");
        for (int i = 0; i < playerAmmount; i++) {
            System.out.println(String.format("Player: %d Disc: %c", i+1, playerDiscs[i]));
        }

        System.out.println("=== Player turns ===");
        for (int i = 0; i < playerAmmount; i++) {
            System.out.println(String.format("Player: %d Turns: %d", i+1, gameLogic.playerTurns(i)));
        }

        System.out.println("=== Elapsed time ===");
        System.out.println(String.format("Elapsed time: " + gameLogic.timeFromBegining()));
    }

    private void playTurn()
    {
        int col = 0;

        while(true) {
            //if (Engine.playerType == PlayerTypes.HUMAN) { col = getUserInputCol(); }
            if (gameLogic.getTypeOfCurrentPlayer() == PlayersTypes.HUMAN) { col = getUserInputCol(); }

            if (!gameLogic.play(col)) {
                System.out.println("Illigal move. please try again.");
                col = getUserInputCol();
            }
            else { break; }
        }
    }

    private int getUserInputCol()
    {
        int input = 0;

        while (true) {
            try {
                System.out.println("Please enter the col you wish to drop a disc: ");
                input = Integer.parseInt(ui.scanner.nextLine());

                if (input > gameLogic.getCols() + 1 || input < 1) {
                    System.out.println("Out of bound col. Please try again. ");
                    continue;
                }
                else { break; }

            } catch (NumberFormatException nfe) {
                System.out.println("Not a number. Try again: ");
            }
        }

        return input;
    }

    private void choosePlayersType()
    {
        int playersAmount = gameLogic.getNumberOfPlayers();
        int robotsCounter = 0;
        PlayersTypes[] playersType = new PlayersTypes[playersAmount];
        String userChoice;
        boolean onceAgain = true;
        int initializedPlayers = 0;

        System.out.println("Before the game begins, please enter the types of the players! :)");
        System.out.println("Please type h for human player and r for robotic player ");

        while (initializedPlayers < playersAmount) {
            System.out.print("Please type your choice for player " + initializedPlayers + 1 + ":  ");
            userChoice = scanner.nextLine();
            System.out.println();

            while (!(userChoice.toLowerCase().equals("r") || userChoice.toLowerCase().equals("h"))) {
                System.out.print("Oops, bad choice. Please try again:  ");
                userChoice = scanner.nextLine();
                System.out.println();
            }

            if (userChoice.toLowerCase().equals("r")) {
                if (robotsCounter == playersAmount) {
                    System.out.println("You chose all players to be robots and that is not legal. Please choose again");
                }
                else {
                    gameLogic.initPlayer(PlayersTypes.ROBOT, initializedPlayers + 1 , "Computer");
                    robotsCounter++;
                    initializedPlayers++;
                }
            } else {
                System.out.print("Please type player's name: ");
                String name = scanner.nextLine();
                gameLogic.initPlayer(PlayersTypes.HUMAN, initializedPlayers + 1, name);
                initializedPlayers++;
            }
        }
        /*
        while (onceAgain) {
            for (int i = 0; i < playersAmount; i++) {
                System.out.print("Please type your choice for player " + i + ":  ");
                userChoice = scanner.nextLine();
                System.out.println();

                while (!(userChoice.toLowerCase().equals("r") || userChoice.toLowerCase().equals("h"))) {
                    System.out.print("Oops, bad choice. Please try again:  ");
                    userChoice = scanner.nextLine();
                    System.out.println();
                }

                if (userChoice.toLowerCase().equals("r")) {
                    gameLogic.initPlayer(PlayersTypes.ROBOT, i + 1, "Computer");
                    //playersType[i] = PlayersTypes.ROBOT;
                    robotsCounter++;
                } else {
                    System.out.print("Please type player's name: ");
                    String name = scanner.nextLine();
                    gameLogic.initPlayer(PlayersTypes.HUMAN, i + 1, name);
                    //playersType[i] = PlayersTypes.HUMAN;
                }
            }

            onceAgain = robotsCounter == playersAmount;
            if (onceAgain) { System.out.println("You chose all players to be robots and that is not legal. Please choose again"); }
        }

        for (int i = 0; i < playersAmount; i++) { gameLogic.setPlayerType(i, playersType[i]); }
        */
    }

    private void writeGameToFile() throws IOException {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("ddMMYY_HHmm");
        String time = localDateFormat.format(new Date());

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("N-in-a-Row_" + time))) {
            out.writeObject(gameLogic);
            out.flush();
        }
    }

    private void loadGameFromFile(String gameFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(gameFile))) {
            gameLogic = (Game)in.readObject();
        }
    }

    public static void main(String[] args) {
        ui UI = new ui(args[0]);

    }
}
