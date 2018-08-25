package ui;

/**
 * Created by user on 31/07/2018.
 */
public class PrimaryMenu_noRestart implements Menu {

    @Override
    public MenuChoice showMenu() {
        boolean printAgain = true;
        int userChoice = 0;

        while (printAgain) {
            System.out.println("Please type the Number of action you wish to make:");
            System.out.println("1) Load new configuration XML");
            System.out.println("2) Load game from file");
            System.out.println("3) Exit");

            while (true) {
                try {
                    //userChoice = Integer.parseInt(UI.scanner.nextLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Not a number. Try again: ");
                }
            }

            printAgain = (userChoice > 3 || userChoice < 1);
            if (printAgain) { System.out.println("Bad choice. Try again!"); }
        }

        switch(userChoice) {
            case 1: return MenuChoice.LOADXML;
            case 2: return MenuChoice.LOADGAME;
            case 3: return MenuChoice.EXIT;
        }

        return MenuChoice.INVALIDCHOICE;
    }
}
