package gui.graphical;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import source.Main;
import source.Model;
import source.Player;

public class EndScreen extends ScreenAdapter {
    BlokusDuoGame blokusDuoGame;
    Stage stage;
    Skin skin;
    Table table;
    Player winner;

    SpriteBatch batch;

    public EndScreen(BlokusDuoGame blokusDuoGame, Player winner) {
        this.blokusDuoGame = blokusDuoGame;
        this.stage = blokusDuoGame.stage;
        this.skin = blokusDuoGame.skin;
        table = new Table();
        table.setFillParent(true);
        this.winner = winner;

    }

    public void show() {
        super.show();
        stage.addActor(table);
    }

    public void hide() {
        super.hide();
        stage.clear();
    }

    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        stage.act();
        stage.draw();
        blokusDuoGame.showEndDialog((winner==null ? "A draw!" : (winner.getName() + " wins!")) + " Thanks for playing :)");
    }

    public void resize(int width, int height) {
        super.resize(width,height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        super.dispose();
    }
}
