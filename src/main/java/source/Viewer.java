package source;

/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/
public class Viewer {
    private Model model;

    public Viewer(Model model) {
        this.model = model;

    }
    //displays the current model depending on what phase, and asks questions for the controller to prompt
    public void display() {
        switch(model.getPhase()) {
            case OFF:
                break;
            case SETUP:
                System.out.println("~~~~~~~~~~WELCOME TO BLOCKUS DUO~~~~~~~~~~");
                System.out.println("~~~~Lets start by stating a few rules!~~~~");
                System.out.println();
                System.out.println("1) The objective of the game is that each player have to fit as many of their 21 pieces on the board as possible.");
                System.out.println("2) The players will receive a color X or O and will play the game following the basic rules of Blockus Duo.");
                System.out.println("3) The game ends when both players are blocked from laying down any" +
                        "more pieces or when a player has placed all of their pieces on the board.");
                System.out.println("4) Finally, the scores are tallied, and the player with the highest score is the winner.");
                System.out.println();
                System.out.println("~~~~~~~~~~~Now you're all set!!~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~~~~~~~ENJOY~~~~~~~~~~~~~~~~~~~");
                System.out.println();
                break;
            case GETTING_PLAYER_NAMES:
                System.out.print("Please enter the name of Player " + model.getCurrentPlayerNumber() + ": ");
                break;
            case GETTING_PIECE:
                model.getBoard().displayBoard();
                System.out.println("Player " + model.getCurrentPlayer().getName() + ",\nYou have these pieces: ");
                System.out.println(model.getCurrentPlayer().getPieces().toString());
                System.out.print("What piece would you like to place? ");
                break;
            case GETTING_COMMANDS:
                model.getCurrentMove().getPiece().display();
                System.out.println("PLease enter 'r' to rotate, 'f' to flip and 'p' to place the piece on the board:");
                break;
            case GETTING_COORDINATES:
                System.out.println("Please enter the x and y coordinates on the board:");
                break;
            case END:
                System.out.println("Gameover!!... The final board was:");
                model.getBoard().displayBoard();
                System.out.println();
                System.out.println("~~~~~~~~~~~Thank you for Playing~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~~~~~~~GOODBYE~~~~~~~~~~~~~~~~~~~");
                System.exit(0);
        }

    }
}
