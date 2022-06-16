package gui.graphical;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class InputScreen extends ScreenAdapter {

    BlokusDuoGame blokusDuoGame;

    Stage stage;
    Skin skin;
    Table table;

    public InputScreen(BlokusDuoGame blokusDuoGame) {
        this.blokusDuoGame = blokusDuoGame;
        this.stage = blokusDuoGame.stage;
        this.skin = blokusDuoGame.skin;

        table = new Table();
        table.setFillParent(true);

        Image image = new Image(new Texture("BlokusIcon.png") );
        table.add(image).padTop(0);
        table.row();


        Label label = new Label("Player 1 - Enter your name:",skin,"font", Color.WHITE );
        table.add(label).pad(10);
        table.row();

        TextField textField = new TextField(null,skin);
        textField.setMessageText("Player 1");
        table.add(textField).pad(10);
        table.row();

        Label label2 = new Label("Player 2 - Enter your name:",skin,"font", Color.WHITE );
        table.add(label2).pad(10);
        table.row();

        TextField textField2 = new TextField(null,skin);
        textField2.setMessageText("Player 2");
        table.add(textField2).pad(10);
        table.row();


        TextButton textButton = new TextButton("OK",skin);
        table.add(textButton).center();
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String player1 = textField.getText();
                String player2 = textField2.getText();
                if (player1.isEmpty()) {
                    player1 = "Player_1";
                }
                if (player2.isEmpty()) {
                    player2 = "Player_2";
                }

                // send name to game ui.control thread
                blokusDuoGame.uiStream.println(player1);
                blokusDuoGame.uiStream.println(player2);
            };
        });
    }

    public void show() {
        super.show();
        stage.addActor(table);
    }

    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        stage.act(delta);
        stage.draw();
    }

    public void hide() {
        super.hide();
        stage.clear();
    }

    public void resize(int width, int height) {
        super.resize(width,height);
        stage.getViewport().update(width, height, true);
    }
}
