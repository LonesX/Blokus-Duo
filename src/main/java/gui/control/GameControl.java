package gui.control;


import com.badlogic.gdx.Gdx;

import gui.UI;
import source.Main;

public class GameControl implements Runnable {

    UI ui;

    public GameControl(UI ui) {
        this.ui = ui;
    }

    @Override
    public void run() {
        String[] name = new String[3];
        name[1] = ui.getName();
        name[2] = ui.getName();
        if(Main.getModel().getCurrentPlayerNumber() == 1){
            Main.getModel().getCurrentPlayer().setName(name[1]);
            Main.getModel().getOtherPlayer().setName(name[2]);
        }
        else{
            Main.getModel().getCurrentPlayer().setName(name[2]);
            Main.getModel().getOtherPlayer().setName(name[1]);
        }

        ui.displayGreeting("The first player is, "+ name[Main.getModel().getCurrentPlayerNumber()] +"!");


    }
}
