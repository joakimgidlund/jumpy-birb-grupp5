package se.yrgo.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
    private ArrayList<Obstacle> obstacleList;
    private SpriteBatch batch;
    private Texture obs;
    private Texture bg;
    private Texture birb;

    private FitViewport viewport;

    private Obstacle obstacle;
    private GameObject player;

    private int speed;

    private static final int screenWidth = 1024;
    private static final int screenHeight = 720;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
        Gdx.graphics.setResizable(false);

        viewport = new FitViewport(screenWidth, screenHeight);
        speed = 5;

        batch = new SpriteBatch();
        bg = new Texture("bg.png");
        obs = new Texture("obstacle.png");
        birb = new Texture("birb.png");

        obstacle = new Obstacle(obs, 924, 0, 200, 100, 500);
        player = new GameObject(birb, 50, 360, 50, 50);
        obstacleList = new ArrayList<>();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if(obstacleList.isEmpty()) {
            obstacleList.add(obstacle);
        }

        if (obstacleList.get(obstacleList.size() - 1).getDown().x < 700) {
            obstacleList.add(new Obstacle(obs, 1124, 0, 100, 200, 600));
        }

        if (obstacleList.get(0).getDown().x < -100) {
            obstacleList.remove(0);
        }

        viewport.apply();

        batch.begin();
        batch.draw(bg, 0, 0);
        for (Obstacle o : obstacleList) {
            batch.draw(o.getTexture(), o.getDown().x, o.getDown().y);
            batch.draw(o.getTexture(), o.getUp().x, o.getUp().y);
        }

        obstacleList.get(0).setPosition(-speed);
        if(obstacleList.size() > 1)
            obstacleList.get(1).setPosition(-speed);


        // for(Obstacle o : obstacleList) {
        //     o.setPosition(-speed);
        // }
        // batch.draw(obstacle.getTexture(), obstacle.getPosition().x,
        // obstacle.getPosition().y);
        batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        obs.dispose();
        birb.dispose();
    }
}
