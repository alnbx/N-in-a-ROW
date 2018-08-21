package ui;

import java.util.Set;

/**
 * Created by user on 31/07/2018.
 */
public class WinnerMessage implements PrintMessages {
    @Override
    public void printMessage(Set<Integer> winners, int amountOfPlayers)
    {
        int player = amountOfPlayers - 1;

        if (winners.isEmpty()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!! WE HAVE A WINNER !!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!! PLAYER:   " + player + "  WON !!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!! WE HAVE A WINNER !!!!!!!!!!!!!!!!!!!!");
            System.out.print("!!!!!!!!!!!!!!!!!!!! PLAYER:   ");
            for (Integer i : winners) { System.out.print(i + " "); };
            System.out.println("+ WON !!!!!!!!!!!!!!!!!!!!\");");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        try { Thread.sleep(1500); }
        catch (InterruptedException ignored) {}
    }
}
