package se.yrgo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Birb extends Game {

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    public void create() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        Gdx.graphics.setResizable(false);
        //Sets the FPS to 60 locked
        Gdx.graphics.setForegroundFPS(60);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1f);

        this.setScreen(new MainMenu(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
