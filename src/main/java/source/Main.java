/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/
package source;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import gui.UI;
import gui.control.GameControl;
import gui.graphical.BlokusDuoGame;
import gui.graphical.GUI;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static Model model;
    private static Viewer viewer;
    private static Controller controller;
    public static void main(String[] args) throws IOException {
        /*Checks if there was a command line inputted, if not then it will random choose a player to start*/
        if(Arrays.asList(args).contains("-X")) {
            model = new Model("-X");
        } else if(Arrays.asList(args).contains("-O")) {
            model = new Model("-O");
        } else {
            Random random = new Random();
            model = new Model("-" + Player.getPlayerColours()[random.nextInt(Player.getPlayerColours().length)]);
        }

        if(Arrays.asList(args).contains("-gui")) {
        // if(1 == 1) { 
            startGUI();
        } else {
            startTextUI(false);
        }
    }
    //the main loop of the game
    public static void loop(boolean isGUI) {
        while(true) {
            controller.getInput();
            model.updateText();
            viewer.display();
        }
    }


    public static void startGUI() throws IOException {
        System.out.println("Starting graphical interface");
        UI ui;
        GameControl gameControl;
        Thread gameControlThread;

        ui = new GUI();
        model.setBoard(new Board());
        gameControl = new GameControl(ui);
        gameControlThread = new Thread(gameControl);
        gameControlThread.start();

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Application(new BlokusDuoGame(gameControlThread,ui), config);
        //Main.startTextUI(true);
    }

    public static void startTextUI(boolean isGUI) {
        System.out.println("Starting text based interface");
        viewer = new Viewer(model);
        controller = Controller.createInstance(model);
        loop(isGUI);
    }

    public static Model getModel() {
        return model;
    }
}