package ui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import common.*;
import engine.GameFactory;
import engine.GameLogic;
import engine.Game;
import engine.Move;

/**
 * Created by user on 30/07/2018.
 */

public class UI {
    private String xmlPath;
    private String savedGamePath;
    private Menu primaryMenu_noRestart;
    private Menu gameMenu;
    private Menu primaryMenu_wRestart;
    private PrintMessages winningMessage;
    private PrintMessages endGame;
    private GameLogic gameLogic;
    private boolean isValidXML;
    private GameFactory gameFactory;

    public static Scanner scanner = new Scanner(System.in);
    public static final char[] playerDiscs = {' ', '@', '#', '$', '%', '&', '+', '~'};

    public UI() {
        this.xmlPath = "";
        this.savedGamePath = "";
        this.primaryMenu_noRestart = new PrimaryMenu_noRestart();
        this.primaryMenu_wRestart = new PrimaryMenu_wRestart();
        this.gameMenu = new GameMenu();
        this.winningMessage = new WinnerMessage();
        this.endGame = new EndGameMessage();
        this.isValidXML = false;
        this.gameFactory = new GameFactory();
    }

    public void playGame() {
        MenuChoice menuChoice = MenuChoice.INVALIDCHOICE;
        System.out.println("Hello and welcome to out fabulous N-in-a-Row game!\n");
        while (menuChoice != MenuChoice.EXIT) {
            if (!this.isValidXML) {
                while (menuChoice != MenuChoice.EXIT && !this.isValidXML) {
                    menuChoice = primaryMenu_noRestart.showMenu();
                    handleUserChoicePrimaryMenu_noRestart(menuChoice);
                }
            }
            else {
                boolean succeedLoading = false;
                while (menuChoice != MenuChoice.EXIT && !succeedLoading) {
                    menuChoice = primaryMenu_wRestart.showMenu();
                    succeedLoading = handleUserChoicePrimaryMenu_withRestart(menuChoice);
                }
            }

            if(menuChoice != MenuChoice.EXIT) {
                menuChoice = playSingleRound();
            }                                                                                                        
        }

        System.out.println("Bye Bye :(");
    }

    private boolean loadXML() {
        boolean configurationLoaded = false;

        System.out.println("Please enter the full path of an XML file with game settings: ");
        this.xmlPath = scanner.nextLine();
        try {
            gameLogic = gameFactory.getNewGame(xmlPath);
            configurationLoaded = true;
            this.isValidXML = true;

            this.gameLogic.setBoardFromSettings(true);
            printBoard(false);
            choosePlayersType();
            this.savedGamePath = "";
        } catch (Exception e) {
            System.out.println("\nSorry to interrupt but the configuration file is invalid :(");
        }
        finally {
            return configurationLoaded;
        }
    }

    private boolean handleUserChoicePrimaryMenu_withRestart(MenuChoice userChoice)
    {
        boolean succeededLoading = false;
        switch (userChoice){
            case LOADXML:
                succeededLoading = loadXML();
                break;
            case RESTARTGAME:
                succeededLoading = true;
                if (this.savedGamePath.equals(""))
                    this.gameLogic.setBoardFromSettings(false);
                else {
                    if (!readGameFromFile(this.savedGamePath))
                        System.out.println("Failed to reload game from file");
                }
                break;
            case LOADGAME:
                succeededLoading = loadGameFromFile();
                break;
            default:
                break;
        }

        return succeededLoading;
    }

    private boolean handleUserChoicePrimaryMenu_noRestart(MenuChoice userChoice)
    {
        boolean continueGame = true;
        switch (userChoice) {
            case LOADXML:
                continueGame = loadXML();
                break;
            case LOADGAME:
                continueGame = loadGameFromFile();
                break;
            default:
                break;
        }

        return continueGame;
    }

    private boolean loadGameFromFile()
    {
        boolean gameLoaded = false;

        System.out.println("Please enter the full path of the game file you wish to load: ");
        String gameFile = scanner.nextLine();
        if (readGameFromFile((gameFile)))
            gameLoaded = true;
        else {
            System.out.println("Failed to load game from file");
        }
        return gameLoaded;
    }

    private MenuChoice playSingleRound()
    {
        MenuChoice userChoice = MenuChoice.INVALIDCHOICE;

        while((!gameLogic.getHasWinner()) && (!gameLogic.getIsBoardFull()) && MenuChoice.EXIT != userChoice) {
            printBoard(true);
            userChoice = gameMenu.showMenu();
            handleUserChoiceGameMenu(userChoice);
        }

        if (MenuChoice.EXIT != userChoice) {
            printBoard(true);
            if (gameLogic.getHasWinner()) {
                Set<Integer> winners = gameLogic.getWinners();
                //int idWinner = gameLogic.getIdOfCurrentPlayer() - 1;
                //if (idWinner == 0)
                //    idWinner = gameLogic.getNumberOfInitializedPlayers();
                winningMessage.printMessage(winners, gameLogic.getNumberOfInitializedPlayers());
            }
            else if (gameLogic.getIsBoardFull()) {
                endGame.printMessage(new HashSet<Integer>(), gameLogic.getNumberOfInitializedPlayers());
            }
        }

        return userChoice;
    }

    private boolean readGameFromFile(String gameFile) {
        boolean readOK = true;

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(gameFile))) {
            this.gameLogic = (Game) in.readObject();
            this.isValidXML = true;
            this.savedGamePath = gameFile;
        } catch (Exception e) {
            readOK = false;
        }
        finally {
            return readOK;
        }
    }

    private void handleUserChoiceGameMenu(MenuChoice userChoice) {
        switch (userChoice) {
            case MAKETURN:
                playTurn();
                break;
            case HISTORY:
                showTurnsHistory();
                break;
            case UNDO:
                undoLastMove();
                break;
            case SAVEGAME:
                saveGameToFile();
                break;
        }
    }

    private void undoLastMove() {
        if (!gameLogic.undoLastMove())
            printMessageWithDelay("No Moves to undo");
    }

    private void showTurnsHistory() {
        List<Move> moves = gameLogic.getMovesHistory();

        if (moves.isEmpty()) {
            printMessageWithDelay("No moves were played");
        } else {
            System.out.println("\n");
            for (Move m : moves) {
                System.out.println(String.format("%d: Player %d --> Column %d", m.getMoveIndex() + 1, m.getPlayerId(), m.getCol() + 1));
            }
            printMessageWithDelay("");
        }
    }

    private void printMessageWithDelay(String msg) {
        System.out.println("\n" + msg);
        try {
            Thread.sleep(1800);
        } catch (InterruptedException ignored) { }
    }

    private void showGameStats() {
        int playerAmmount = gameLogic.getNumberOfInitializedPlayers();

        String gameStatsTitle = "Game Stats:";
        System.out.println("\n" + gameStatsTitle);
        System.out.println(repeatStr("-", gameStatsTitle.length()));
        System.out.println("Status: Active");
        System.out.println("Length of winning sequence: " + gameLogic.getSequenceLength());
        System.out.println("Current player: " + gameLogic.getIdOfCurrentPlayer());
        String time = gameLogic.timeFromBegining();
        System.out.println(String.format("Elapsed time: " + time));

        String playersStatsTitle = "Players Stats:";
        System.out.println("\n" + playersStatsTitle);
        System.out.println(repeatStr("-", playersStatsTitle.length()));
        for (int i = 0; i < playerAmmount; i++) {
            System.out.println(String.format("Player %d: Disc: %c, Turns played: %d",
                    i + 1, playerDiscs[i + 1], gameLogic.playerTurns(i + 1)));
        }
    }

    private String repeatStr(String str, int times){
        String repeated = "";
        for (int i = 0; i < times; ++i)
            repeated += str;

        return repeated;
    }

    private void playTurn() {
        int col = 0;

        while (true) {
            if (gameLogic.getTypeOfCurrentPlayer() == PlayerTypes.HUMAN) {
                col = getUserInputCol();
            }

            if (!gameLogic.play(col, false)) {
                System.out.println("Illegal move. please try again.");
                col = getUserInputCol();
            } else {
                break;
            }
        }
    }

    private int getUserInputCol() {
        int input = 0;

        while (true) {
            try {
                System.out.println("Please enter the col you wish to drop a disc: ");
                input = Integer.parseInt(UI.scanner.nextLine());

                if (input > gameLogic.getCols() + 1 || input < 1) {
                    System.out.println("Out of bound col. Please try again. ");
                    continue;
                } else {
                    break;
                }

            } catch (NumberFormatException nfe) {
                System.out.println("Not a number. Try again: ");
            }
        }

        return input;
    }

    private void choosePlayersType() {
        int playersAmount = gameLogic.getNumberOfPlayersToInitialized();
        int robotsCounter = 0;
        String userChoice;
        int initializedPlayers = 0;

        System.out.println("Before the game begins, please enter the types of the players! :)");

        while (initializedPlayers < playersAmount) {
            System.out.println();
            System.out.println("Please type h for human player and r for robotic player ");
            System.out.print(String.format("Please type your choice for player %d: ", (initializedPlayers + 1)));
            userChoice = scanner.nextLine();

            while (!(userChoice.toLowerCase().equals("r") || userChoice.toLowerCase().equals("h"))) {
                System.out.print("Oops, bad choice. Please try again:  ");
                userChoice = scanner.nextLine();
                System.out.println();
            }

            if (userChoice.toLowerCase().equals("r")) {
                if (robotsCounter == playersAmount) {
                    System.out.println("You chose all players to be robots and that is not legal. Please choose again");
                } else {
                    this.gameLogic.initPlayer(PlayerTypes.COMPUTER, initializedPlayers + 1, "Computer");
                    robotsCounter++;
                    initializedPlayers++;
                }
            } else {
                System.out.print("Please type player's name: ");
                String name = scanner.nextLine();
                this.gameLogic.initPlayer(PlayerTypes.HUMAN, initializedPlayers + 1, name);
                initializedPlayers++;
            }
        }
    }

    private void saveGameToFile() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("ddMMYY_HHmm");
        String time = localDateFormat.format(new Date());

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("N-in-a-Row_" + time + ".nar"))) {
            out.writeObject(this.gameLogic);
            out.flush();
            printMessageWithDelay("Game saved to file: " + "N-in-a-Row_" + time + ".nar");
        } catch (IOException e) {
            printMessageWithDelay("Failed to save game to file");
        }
    }

    private void printLineSeparator(int cols) {
        for (int i = 0; i < (int) (((13 + ((cols - 1) * 6)) / 2.0) + 0.5); i++) {
            System.out.print("- ");
        }
        System.out.println();
    }

    private void printTopScales(int cols) {

        System.out.print("      ");
        for (int i = 0; i < cols; i++) {
            if (0 == ((i + 1) / 10)) {
                System.out.print(String.format("|  %d  ", i + 1));
            } else {
                System.out.print(String.format("| %d  ", i + 1));
            }
        }
        System.out.println("|");

        printLineSeparator(cols);
    }

    private void printBoard(boolean printSpec) {
        char[][] board = gameLogic.boardReadyToPrint();
        int cols = gameLogic.getCols();
        int rows = board.length;
        System.out.println("\n");

        for (int row = 0; row < rows; row++) {
            if (((row + 1) / 10) == 0) {
                System.out.print(String.format("   %d  ", row + 1));
            } else {
                System.out.print(String.format("  %d  ", row + 1));
            }

            for (int col = 0; col < cols; col++) {
                int i = board[row][col] - '0';
                System.out.print(String.format("|  %c  ", playerDiscs[i]));
            }
            System.out.println("|");
            printLineSeparator(cols);
        }

        printTopScales(cols);

        if (printSpec) {
            showGameStats();
        }

        System.out.println();
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.playGame();
    }
}