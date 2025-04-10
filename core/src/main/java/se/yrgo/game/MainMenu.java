package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen {

    Birb game;
    Stage stage;

    Texture splash;
    Texture background;
    
    Texture playUp;
    Texture playDown;
    Texture quitUp;
    Texture quitDown;

    public MainMenu(Birb game) {
        this.game = game;

        splash = new Texture("doris.png");
        background = new Texture("start_menu.png");

        playUp = new Texture("play_up.png");
        playDown = new Texture("play_down.png");
        quitUp = new Texture("quit_up.png");
        quitDown = new Texture("quit_down.png");
        

        stage = new Stage(game.viewport);
        Gdx.input.setInputProcessor(stage);

        game.viewport.apply();
    }

    @Override
    public void show() {
        Table table = new Table();

        table.setFillParent(true);
        table.top();

        Drawable playUpDraw = new TextureRegionDrawable(playUp);
        Drawable playDownDraw = new TextureRegionDrawable(playDown);
        Button playButton = new Button(playUpDraw, playDownDraw);

        Drawable quitUpDraw = new TextureRegionDrawable(quitUp);
        Drawable quitDownDraw = new TextureRegionDrawable(quitDown);
        Button quitButton = new Button(quitUpDraw, quitDownDraw);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.padTop(300);
        table.add(playButton).height(100).width(200).padBottom(50);
        table.row();
        table.add(quitButton).height(100).width(200);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.draw(splash, Birb.SCREEN_WIDTH / 2 - 25, 600);
        game.batch.end();
        
        stage.act();
        stage.draw();
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
        background.dispose();
        splash.dispose();
        playDown.dispose();
        playUp.dispose();
        quitUp.dispose();
        quitDown.dispose();
    }
}
