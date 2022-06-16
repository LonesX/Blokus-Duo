package gui.graphical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import gui.UI;
import source.Player;

import java.awt.Toolkit;

import java.io.PrintStream;

public class BlokusDuoGame extends Game {
    public static int WIDTH;
    public static int HEIGHT;
    Thread gameControlThread;
    PrintStream uiStream;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Screen startScreen;
    Screen playScreen;
    Screen GameplayScreen;
    Screen EndScreen;

    public BlokusDuoGame(Thread gameControlThread, UI ui) {
        GUI gui = (GUI)ui;
        this.gameControlThread = gameControlThread;
        gui.setGame(this);
        this.uiStream = new PrintStream(gui.getPipedOutputStream());
    }

    public void create() {
        WIDTH = Gdx.graphics.getDisplayMode().width;
        HEIGHT = Gdx.graphics.getDisplayMode().height;
        camera = new OrthographicCamera(WIDTH,HEIGHT);
        camera.position.set(WIDTH  * 0.5f, HEIGHT * 0.5f, 0.0f);
        stage = new Stage(new FitViewport(WIDTH,HEIGHT,camera));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("BlokusDuoSkin.json"));
        Gdx.graphics.setWindowedMode(WIDTH - 200, HEIGHT - 100);
        startScreen = new InputScreen(this);
        playScreen = new StartScreen(this);
        GameplayScreen = new GameplayScreen(this);
        activateStartScreen();
        System.out.println("Width: " + WIDTH + "height: " + HEIGHT);
    }

    public void activateStartScreen() {
        setScreen(startScreen);
    }

    public void activatePlayScreen() {
        setScreen(playScreen);
    }

    public void activateGameplayScreen(){
        setScreen(GameplayScreen);
    }

    public void activateEndScreen(Player winner){
        EndScreen = new EndScreen(this, winner);
        setScreen(EndScreen);
    }

    public void postRunnable(Runnable r) {
        Gdx.app.postRunnable(r);
    }

    void showDialog(String text) {
        Dialog dialog = new Dialog("Attention", skin, "default");
        dialog.text(text);
        dialog.button("OK");
        dialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               activateGameplayScreen();
            };
        });
        dialog.getContentTable().pad(10);
        dialog.getButtonTable().pad(10);
        dialog.show(stage);
    }


    void showEndDialog(String text) {
        Dialog dialog2 = new Dialog("GAME OVER!", skin, "default");
        dialog2.text(text);
        dialog2.getContentTable().pad(10);
        dialog2.show(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (startScreen != null) startScreen.dispose();
        if (playScreen != null) playScreen.dispose();
        if (skin != null) skin.dispose();
        if (stage != null) stage.dispose();
        if (gameControlThread != null) gameControlThread.stop();
    }

    public void setGreeting(String greeting) {
        showDialog(greeting);
    }
}
