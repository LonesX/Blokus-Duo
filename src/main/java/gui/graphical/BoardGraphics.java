package gui.graphical;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import source.Board;
import source.Main;
import source.Model;

import static source.Board.BOARD_SIZE;


public class BoardGraphics {
    int boardX;
    int boardY;
    int boardSize;
    int cellSize;
    char[][] board;
    private Model model = Main.getModel();
    private ShapeRenderer shapeRenderer;

    public BoardGraphics(int boardX, int boardY, int cellSize, int boardSize, char[][] board) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.boardSize = boardSize;
        this.board = board;
        cellSize = boardSize/BOARD_SIZE;
    }

    public void updateBoard(Board board){
            this.board = new char[BOARD_SIZE][BOARD_SIZE];
            board.initializeBoard();
    }

    public void drawBoard(int boardX, int boardY, int cellSize) {
        for (int y = 0; y < Board.BOARD_SIZE; y++) {
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                if(board[y][x] == model.getPlayer1().getPlayerBlockType()){
                    shapeRenderer.setColor(Color.GREEN);
                }else if (board[y][x] == model.getPlayer2().getPlayerBlockType()){
                    shapeRenderer.setColor(Color.RED);
                }else {
                    shapeRenderer.setColor(Color.CLEAR);
                }
                shapeRenderer.rect(boardX+(x * cellSize), boardY+(y * cellSize),cellSize, cellSize);
            }
        }
    }

    public int getBoardColumn(int x) {
        int result = -1;
        if ((boardX < x) && ((boardX + boardSize) > x)) {
            result = (int)((x - boardX) / cellSize);
        }
        return result;
    }

    public int getBoardRow(int y) {
        int result = -1;
        if ((boardY < y) && ((boardY + boardSize) > y)) {
            result = (int)((y - boardY) / cellSize);
        }
        return result;
    }

}
