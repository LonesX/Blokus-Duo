/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/
//singleton class, i.e. only one controller can exist
package source;

public class Controller {
    private static Controller instance;
    private Menu menu;
    private Model model;

    private Controller(Model model) {
        this.model = model;
        menu = new Menu();
    }
    //gets the input if the phase asks for it
    public void getInput() {
        switch(model.getPhase()) {
            case GETTING_COORDINATES:
                menu.getInputLine();        //coords are two values so read as rest of line
                break;
            case GETTING_COMMANDS:
            case GETTING_PIECE:
            case GETTING_PLAYER_NAMES:
                menu.getInput();            //reads the next input, stops at spaces
                break;
            case OFF:
            case SETUP:
            case END:
            default:
                break;
        }
    }

    public static Controller createInstance(Model model) {
        instance = new Controller(model);
        return instance;
    }

    public static Controller getInstance() {
        return instance;
    }

    public Menu getMenu() {
        return menu;
    }
}