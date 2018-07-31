/**
 * Created by user on 31/07/2018.
 */
public class FirstMenu implements Menu {

    @Override
    public MenuChoice showMenu() {
        System.out.println("Hello and welcome to out fabulous N-in-a-Row game!\n");
        System.out.println("Loading the configuration file...\n");
        return MenuChoice.INVALIDCHOICE;
    }
}
