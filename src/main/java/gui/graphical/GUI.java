package gui.graphical;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import gui.UI;

public class GUI implements UI {

    private PipedOutputStream pipedOutputStream;
    private PipedInputStream pipedInputStream;
    private BlokusDuoGame blokusDuoGame;
    private Scanner scanner;

    public GUI() throws IOException {
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream(pipedOutputStream);
        scanner = new Scanner(pipedInputStream);
    }

    void setGame(BlokusDuoGame blokusDuoGame) {
        this.blokusDuoGame = blokusDuoGame;
    }

    public PipedOutputStream getPipedOutputStream() {
        return pipedOutputStream;
    }

    public String getName() {
        return scanner.next();
    }

    public void displayGreeting(String greeting) {
        blokusDuoGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                blokusDuoGame.activatePlayScreen();
                blokusDuoGame.setGreeting(greeting);
            }
        });
    }
}
