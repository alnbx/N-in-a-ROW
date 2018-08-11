package ui;

/**
 * Created by user on 31/07/2018.
 */
public class GameMenu implements Menu {
    @Override
    public MenuChoice showMenu() {
        boolean printAgain = true;
        int userChoice = 0;

        while (printAgain) {
            System.out.println("Please type the Number of action you wish to make:");
            System.out.println("1) Make a move");
            System.out.println("2) Show history");
            System.out.println("3) Undo last move");
            System.out.println("4) Save game to file");
            System.out.println("5) Exit");

            while (true) {
                try {
                    userChoice = Integer.parseInt(UI.scanner.nextLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Not a number. Try again: ");
                }
            }

            printAgain = (userChoice > 5 || userChoice < 1);
            if (printAgain) { System.out.println("Bad choice. Try again!"); }
        }

        switch(userChoice) {
            case 1: return MenuChoice.MAKETURN;
            case 2: return MenuChoice.HISTORY;
            case 3: return MenuChoice.UNDO;
            case 4: return MenuChoice.SAVEGAME;
            case 5: return MenuChoice.EXIT;
        }

        return MenuChoice.INVALIDCHOICE;
    }
}
