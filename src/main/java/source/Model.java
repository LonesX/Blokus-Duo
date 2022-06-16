package source;/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/

public class Model {
    private Player playerA;
    private Player playerB;
    private Player currentPlayer;
    private Board board;
    private Phase phase = Phase.OFF;
    private Move currentMove;
    private boolean lastPlayerCouldntMakeMove = false;

    public Model (String firstPlayer) {

        if(firstPlayer.equals("-X")) {
            playerA = new Player('X', 1);
            playerB = new Player('O', 2);
            currentPlayer = playerA;

        }
        else if (firstPlayer.equals("-O")){
            playerA = new Player('X', 2);
            playerB = new Player('O', 1);
            currentPlayer = playerB;
        } else throw new IllegalArgumentException("Unknown command " + firstPlayer);
    }

    public enum Phase {
        OFF,
        SETUP,
        GETTING_PLAYER_NAMES,
        GETTING_PIECE,
        GETTING_COMMANDS,
        GETTING_COORDINATES,
        END
    }


    //updates the relevant info with info from the controller and changes phase when necessary
    public void updateText() {
        Menu menu = Controller.getInstance().getMenu();
        switch(phase) {
            case OFF:
                phase = Phase.SETUP;
                break;
            case SETUP:
                board = new Board();
                phase = Phase.GETTING_PLAYER_NAMES;
                break;
            case GETTING_PLAYER_NAMES:
                if(!hasName(currentPlayer)) {
                    currentPlayer.setName(menu.getInputString());
                    switchTurns();
                }
                if(hasName(currentPlayer)) {
                    phase = Phase.GETTING_PIECE;
                }
                break;
            case GETTING_PIECE:
                currentMove = new Move();
                try {
                    String pieceName = menu.getInputString();
                    Piece piece = Piece.getPieceByName(pieceName, currentPlayer.getPieces());
                    currentMove.setPiece(piece);
                    currentMove.setPlayer(currentPlayer);
                } catch (PieceNotFoundException e) {
                    //Error if player doesnt have desired piece
                    System.out.println(e.getMessage());
                    break;
                }
                phase = Phase.GETTING_COMMANDS;
                break;
            case GETTING_COMMANDS:
                String command = menu.getInputString();
                switch(command) {
                    case "r":
                        currentMove.getPiece().rotate(90);
                        break;
                    case "f":
                        currentMove.getPiece().flip(false, true);
                        break;
                    case "p":
                        phase = Phase.GETTING_COORDINATES;
                        break;
                    default:
                        System.out.println("Unknown Command");
                        break;
                }
                break;
            case GETTING_COORDINATES:
                try {
                    int[] coords = menu.getCoords();
                    currentMove.setX(coords[0]);
                    currentMove.setY(coords[1]);
                    board.placePiece(currentMove);
                    currentPlayer.removeLocation(currentMove.getPiece());
                    currentPlayer.removePiece(currentMove.getPiece().getName());
                    
                } catch (UnknownCoordinatesException e) {
                    //If the coords couldnt be read error
                    System.out.println(e.getMessage());
                    break;
                } catch (IllegalMoveException e) {
                    //If the move was Illegal error
                    phase = Phase.GETTING_PIECE;
                    System.out.println(e.getMessage());
                    break;
                }
                switchTurns();
                phase = Phase.GETTING_PIECE;
                if (!board.canMakeMove(currentPlayer)) {
                    System.out.println("Player " + currentPlayer.getName() + ", has no valid moves");
                    if(lastPlayerCouldntMakeMove) {
                        switchTurns();
                        System.out.println("Player " + currentPlayer.getName() + ", has no valid moves");
                        System.out.println("No valid moves left");
                        phase = Phase.END;
                        break;
                    }
                    lastPlayerCouldntMakeMove = true;
                    switchTurns();
                    break;
                };
                lastPlayerCouldntMakeMove = false;
                break;
            case END:
                break;
            default:
                break;
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }


    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return playerA;
    }

    public Player getPlayer2() {
        return playerB;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Player getOtherPlayer(){
        Player temp;
        switchTurns();
        temp = currentPlayer;
        switchTurns();
        return temp;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayer.equals(playerA) ? 1 : 2;
    }

    public boolean hasName(Player p) {
        return p.getName() != null;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean lastPlayerCouldntMakeMove() {
        return lastPlayerCouldntMakeMove;
    }

    public void setLastPlayerCouldntMakeMove(boolean lastPlayerCouldntMakeMove) {
        this.lastPlayerCouldntMakeMove = lastPlayerCouldntMakeMove;
    }

    public void switchTurns() {
        if (currentPlayer == playerA) {
            currentPlayer = playerB;
        } else {
            currentPlayer = playerA;
        }
    }

    public Move getCurrentMove() {
        return currentMove;
    }
}
