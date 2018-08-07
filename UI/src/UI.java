import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import common.PlayersTypes;
import engine.GameLogic;
import engine.Game;
import engine.Move;

/**
 * Created by user on 30/07/2018.
 */

public class UI
{
    private String xmlPath;
    private Menu firstMenu;
    private Menu mainMenu;
    private Menu gameMenu;
    private PrintMessages winningMessage;
    private PrintMessages endGame;
    private GameLogic gameLogic;

    public static Scanner scanner = new Scanner(System.in);
    public static final char[] playerDiscs = {' ', '@', '#', '$', '%', '&', '+', '~'};

    public UI(String xmlPath)
    {
        this.xmlPath = xmlPath;
        this.firstMenu = new FirstMenu();
        this.mainMenu = new PrimaryMenu();
        this.gameMenu = new GameMenu();
        this.winningMessage = new WinnerMessage();
        this.endGame = new EndGameMessage();
        this.gameLogic = new Game();

        //prepareGame();
        //printBoard();
    }

    public void playGame() {
        firstMenu.showMenu();
        loadXML();
        mainGameLoop();
    }

    /*
    private void prepareGame()
    {
        //MenuChoice choice = MenuChoice.INVALIDCHOICE;
        firstMenu.showMenu();
        loadXML();
        //loadFirstXML();
        //handleUserChoicePrimaryMenu(mainMenu.showMenu());
        //printBoard();
        mainGameLoop();
    }
    */

    private void mainGameLoop() {
        boolean stayInGame = true;
        printBoard();
        while (stayInGame) {
            stayInGame = handleUserChoicePrimaryMenu(mainMenu.showMenu());
        }

        System.out.println("Bye Bye :(");
        System.exit(0);
    }

    /*
    private void loadFirstXML()
    {
        boolean configurationLoaded = false;

        while (!configurationLoaded) {
            try {
                gameLogic.loadSettingsFile(this.xmlPath);
                configurationLoaded = true;
            } catch (Exception e) {
                System.out.println("Sorry to interrupt but the configuration file is invalid:\n");
                //System.out.println(e.getStackTrace());
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        }

        do {
            if (!gameLogic.loadSettingsFile(this.xmlPath)) {
                System.out.println("Sorry to interrupt but the configuration file is invalid");
                checkAndPrintWhyXMLInvalid();
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        } while (!gameLogic.configurationLoaded());

    }
    */

    private void loadXML() {
        boolean configurationLoaded = false;

        while (!configurationLoaded) {
            try {
                gameLogic.loadSettingsFile(this.xmlPath);
                configurationLoaded = true;
            } catch (Exception e) {
                System.out.println("Sorry to interrupt but the configuration file is invalid:\n");
                //System.out.println(e.getStackTrace());
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        }
    }

    private boolean handleUserChoicePrimaryMenu(MenuChoice userChoice)
    {
        boolean continueGame = true;
        switch (userChoice){
            case LOADXML:
                System.out.println("Please enter the path to the XML you wish to loadSettingsFile: ");
                this.xmlPath = scanner.nextLine();
                loadXML();
                printBoard();
                /*
                try {
                    gameLogic.loadSettingsFile(path);
                    System.out.println("New configuration loaded successfully");
                } catch (Exception e){
                    System.out.println("Bad XML was loaded: " + e + "\nLoaded last legal configuration");
                }
                */
                //gameLogic.loadSettingsFile(path) ? System.out.println("New configuration loaded successfully") :
                //                    System.out.println("Bad XML was loaded.\nLoaded last legal configuration");
                break;

            case STARTGAME:
                continueGame = startGame();
                break;


            case EXIT:
                continueGame = false;
                /*
                System.out.println("Bye Bye :(");
                System.exit(0);
                break;
                */
        }

        return continueGame;
    }

    private boolean startGame()
    {
        choosePlayersType();
        boolean continuePlaying = playSingleRound();
        return continuePlaying;
    }

    private boolean playSingleRound()
    {
        printBoard();
        MenuChoice userChoice = gameMenu.showMenu();
        handleUserChoiceGameMenu(userChoice);
        boolean continuePlaying = true;

        while((!gameLogic.getHasWinner()) && (!gameLogic.getIsBoardFull()) && MenuChoice.EXIT != userChoice) {
            printBoard();
        }

        if (gameLogic.getHasWinner()) {
            winningMessage.printMessage(gameLogic.getIdOfCurrentPlayer());
        }
        else if (gameLogic.getIsBoardFull()) {
            endGame.printMessage(0);
        }
        else {
            continuePlaying = false;
        }

        return continuePlaying;

        //TODO: if Engine.getHasWinner/endGame we need to start a new game? show main menu? reset everything?
        //TODO: add function to reset the board.
    }

    private void handleUserChoiceGameMenu(MenuChoice userChoice)
    {
        switch (userChoice){
            case GAMESTATS: showGameStats(); break;
            case MAKETURN: playTurn(); break;
            case HISTORY: showTurnsHistory(); break;
            //case EXIT: System.out.println("Bye Bye"); System.exit(0);
        }
    }

    private void showTurnsHistory()
    {
        for (Move m : gameLogic.getMovesHistory()) {
            System.out.println(String.format("Player %d has thrown a disc to column %d", m.getPlayerId(), m.getCol()));
        }
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
                input = Integer.parseInt(UI.scanner.nextLine());

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
        //boolean onceAgain = true;
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
                    this.gameLogic.initPlayer(PlayersTypes.ROBOT, initializedPlayers + 1 , "Computer");
                    robotsCounter++;
                    initializedPlayers++;
                }
            } else {
                System.out.print("Please type player's name: ");
                String name = scanner.nextLine();
                this.gameLogic.initPlayer(PlayersTypes.HUMAN, initializedPlayers + 1, name);
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

    private void writeGameToFile() throws IOException
    {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("ddMMYY_HHmm");
        String time = localDateFormat.format(new Date());

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("N-in-a-Row_" + time))) {
            out.writeObject(this.gameLogic);
            out.flush();
        }
    }

    private void loadGameFromFile(String gameFile) throws IOException, ClassNotFoundException
    {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(gameFile))) {
            this.gameLogic = (Game)in.readObject();
        }
    }

    private void printLineSeparator(int cols)
    {
        for (int i = 0; i < (int)(((13 + ((cols - 1) * 6)) / 2.0) + 0.5); i++) { System.out.print("- "); }
        System.out.println();
    }

    private void printTopScales(int cols)
    {

        //printLineSeparator(cols);

        System.out.print("      ");
        for (int i = 0; i < cols; i++) { System.out.print(String.format("|  %d  ", i+1)); }
        System.out.println("|");

        printLineSeparator(cols);
        System.out.println("\n");
    }

    private void printBoard()
    {
        char[][] board = gameLogic.boardReadyToPrint();
        int cols = gameLogic.getCols();

        System.out.println("\n");

        for(int row = 0; row < board.length; row++ )
        {
            System.out.print(String.format("   %d  ", row + 1));
            for (int col = 0; col < cols; col++) {
                int i = board[row][col] - '0';
                System.out.print(String.format("|  %c  ",playerDiscs[i]));
            }
            System.out.println("|");
            printLineSeparator(cols);
        }

        printTopScales(cols);
    }

    public static void main(String[] args)
    {
        UI ui;
        //TODO: For debug only!
        if (args.length == 0) { ui = new UI("/Users/Miri/Documents/MTA_computerScience/JAVA/exercises/ex1-small.xml"); }
        else { ui = new UI(args[0]); }
        ui.playGame();
    }
}
