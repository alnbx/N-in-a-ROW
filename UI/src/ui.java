import java.util.Scanner;

/**
 * Created by user on 30/07/2018.
 */

public class ui
{
    String xmlPath;

    private Scanner scanner = new Scanner(System.in);

    private void showFirstTimeMenu(String xmlPath)
    {
        System.out.println("Hello and welcome to out fabulous N-in-a-Row game!\n");
        System.out.println("Loading the configuration file...\n");
        loadFirstXML(xmlPath);
        //choosePlayersTypes(); //from XML????
        showMainMenu();
    }

    private void loadFirstXML(String xmlPath)
    {
        do {
            if (!Engine.load(xmlPath)) {
                System.out.println("Sorry to interrupt but the configuration file is invalid");
                checkAndPrintWhyXMLInvalid();
                System.out.println("Please provide another XML or type exit if you wish to exit");
                xmlPath = scanner.nextLine();
                if (xmlPath.toLowerCase().equals("exit")) {System.out.println("ByeBye"); System.exit(0); }
            }
        } while (!Engine.configurationLoaded());
    }

    private void showMainMenu()
    {
        boolean printAgain = true;

        while (printAgain) {
            System.out.println("Please type the Number of action you wish to make:");
            System.out.println("1) Load new configuration XML");
            System.out.println("2) Start game");
            System.out.println("3) Exit");

            int userChoice = scanner.nextInt();
            printAgain = (userChoice > 3 || userChoice < 1);
            if (printAgain) { System.out.println("Bad choice. Try again!"); }
        }

        handleUserChoice(userChoice);
    }

    private void handleUserChoice(int userChoice)
    {
        switch (userChoice){
            case 1:
                String path = scanner.nextLine();
                Engine.load(path) ? System.out.println("New configuration loaded successfully") :
                                    System.out.println("Bad XML was loaded.\nLoaded last legal configuration");
                break;

            case 2:
                startGame();
                break;

            case 3:
                System.out.println("Bye Bye :(");
                System.exit(0);
        }
    }

    private void startGame()
    {
        choosePlayersType();
        playGame();
    }

    private void playGame()
    {
        while ((false == Engine.isHasWinner()) && (false == Engine.ifFull())) { showSecondaryMenu(); }

        Engine.isHasWinner ? printWinningMessage(Engine.getWinner()) : printEndGame();
    }

    private void printEndGame()
    {
        System.out.println("***************************************************************************************");
        System.out.println("******************** Game ended. The board is full and no one won. ********************");
        System.out.println("***************************************************************************************");
    }

    private void printWinningMessage(int winner)
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!! WE HAVE A WINNER !!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!! PLAYER:   " + winner + "  WON !!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try { Thread.sleep(1500); }
        catch (InterruptedException ignored) {}
        showMainMenu();
    }

    private void showSecondaryMenu()
    {
        int userChoice = 0;

        System.out.println("Please type the Number of action you wish to make:");


    }

    private void choosePlayersType()
    {
        int playersAmount = Engine.getNumberOfPlayers();
        int robotsCounter = 0;
        PlayersTypes[] playersType = new PlayersTypes[playersAmount];
        String userChoice;
        boolean onceAgain = true;

        System.out.println("Before the game begins, please enter the types of the players! :)");
        System.out.println("Please type h for human player and r for robotic player ");
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
                    playersType[i] = PlayersTypes.ROBOT;
                    robotsCounter++;
                } else {
                    playersType[i] = PlayersTypes.HUMAN;
                }
            }

            onceAgain = robotsCounter == playersAmount;
            if (onceAgain) { System.out.println("You chose all players to be robots and that is not legal. Please choose again"); }
        }

        for (int i = 0; i < playersAmount; i++) { Engine.setPlayerType(i, playersType[i]); }
    }
}
