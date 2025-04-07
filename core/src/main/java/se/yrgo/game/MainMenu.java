package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen{

    Birb game;
    Stage stage;
    TextureAtlas atlas;
    Skin skin;
    Texture splash;

    public MainMenu(Birb game) {
        this.game = game;

        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        splash = new Texture("doris.png");

        stage = new Stage(game.viewport);
        Gdx.input.setInputProcessor(stage);

        game.viewport.apply();
    }

    @Override
    public void show() {
        Table table = new Table();

        table.setFillParent(true);
        table.top();

        TextButton play = new TextButton("Play", skin);
        TextButton quit = new TextButton("Quit", skin);
        
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.padTop(400);
        table.add(play).height(100).width(200);
        table.row();
        table.add(quit).height(100).width(200);
        table.row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.batch.begin();
        // game.font.draw(game.batch, "Press N to start.", Birb.SCREEN_WIDTH / 2, Birb.SCREEN_HEIGHT / 2);
        game.batch.draw(splash, game.SCREEN_WIDTH / 2 - 25, 600);
        game.batch.end();
        
        stage.act();
        stage.draw();

        // if (Gdx.input.isKeyJustPressed(Input.Keys.N) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
        //     game.setScreen(new GameScreen(game));
        //     dispose();
        // }

        // if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        //     Gdx.app.exit();
        // }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}
