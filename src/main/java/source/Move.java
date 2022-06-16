package source;/*this class stores the information about a move */

public class Move {
    private Player player;
    private Piece piece;
    private int x;
    private int y;

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "[Player = " + player.getName() + "], " +
        "[Piece = " + piece + "], " +
        "[x = " + x + "], " +
        "[y = " + y + "]";
    }
}
