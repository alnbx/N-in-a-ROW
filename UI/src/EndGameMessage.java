/**
 * Created by user on 31/07/2018.
 */
public class EndGameMessage implements PrintMessages {
    @Override
    public void printMessage(int player)
    {
        System.out.println("**********************************************************");
        System.out.println("********************** END OF GAME ***********************");
        System.out.println("******************** NO WINNER - TIE *********************");
        System.out.println("**********************************************************");
        try { Thread.sleep(1500); }
        catch (InterruptedException ignored) {}
    }
}
