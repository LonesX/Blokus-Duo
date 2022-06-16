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

public class StartScreen extends ScreenAdapter {
    BlokusDuoGame blokusDuoGame;
    Stage stage;
    Skin skin;
    Table table;

    SpriteBatch batch;

    public StartScreen(BlokusDuoGame blokusDuoGame) {
        this.blokusDuoGame = blokusDuoGame;
        this.stage = blokusDuoGame.stage;
        this.skin = blokusDuoGame.skin;

        table = new Table();
        table.setFillParent(true);
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
    }

    public void resize(int width, int height) {
        super.resize(width,height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        super.dispose();
    }
}
