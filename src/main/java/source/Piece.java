package source;

import java.util.ArrayList;
import java.util.Arrays;

public class Piece {
    private int[][] layout;
    private String name;
    /* Array list to store all the block names according to the assignment naming */
    private static final ArrayList<String> piecesNames = new ArrayList<String>(Arrays.asList("I1", "I2",
            "I3", "I4", "I5", "V3", "L4", "Z4", "O4", "L5", "T5", "V5", "N", "Z5", "T4", "P", "W", "U", "F", "X", "Y"));

    public Piece(int[][] layout) {
        this.layout = layout;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /* Function to get the piece by the name */
    public static Piece getPieceByName(String name, ArrayList<Piece> pieces) throws PieceNotFoundException {
        for (Piece piece : pieces) {
            if (piece.getName().equals(name)) {
                return piece;
            }
        }
        throw new PieceNotFoundException("Player does not have piece " + name);
    }

    /* Function to get the pieces index by the name */
    public static int getIndexByName(String s) {
        return piecesNames.indexOf(s);
    }

    /* Stores all the blocks */
    public static ArrayList<Piece> initializePieces() {
        Piece[] pieces = new Piece[21];

        pieces[0] = new Piece(new int[][]{
                {0}
        });
        pieces[1] = new Piece(new int[][]{
                {1},
                {0}
        });
        pieces[2] = new Piece(new int[][]{
                {2},
                {1},
                {0}
        });
        pieces[3] = new Piece(new int[][]{
                {3},
                {2},
                {1},
                {0}
        });
        pieces[4] = new Piece(new int[][]{
                {4},
                {3},
                {2},
                {1},
                {0}
        });
        pieces[5] = new Piece(new int[][]{
                {1, -1},
                {0, 2}
        });
        pieces[6] = new Piece(new int[][]{
                {2, -1},
                {1, -1},
                {0, 3}
        });
        pieces[7] = new Piece(new int[][]{
                {-1, 2, 3},
                {1, 0, -1},
        });
        pieces[8] = new Piece(new int[][]{
                {1, 2},
                {0, 3},
        });
        pieces[9] = new Piece(new int[][]{
                {1, -1, -1, -1},
                {0, 2, 3, 4},
        });
        pieces[10] = new Piece(new int[][]{
                {-1, 2, -1},
                {-1, 1, -1},
                {4, 0, 3},
        });
        pieces[11] = new Piece(new int[][]{
                {2, -1, -1},
                {1, -1, -1},
                {0, 3, 4},
        });
        pieces[12] = new Piece(new int[][]{
                {-1, 0, 1, 2},
                {4, 3, -1, -1},
        });
        pieces[13] = new Piece(new int[][]{
                {-1, -1, 2},
                {3, 0, 1},
                {4, -1, -1},
        });
        pieces[14] = new Piece(new int[][]{
                {-1, 1, -1},
                {3, 0, 2},
        });
        pieces[15] = new Piece(new int[][]{
                {0, 1},
                {3, 2},
                {4, -1},
        });
        pieces[16] = new Piece(new int[][]{
                {-1, 1, 2},
                {3, 0, -1},
                {4, -1, -1},
        });
        pieces[17] = new Piece(new int[][]{
                {1, 2},
                {0, -1},
                {3, 4},
        });
        pieces[18] = new Piece(new int[][]{
                {-1, 1, 2},
                {4, 0, -1},
                {-1, 3, -1},
        });
        pieces[19] = new Piece(new int[][]{
                {-1, 1, -1},
                {4, 0, 2},
                {-1, 3, -1},
        });
        pieces[20] = new Piece(new int[][]{
                {-1, 1, -1, -1},
                {4, 0, 2, 3},
        });
        //assigns the name from the predefined list
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setName(piecesNames.get(i));
        }
        return new ArrayList<Piece>(Arrays.asList(pieces));
    }

    /* gets the block array */
    public int[][] getLayout() {
        return layout;
    }

    public int[][] getFlippedLayout() {
        int[][] flipped = new int[layout.length][layout[0].length];
        for(int i = 0;i<layout.length;i++) {
            for(int j=0;j<layout[0].length;j++) {
                flipped[i][j] = layout[layout.length-1-i][j];
            } 
        }
        return flipped;
    }

    //rotates the piece by an angle
    public void rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if (degrees % 90 != 0) {
            throw new IllegalArgumentException("valid rotations are 0, 90, 180 or 270");
        }
        int times = degrees / 90;
        for (int t = 0; t < times; t++) {      //ie 180 = 90 * 2 duh
            //this is a 90 deg rotation 
            int[][] newLayout = new int[layout[0].length][layout.length];
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    newLayout[j][layout.length - i - 1] = layout[i][j];  //tranposition and the y axis is reversed
                }
            }
            layout = newLayout;
        }
    }

    // flips the piece along an axis
    public void flip(boolean horizontal, boolean vertical) {
        int[][] newLayout = new int[layout.length][layout[0].length];      //all reflections will be the same size
        if (horizontal) {   //redundant in text but maybe usefull when using ui
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    newLayout[layout.length - i - 1][j] = layout[i][j]; //the y values are reversed
                }
            }
            layout = newLayout;
        }
        if (vertical) {
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    newLayout[i][layout[i].length - j - 1] = layout[i][j];  //the x values are reversed
                }
            }
            layout = newLayout;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {
        ArrayList<Piece> pieces = initializePieces();
        for (Piece piece : pieces) {
            System.out.println(piece);
            for (int i = 0; i < piece.layout.length; i++) {
                for (int j = 0; j < piece.layout[i].length; j++) {
                    System.out.print(piece.layout[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    public void display() {
        for (int[] x : this.getLayout())
        {
            for (int y : x) {
                if (y != -1) {
                    System.out.print("*" + " ");
                }
                else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}