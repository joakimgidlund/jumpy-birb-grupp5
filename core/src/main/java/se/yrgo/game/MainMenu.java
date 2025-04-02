package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen {

    Birb game;

    public MainMenu(Birb game) {
        this.game = game;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();

        game.batch.begin();
        game.font.draw(game.batch, "Press N to start.", Birb.SCREEN_WIDTH / 2, Birb.SCREEN_HEIGHT / 2);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.N) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
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
    }
}
