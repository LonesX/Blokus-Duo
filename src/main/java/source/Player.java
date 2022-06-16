package source;

import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private ArrayList<Piece> pieces;
    private ArrayList<int[]> pieceLocations = new ArrayList<int[]>();
    private char playerBlockType;
    private int playerNumber;     
    private static final char[] playerColours = {'X','O'};

    public Player(char playerBlockType, int playerNumber) {
        pieces = Piece.initializePieces();
        pieces.forEach(p -> {pieceLocations.add(new int[2]);});
        score = 0;
        this.playerBlockType = playerBlockType;
        this.playerNumber = playerNumber;
    }



    public char getPlayerBlockType() {
        return playerBlockType;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public ArrayList<int[]> getPieceLocations() {
        return pieceLocations;
    }

    public boolean hasName(){
        return name != null;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerBlockType(char playerBlockType) {
        this.playerBlockType = playerBlockType;
    }


    public static char[] getPlayerColours() {
        return playerColours;
    }

    public void removeLocation(Piece p) {
        int i = pieces.indexOf(p);
        pieceLocations.remove(i);
    }

    public void removePiece(String s) {
        for (int i=0;i<pieces.size();i++) {
            if(pieces.get(i).getName().equals(s)) {
                pieces.remove(i);
            }
        }
    }
    public int getPlayerNumber() {
        return playerNumber;
    }
}