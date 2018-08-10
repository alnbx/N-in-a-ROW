package ui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private Menu welcomeMessage;
    private Menu mainMenu;
    private Menu gameMenu;
    private Menu primaryMenu_wRestart;
    private PrintMessages winningMessage;
    private PrintMessages endGame;
    private GameLogic gameLogic;
    private boolean playersSet;
    private boolean isValidXML;

    public static Scanner scanner = new Scanner(System.in);
    public static final char[] playerDiscs = {' ', '@', '#', '$', '%', '&', '+', '~'};

    public UI()
    {
        this.xmlPath = "";
        this.welcomeMessage = new WelcomeMessage();
        this.mainMenu = new PrimaryMenu();
        this.gameMenu = new GameMenu();
        this.winningMessage = new WinnerMessage();
        this.endGame = new EndGameMessage();
        this.gameLogic = new Game();
        this.playersSet = false;
        this.isValidXML = false;
    }


    public void playGame()
    {
        boolean stayInGame = true;
        welcomeMessage.showMenu();
        while (stayInGame) {
            stayInGame = handleUserChoicePrimaryMenu(mainMenu.showMenu());

            if (stayInGame)
                gameLogic.setBoardFromSettings();
        }

        System.out.println("Bye Bye :(");
    }

    private boolean loadXML() {
        boolean configurationLoaded = false;

        while (!configurationLoaded) {
            System.out.println("Please enter the path of an XML file with game settings: ");
            this.xmlPath = scanner.nextLine();
            try {
                gameLogic.loadSettingsFile(this.xmlPath);
                configurationLoaded = true;
            } catch (Exception e) {
                System.out.println("\nSorry to interrupt but the configuration file is invalid :(");
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {
                    break;
                }
            }
        }

        return configurationLoaded;
    }

    private boolean handleUserChoicePrimaryMenu(MenuChoice userChoice)
    {
        boolean continueGame = true;
        switch (userChoice){
            case LOADXML:
                if (!loadXML())
                    continueGame = false;
                else {
                    printBoard();
                    this.isValidXML = true;
                }
                break;
            case STARTGAME:
                if (!isValidXML) {
                    System.out.println("Please load a game file or a settings file, before starting a new game");
                }
                else {
                    continueGame = startGame();
                }
                break;
            case LOADGAME:
                continueGame = loadGameFromFile();
                if (continueGame) {
                    isValidXML = true;
                    playersSet = true;
                }
                break;
            case EXIT:
                continueGame = false;
        }

        return continueGame;
    }

    private boolean startGame()
    {
        if (!this.playersSet)
            choosePlayersType();
        boolean continuePlaying = playSingleRound();
        return continuePlaying;
    }

    private boolean playSingleRound()
    {
        boolean continuePlaying = true;
        MenuChoice userChoice = MenuChoice.INVALIDCHOICE;

        while((!gameLogic.getHasWinner()) && (!gameLogic.getIsBoardFull()) && MenuChoice.EXIT != userChoice) {
            printBoard();
            userChoice = gameMenu.showMenu();
            handleUserChoiceGameMenu(userChoice);
        }

        if (MenuChoice.EXIT == userChoice) {
            continuePlaying = false;
        }
        else {
            printBoard();
            if (gameLogic.getHasWinner()) {
                int idWinner = gameLogic.getIdOfCurrentPlayer() - 1;
                if (idWinner == 0)
                    idWinner = gameLogic.getNumberOfPlayers();
                winningMessage.printMessage(idWinner);
            }
            else if (gameLogic.getIsBoardFull()) {
                endGame.printMessage(0);
            }
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
            case UNDO: undoLastMove(); break;
            case SAVEGAME: saveGameToFile(); break;
        }
    }

    private void undoLastMove() {
        if (!gameLogic.undoLastMove())
            System.out.println("No Moves to undo");
    }

    private void showTurnsHistory()
    {
        List<Move> moves = gameLogic.getMovesHistory();

        if (moves.isEmpty()) {
            System.out.println("No moves were played");
        }
        else {
            for (Move m : moves) {
                System.out.println(String.format("%d: Player %d has dropped a disc to column %d\n", m.getMoveIndex(), m.getPlayerId(), m.getCol()));
            }
        }
    }

    private void showGameStats()
    {
        int playerAmmount = gameLogic.getNumberOfPlayers();

        System.out.println("========== Game statistics ==========");
        System.out.println(String.format("Length of winning sequence: " + gameLogic.getSequenceLength()));
        System.out.println("Current player: " + gameLogic.getIdOfCurrentPlayer());
        String time = gameLogic.timeFromBegining();
        System.out.println(String.format("Elapsed time: " + time));

        System.out.println("=== Players discs ===");
        for (int i = 0; i < playerAmmount; i++) {
            System.out.println(String.format("Player: %d Disc: %c", i + 1, playerDiscs[i+1]));
        }

        System.out.println("=== Player turns ===");
        for (int i = 0; i < playerAmmount; i++) {
            System.out.println(String.format("Player: %d Turns: %d", i + 1, gameLogic.playerTurns(i+1)));
        }
    }

    private void playTurn()
    {
        int col = 0;

        while(true) {
            if (gameLogic.getTypeOfCurrentPlayer() == PlayersTypes.HUMAN) { col = getUserInputCol(); }

            if (!gameLogic.play(col)) {
                System.out.println("Illegal move. please try again.");
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
        String userChoice;
        int initializedPlayers = 0;

        System.out.println("Before the game begins, please enter the types of the players! :)");

        while (initializedPlayers < playersAmount) {
            System.out.println();
            System.out.println("Please type h for human player and r for robotic player ");
            System.out.print(String.format("Please type your choice for player %d: ",  (initializedPlayers + 1)));
            userChoice = scanner.nextLine();

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
        this.playersSet = true;
    }

    private void saveGameToFile()
    {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("ddMMYY_HHmm");
        String time = localDateFormat.format(new Date());

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("N-in-a-Row_" + time + ".nar"))) {
            out.writeObject(this.gameLogic);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("Failed to save game to file");
        }
    }

    private boolean loadGameFromFile()
    {
        boolean continuePlaying = true;

        System.out.println("Please enter the name of the game file you wish to load");
        String gameFile = scanner.nextLine();
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(gameFile))) {
            this.gameLogic = (Game)in.readObject();
            continuePlaying = playSingleRound();
        }
        catch (Exception e) {
            System.out.println("Failed to load game from file");
        }
        finally {
            return continuePlaying;
        }
    }

    private void printLineSeparator(int cols)
    {
        for (int i = 0; i < (int)(((13 + ((cols - 1) * 6)) / 2.0) + 0.5); i++) { System.out.print("- "); }
        System.out.println();
    }

    private void printTopScales(int cols)
    {

        System.out.print("      ");
        for (int i = 0; i < cols; i++) {
            if (0 == ((i + 1) / 10)) { System.out.print(String.format("|  %d  ", i+1)); }
            else                     { System.out.print(String.format("| %d  ", i+1)); }
        }
        System.out.println("|");

        printLineSeparator(cols);
        System.out.println("\n");
    }

    private void printBoard()
    {
        char[][] board = gameLogic.boardReadyToPrint();
        int cols = gameLogic.getCols();
        int rows = board.length;
        System.out.println("\n");

        for(int row = 0; row < rows; row++ )
        {
            if (((row + 1)/10) == 0) { System.out.print(String.format("   %d  ", row + 1)); }
            else                     { System.out.print(String.format("  %d  ", row + 1)); }

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
        UI ui = new UI();
        ui.playGame();
    }
}
