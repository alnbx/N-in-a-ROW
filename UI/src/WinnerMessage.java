/**
 * Created by user on 31/07/2018.
 */
public class WinnerMessage implements PrintMessages {
    @Override
    public void printMessage(int player)
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!! WE HAVE A WINNER !!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!! PLAYER:   " + player + "  WON !!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try { Thread.sleep(1500); }
        catch (InterruptedException ignored) {}
    }
}
