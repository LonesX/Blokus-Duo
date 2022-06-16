package gui.graphical;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import source.*;

import java.util.ArrayList;

    public class GameplayScreen extends ScreenAdapter {
    private Model model = Main.getModel();
    private BlokusDuoGame blokusDuoGame;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Table main;
    private Skin skin;
    private Sound error = Gdx.audio.newSound(Gdx.files.internal("error_sfx.mp3"));
    private Sound validMove = Gdx.audio.newSound(Gdx.files.internal("validMV_sfx.mp3"));



    private final int PREVIEW_SIZE = 20;
    private final int LARGE_SIZE = 60;
    private final int BOARD_X = BlokusDuoGame.WIDTH/2 - (LARGE_SIZE * Board.BOARD_SIZE/2);
    private final int OFFSET_Y = 100;
    private final int PIECE_1_X = 100;
    private final int PIECE_2_X = (BlokusDuoGame.WIDTH - PIECE_1_X - (3 * 6 * PREVIEW_SIZE));
    private int startMSG = 0;

        private Piece currentlyMoving = null;
    private int[] prevMouseLocation = {0, 0};

    public GameplayScreen(BlokusDuoGame blokusDuoGame) {
        this.blokusDuoGame = blokusDuoGame;
        this.stage = blokusDuoGame.stage;
        this.skin = blokusDuoGame.skin;

        create();

        main = new Table();
        main.setFillParent(true);
        main.top();
    }

    public void show() {
        super.show();
        stage.addActor(main);
        addListeners();
    }

    public void hide() {
        super.hide();
        stage.clear();
    }

    public void create() {
        setDefaultPieceLocations(PIECE_1_X, OFFSET_Y, model.getPlayer1());
        setDefaultPieceLocations(PIECE_2_X, OFFSET_Y, model.getPlayer2());
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

    }

    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        stage.act();
        stage.draw();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(blokusDuoGame.camera.combined);
        drawBoard(BOARD_X, OFFSET_Y, LARGE_SIZE);
        drawPieceBoxes(PIECE_1_X, OFFSET_Y);
        drawPieceBoxes(PIECE_2_X, OFFSET_Y);
        shapeRenderer.setColor(Color.GREEN);
        drawPieces(model.getPlayer1());
        shapeRenderer.setColor(Color.RED);
        drawPieces(model.getPlayer2());
        shapeRenderer.setColor(Color.CORAL);
        Vector3 mouseLoc = unproject(new Vector3(prevMouseLocation[0], prevMouseLocation[1], 0));
        shapeRenderer.circle(mouseLoc.x, mouseLoc.y, 5);
        shapeRenderer.end();
    }

    public void drawPieces(Player player) {
        for (int n = 0; n < player.getPieces().size(); n++) {
            Piece p = player.getPieces().get(n);
            int SIZE = PREVIEW_SIZE;
            if(p.equals(currentlyMoving)) SIZE = LARGE_SIZE;
            int[][] layout = p.getFlippedLayout();
            int[] center = {0, 0};
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] == 0) {
                        center = new int[]{j, i};
                    }
                }
            }
            int startX = player.getPieceLocations().get(n)[0] - SIZE * center[0] - SIZE / 2;
            int startY = player.getPieceLocations().get(n)[1] - SIZE * center[1] - SIZE / 2;
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] != -1) {
                        shapeRenderer.rect(startX + (SIZE * j), startY + (SIZE * i), SIZE, SIZE);
                    }
                }
            }
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] == 0) {
                        Color temp = new Color(shapeRenderer.getColor());
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.circle(startX + (SIZE * j) + SIZE / 2, startY + (SIZE * i) + SIZE / 2, SIZE / 4);
                        shapeRenderer.setColor(temp);
                    }
                }
            }
        }
    }

    public void setDefaultPieceLocations(int x, int y, Player player) {
        int xOffset = x;
        int yOffset = y;
        int SIZE = PREVIEW_SIZE;
        for (int n = 0; n < player.getPieces().size(); n++) {
            Piece p = player.getPieces().get(n);
            int startX = xOffset + (SIZE * 6) * (n / 7);
            int startY = yOffset + (SIZE * 6) * (n % 7);

            int[][] layout = p.getFlippedLayout();
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] == 0) {
                        int[] location = player.getPieceLocations().get(n);
                        location[0] = startX + (SIZE * j) + SIZE / 2;
                        location[1] = startY + (SIZE * i) + SIZE / 2;
                    }
                }
            }
        }

    }

    public void drawBoard(int boardX, int boardY, int cellSize) {
        char[][] board = model.getBoard().getBoard();

        if(startMSG != 1){
            drawMessage(null);
            startMSG++;
        }

        for (int y = 0; y < Board.BOARD_SIZE; y++) {
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                if(board[y][x] == model.getPlayer1().getPlayerBlockType()){
                    shapeRenderer.setColor(Color.GREEN);
                }else if (board[y][x] == model.getPlayer2().getPlayerBlockType()){
                    shapeRenderer.setColor(Color.RED);
                }else if (board[y][x] == Board.START_BLOCK) {
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.rect(boardX+(x * cellSize), boardY+(y * cellSize),cellSize, cellSize);
                    shapeRenderer.setColor(Color.GRAY);
                    shapeRenderer.circle(boardX+(x * cellSize) + cellSize/2, boardY+(y * cellSize) + cellSize/2, 15);
                    continue;
                } else {
                    shapeRenderer.setColor(Color.WHITE);
                }
                shapeRenderer.rect(boardX+(x * cellSize), boardY+(y * cellSize),cellSize, cellSize);
            }
        }
        shapeRenderer.setColor(Color.BLACK);
        for (int y = 0; y <= Board.BOARD_SIZE; y++) {
            shapeRenderer.rectLine(boardX, boardY+(y * cellSize),boardX+(Board.BOARD_SIZE * cellSize), boardY+(y * cellSize), 1.3f);
        }
        for (int x = 0; x <= Board.BOARD_SIZE; x++) {
            shapeRenderer.rectLine(boardX+(x * cellSize), boardY,boardX+(x*cellSize), boardY+(Board.BOARD_SIZE * cellSize), 1.3f);
        }
    }
    
    public void drawPieceBoxes(int x, int y) {
        int xOffset = x;
        int yOffset = y;
        int SIZE = PREVIEW_SIZE;
        for (int n = 0; n < 21; n++) {
            int startX = xOffset + (SIZE * 6) * (n / 7);
            int startY = yOffset + (SIZE * 6) * (n % 7);

            Color temp = new Color(shapeRenderer.getColor());
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(startX, startY, SIZE * 5, SIZE * 5);
            //System.out.printf("x: %d, y: %d, size: %d\n",startX, startY, SIZE*5);
            shapeRenderer.setColor(temp);
        }

    }


    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }


    public void dispose() {
        super.dispose();
    }

    private Piece isMouseOnStartLocation(int x, int y) {
        Player player = model.getCurrentPlayer();
        ArrayList<Piece> pieces = player.getPieces();

        int xOffset = (model.getPlayer1()==player ? PIECE_1_X : PIECE_2_X);
        int yOffset = OFFSET_Y;
        int SIZE = PREVIEW_SIZE;

        for (int n = 0; n < pieces.size(); n++) {
            int trueN = Piece.getIndexByName(pieces.get(n).getName());
            
            int startX = xOffset + (SIZE * 6) * (trueN / 7);
            int startY = yOffset + (SIZE * 6) * (trueN % 7);

            if(insideSquare(x, y, startX, startY, SIZE * 5)) return pieces.get(n);
        }
        return null;
    }

    private Piece isMouseOnPiece(int x, int y) {
        Player player = model.getCurrentPlayer();
        ArrayList<Piece> pieces = player.getPieces();
        ArrayList<int[]> locations = player.getPieceLocations();
        int SIZE = PREVIEW_SIZE;
        for (int n = 0; n < pieces.size(); n++) {
            int[][] layout = pieces.get(n).getFlippedLayout();
            int[] center = {0, 0};
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] == 0) {
                        center = new int[]{j, i};
                    }
                }
            }
            int startX = locations.get(n)[0] - SIZE * center[0] - SIZE / 2;
            int startY = locations.get(n)[1] - SIZE * center[1] - SIZE / 2;
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[0].length; j++) {
                    if (layout[i][j] != -1) {
                        //System.out.printf("x: %d y:%d sx: %d sy: %d size: %d\n", x, y, startX + (SIZE * j), startY + (SIZE * i), SIZE);
                        if (insideSquare(x, y, startX + (SIZE * j), startY + (SIZE * i), SIZE)) {
                            return pieces.get(n);
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean makeMove(Piece p, int x, int y) {
        Move move = new Move();
        move.setPiece(p);
        move.setPlayer(model.getCurrentPlayer());

        int[] location = model.getCurrentPlayer().getPieceLocations().get(model.getCurrentPlayer().getPieces().indexOf(p));
        move.setX(getBoardColumn(location[0]));
        move.setY(getBoardRow(location[1]));
        System.out.println("oldx: " + location[0] + " oldy: " + location[1] + " moveX: " + move.getX() + " moveY: " + move.getY());
        try {
            model.getBoard().placePiece(move);
            model.getCurrentPlayer().removeLocation(move.getPiece());
            model.getCurrentPlayer().removePiece(move.getPiece().getName());
            validMove.play();
            return true;
        } catch (SchemeException e) {
            drawErrorMessage(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getBoardColumn(int x) {
        int result = -1;
        if ((BOARD_X < x) && (BOARD_X + (LARGE_SIZE * 14)) > x) {
            result = (int)((x - BOARD_X) / LARGE_SIZE);
        }
        return result;
    }

    public int getBoardRow(int y) {
        int result = -1;
        if ((OFFSET_Y < y) && (OFFSET_Y + (LARGE_SIZE * 14)) > y) {
            result = (int)((y - OFFSET_Y) / LARGE_SIZE);
        }
        return result;
    }

    private void resetPiece(Piece p, Player player) {
        int xOffset = (model.getPlayer1()==player ? PIECE_1_X : PIECE_2_X);
        int yOffset = OFFSET_Y;
        int SIZE = PREVIEW_SIZE;
        
        int n = Piece.getIndexByName(p.getName());
        
        int startX = xOffset + (SIZE * 6) * (n / 7);
        int startY = yOffset + (SIZE * 6) * (n % 7);
        p.flip(true, false);
        int[][] layout = p.getFlippedLayout();
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] == 0) {
                    int[] location = player.getPieceLocations().get(player.getPieces().indexOf(p));
                    location[0] = startX + (SIZE * j) + SIZE / 2;
                    location[1] = startY + (SIZE * i) + SIZE / 2;
                }
            }
        }
    }

    private boolean insideSquare(int x, int y, int sx, int sy, int size) {
        return (x > sx) && (x < sx + size) && (y > sy) && (y < sy + size);
    }

    private Vector3 unproject(Vector3 v) {
        return stage.getViewport().unproject(v);
    }

    private void addListeners() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Vector3 world = unproject(new Vector3(screenX, screenY, 0));
                    //System.out.println("World at: " + world);
                    //System.out.println("Screen at: " + new Vector3(screenX, screenY, 0));
                    currentlyMoving = isMouseOnStartLocation((int) world.x, (int) world.y);
                    //System.out.println(currentlyMoving);
                    prevMouseLocation[0] = screenX;
                    prevMouseLocation[1] = screenY;
                } else if (button == Input.Buttons.RIGHT) {
                    if (currentlyMoving != null) {
                        currentlyMoving.flip(false, true);
                    }
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT && currentlyMoving != null) {
                    System.out.println("Dropped Piece");
                    Vector3 world = unproject(new Vector3(screenX, screenY, 0));
                    boolean successful = makeMove(currentlyMoving, (int) world.x, (int) world.y);

                    if(successful) {
                        currentlyMoving = null;
                        model.switchTurns();
                        drawMessage(null);
                        //phase = Phase.GETTING_PIECE;
                        if (!model.getBoard().canMakeMove(model.getCurrentPlayer())) {
                            drawMessage("Player " + model.getCurrentPlayer().getName() + ", has no valid moves");
                            model.switchTurns();
                            if(!model.getBoard().canMakeMove(model.getCurrentPlayer())) {
                                Player winner;
                                if(model.getCurrentPlayer().getPieces().size() > model.getOtherPlayer().getPieces().size()) {
                                    winner = model.getOtherPlayer();
                                } else if (model.getCurrentPlayer().getPieces().size() < model.getOtherPlayer().getPieces().size()) {
                                    winner = model.getCurrentPlayer();
                                } else {
                                    winner = null;
                                }
                                drawMessage("Player " + model.getCurrentPlayer().getName() + ", has no valid moves");
                                drawMessage("No valid moves left");
                                //phase = Phase.END;
                                blokusDuoGame.activateEndScreen(winner);
                                return false;
                            }
                            return false;
                        };
                    } else {
                        resetPiece(currentlyMoving, model.getCurrentPlayer());
                        currentlyMoving = null;
                    }
                    System.out.println(model.getCurrentPlayer().getPieces().toString());
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (currentlyMoving != null) {
                    Vector3 world = unproject(new Vector3(screenX, screenY, 0));
                    Vector3 prevWorld = unproject(new Vector3(prevMouseLocation[0], prevMouseLocation[1], 0));
                    Vector3 change = new Vector3(world.sub(prevWorld));
                    world.add(prevWorld);
                    //System.out.println("world: " + world + " prevWorld: " + prevWorld + " change " + change);
                    int[] location = model.getCurrentPlayer().getPieceLocations().get(model.getCurrentPlayer().getPieces().indexOf(currentlyMoving));
                    location[0] += change.x;
                    location[1] += change.y;
                    prevMouseLocation[0] = screenX;
                    prevMouseLocation[1] = screenY;
                }
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (currentlyMoving == null) return false;
                if (amountY > 0) {
                    currentlyMoving.rotate(90);
                } else if (amountY < 0) {
                    currentlyMoving.rotate(-90);
                }
                return super.scrolled(amountX, amountY);
            }
        });
    }


    public void drawMessage(String str){
        if(str == null){
            main.clear();
            Label label = new Label(model.getCurrentPlayer().getName() + ", Please drag and drop your gamepiece onto the board! (You can rotate piece by scrolling)",skin,"font", Color.WHITE );
            main.add(label);
            main.row();
        }
        else{
            main.clear();
            Label label = new Label(str,skin,"font", Color.WHITE );
            main.add(label);
            main.row();
        }


    }

    public void drawErrorMessage(String str){
        main.clear();
        Label label = new Label(str,skin,"font", Color.RED );
        main.add(label);
        main.row();
        error.play();
    }




}
