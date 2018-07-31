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
            System.out.println("2) Show game statistics");
            System.out.println("3) Show history");
            System.out.println("4) Exit");

            while (true) {
                try {
                    userChoice = Integer.parseInt(ui.scanner.nextLine());
                    break;
                } catch (NumberFormatException nfe) {
                    System.out.println("Not a number. Try again: ");
                }
            }

            printAgain = (userChoice > 4 || userChoice < 1);
            if (printAgain) { System.out.println("Bad choice. Try again!"); }
        }

        switch(userChoice) {
            case 1: return MenuChoice.MAKETURN;
            case 2: return MenuChoice.GAMESTATS;
            case 3: return MenuChoice.HISTORY;
            case 4: return MenuChoice.EXIT;
        }

        return MenuChoice.INVALIDCHOICE;
    }
}
