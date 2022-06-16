package source;/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/

import java.util.Arrays;

public class Board {
    public static final char NO_BLOCK = '.';
    public static final char START_BLOCK = '*';
    public static final int BOARD_SIZE = 14;
    private char[][] board;
    public static final int PIECE_THRESHOLD = 8;       //when to start checking if moves are possible, i feel like 5 is pretty fair but may change later if it can happen earlier in the game

    /* Constructor for board object */
    public Board() {
        this.board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    public char[][] getBoard() {
        return board;
    }

    /* Function to fill in the starting positions and empty positions on the board object */
    public void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i == 9 && j == 4 || i == 4 && j == 9) {
                    this.board[i][j] = START_BLOCK;
                } else {
                    this.board[i][j] = NO_BLOCK;
                }
            }
        }
    }


    /* Function to display the state of the board object */
    public void displayBoard() {
        /* Prints the column numbering of the board and the entire board */
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%-3d", BOARD_SIZE - i - 1);
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%-3c", this.board[BOARD_SIZE - i - 1][j]);   //internal board is flipped compared to outside, this keeps it consistent
            }
            System.out.print("\n");
        }
        /* Prints the row numbering of the board */
        System.out.printf("%-3s", "");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%-3d", i);
        }
        System.out.print("\n\n");
    }

    /* Function to get the maximum piece number in the pieceType */
    public int getMax(int[][] pieceTypeArray) {
        int max = pieceTypeArray[0][0];
        for (int i = 0; i < pieceTypeArray.length; i++) {
            for (int j = 0; j < pieceTypeArray[0].length; j++) {
                if (max < pieceTypeArray[i][j]) {
                    max = pieceTypeArray[i][j];
                }
            }
        }
        return max;
    }

    public void placePieceFake(Move move) throws IllegalMoveException{      //doesnt affect the real board
        placePiece(move, false);
    }

    public void placePiece(Move move) throws IllegalMoveException {         //does affect the real board
        placePiece(move, true);
    }

    /* Function to fill the board with the piece at the given x , y positions */
    public void placePiece(Move move, boolean permanent) throws IllegalMoveException {
        Player player = move.getPlayer();
        Piece pieceType = move.getPiece();
        int x = move.getX();
        int y = move.getY();
        pieceType.flip(true, false);    //internal board is flipped compared to outside, this keeps it consistent
        //make a temporary board so we can see if it fits the rules
        char[][] tempBoard = new char[BOARD_SIZE][BOARD_SIZE];



        for (int i = 0; i < board.length; i++) {
            tempBoard[i] = Arrays.copyOf(board[i], board[i].length);
        }


        int centreX = 0;
        int centreY = 0;
        // Variable to store the array instead of calling .getLayout function everytime
        int[][] pieceTypeArray = pieceType.getLayout();
        int max = getMax(pieceTypeArray);
        boolean touchingCorner = false;
        //Loops until all the number of pieces are reached and placed
        for (int maxCounter = 0; maxCounter < max + 1; maxCounter++) {
            //Loops through each element of pieceTypeArray
            for (int i = 0; i < pieceTypeArray.length; i++) {
                for (int j = 0; j < pieceTypeArray[0].length; j++) {
                    //Used to find the central position of the piece(always 0)
                    if (pieceTypeArray[i][j] == 0) {
                        centreX = j;
                        centreY = i;
                    }
                    //Used to find the consecutive pieces on the block and places it
                    if (pieceTypeArray[i][j] == maxCounter) {
                        int rx = x - (centreX - j);
                        int ry = y - (centreY - i);
                        if(rx < 0 || rx >= BOARD_SIZE || ry < 0 || ry >= BOARD_SIZE) {
                            throw new IllegalMoveException("Piece cannot be off the side of the board");
                        }
                        //x - (centreX - i) will find where it gets placed in relation to the central position of piece
                        if(tempBoard[ry][rx] == START_BLOCK || tempBoard[ry][rx] == NO_BLOCK) {

                            if(rx+1 <= BOARD_SIZE-1) {
                                if(board[ry][rx+1] == player.getPlayerBlockType()) {
                                    throw new IllegalMoveException("Touching right edge");
                                }
                                if(ry+1 <= BOARD_SIZE-1) {
                                    if(board[ry+1][rx+1] == player.getPlayerBlockType()) {
                                        touchingCorner = true;
                                    }
                                }
                                if(ry-1 >= 0) {
                                    if(board[ry-1][rx+1] == player.getPlayerBlockType()) {
                                        touchingCorner = true;
                                    }
                                }
                            }
                            if(ry+1 <= BOARD_SIZE-1) {
                                if(board[ry+1][rx] == player.getPlayerBlockType()) {
                                    throw new IllegalMoveException("Touching bottom edge");
                                }
                            }
                            if(rx-1 >= 0) {
                                if(board[ry][rx-1] == player.getPlayerBlockType()) {
                                    throw new IllegalMoveException("Touching left edge");
                                }
                                if(ry+1 <= BOARD_SIZE-1) {
                                    if(board[ry+1][rx-1] == player.getPlayerBlockType()) {
                                        touchingCorner = true;
                                    }
                                }
                                if(ry-1 >= 0) {
                                    if(board[ry-1][rx-1] == player.getPlayerBlockType()) {
                                        touchingCorner = true;
                                    }
                                }
                            }
                            if(ry-1 >= 0) {
                                if(board[ry-1][rx] == player.getPlayerBlockType()) {
                                    throw new IllegalMoveException("Touching top edge");
                                }
                            }
                            tempBoard[ry][rx] = player.getPlayerBlockType();
                        } else {
                            throw new IllegalMoveException("ERROR: You cannot have overlapping pieces!");
                        }
                    }
                }
            }
        }
        //if piece wasnt on corresponding start point

        if ((player.getPlayerNumber() == 1 && tempBoard[9][4] == START_BLOCK) || (player.getPlayerNumber() == 2 && tempBoard[4][9] == START_BLOCK)) {
            throw new IllegalMoveException("Must be on starting position");
        }
        //only start checking for corner rule once first 2 turns are over
        if(!touchingCorner && board[9][4] != START_BLOCK && board[4][9] != START_BLOCK) {
            throw new IllegalMoveException("Piece must be touching a corner of another piece of the same type");
        }
        if(permanent) {
            board = tempBoard;
        }
    }

    //improved lones' version so its faster might switch to this style later
    public void placePieceOptimised(Piece pieceType, int x, int y, Player p) {
        //find 0 block
        int centreX = 0;
        int centreY = 0;
        for (int i = 0; i < pieceType.getLayout().length; i++) {
            for (int j = 0; j < pieceType.getLayout()[i].length; j++) {
                if (pieceType.getLayout()[i][j] == 0) {
                    board[x][y] = p.getPlayerBlockType();
                    centreX = j;
                    centreY = i;
                }
            }
        }
        //offset the piece rectangle by where the 0 is
        //draw all the non -1 blocks
        for (int a = x - centreX; a < x - centreX + pieceType.getLayout()[0].length; a++) {
            for (int b = y - centreY; b < y - centreY + pieceType.getLayout().length; b++) {
                if (pieceType.getLayout()[b - y + centreY][a - x + centreX] != -1) {
                    board[b][a] = p.getPlayerBlockType();
                }
            }
        }
    }

    public boolean canMakeMove(Player p) {
        if(p.getPieces().size() > PIECE_THRESHOLD) return true;
        boolean foundValid = false;
        Move m = new Move();
        m.setPlayer(p);
        for (Piece piece : p.getPieces()) {             //for each piece
            m.setPiece(piece);
            for(int i=0;i<BOARD_SIZE;i++) {
                m.setY(i);
                for(int j=0;j<BOARD_SIZE;j++) {         //try every coordinate
                    m.setX(j);
                    if(board[i][j] == NO_BLOCK) {       //that is empty
                        for(int r=0;r<360;r+=90) {      //with all possible rotation/flips
                            try {
                                placePieceFake(m);
                                foundValid = true;
                            } catch (IllegalMoveException e) {}
                            piece.flip(false, true);
                            try {
                                placePieceFake(m);
                                foundValid = true;
                            } catch (IllegalMoveException e) {}
                            piece.rotate(90);
                        }
                        if(foundValid) {
                            System.out.println("Available move!\nPiece: " + piece.getName() + " x: " + m.getX() + " y: " + m.getY());
                            return true;
                        };
                    }

                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Player player1 = new Player('O', 1);
            Player player2 = new Player('X', 2);
            Board x = new Board();
            x.displayBoard();
            Move move1 = new Move();
            move1.setPlayer(player1);
            move1.setPiece(Piece.getPieceByName("V5", player1.getPieces()));
            move1.setX(4);
            move1.setY(9);

            x.placePiece(move1);
            x.displayBoard();

            Move move2 = new Move();
            move2.setPlayer(player2);
            move2.setPiece(Piece.getPieceByName("V5", player2.getPieces()));
            move2.setX(9);
            move2.setY(4);

            x.placePiece(move2);
            x.displayBoard();

            move1.setPiece(Piece.getPieceByName("I5", player1.getPieces()));
            move1.getPiece().rotate(90);
            move1.setX(7);
            move1.setY(10);
            x.placePiece(move1);
            x.displayBoard();
        } catch (SchemeException e) {
            e.printStackTrace();
        }
    }
}
