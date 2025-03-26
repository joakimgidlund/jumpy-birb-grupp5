package se.yrgo.game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends ApplicationAdapter {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int GAP = 250;

    private ArrayList<Obstacle> obstacleList;
    private SpriteBatch batch;
    private Texture obs;
    private Texture bg;
    private Texture birb;

    private BitmapFont font;

    private FitViewport viewport;

    private GameObject player;
    private int score;

    private boolean isCollided; // Currently used for collision testing, might impact game flow later

    private int speed;
    private float yVelocity = 0;
    float gravity = -2.5f;
    float jumpStrength = 30;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        Gdx.graphics.setResizable(false);

        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        speed = 5;

        batch = new SpriteBatch();
        bg = new Texture("bg.png");
        obs = new Texture("obstacle.png");
        birb = new Texture("birb.png");

        font = new BitmapFont();

        Obstacle firstObstacle = new Obstacle(obs, 1380, 0, 100, 200, GAP);
        player = new GameObject(birb, 50, 335, 50, 50, -2.5f);
        obstacleList = new ArrayList<>();
        obstacleList.add(firstObstacle);

        isCollided = false;

        score = 0;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();

        input();
        updateGameObjects();
        obstacleLogic();

        collision();

        drawing();
    }

    public void input() {

        //Control the birb with SPACE key and mouse click
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
            || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            player.setyVelocity(jumpStrength);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

    }

    public void updateGameObjects() {
        player.movement();
    }


    private void drawing() {
        batch.begin();
        batch.draw(bg, 0, 0);

        Obstacle.drawObstacles(batch, obstacleList);

        // Draw player character
        batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);

        // Draw a text with current score
        font.draw(batch, "Score: " + score, SCREEN_WIDTH / 2, SCREEN_HEIGHT - 50);
        if (isCollided) {
            font.draw(batch, "OBSTACLE HIT", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
        }

        batch.end();
    }

    public void obstacleLogic() {

        // Sets the speed (updates position on all elements, "movement")
        for (Obstacle o : obstacleList) {
            o.move(-speed);
        }

        // Creates a new obstacle when the last obstacle on screen reaches below 700
        // pixels.
        if (obstacleList.get(obstacleList.size() - 1).getBottomRect().x < 700) {
            int height = ThreadLocalRandom.current().nextInt(350) + 100;
            obstacleList.add(new Obstacle(obs, 1280, 0, 100, height, GAP));

            // Print information about added obstacle
            // System.out.printf("New obstacle height: %d%nObstacleList index: %d%n",
            // height, obstacleList.size() - 1);
        }

        // Removes the first obstacle that goes off-screen.
        // Currently also resets collision flag for testing
        if (obstacleList.get(0).getBottomRect().x < -100) {
            obstacleList.remove(0);
            isCollided = false;
        }
    }

    public void collision() {
        // Fetches the first obstacle, since it's the only one we can collide with
        Obstacle firstObstacle = obstacleList.get(0);

        // Adds score if an obstacle is avoided, but only once
        if (player.getPosition().overlaps(firstObstacle.getGapRect())
                && !firstObstacle.getIsScored()) {
            firstObstacle.setIsScored(true);
            score++;
        }

        // This checks collision with obstacles, will end the game in the future
        if (player.getPosition().overlaps(firstObstacle.getBottomRect())
                || player.getPosition().overlaps(firstObstacle.getTopRect())) {
            isCollided = true;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        font.dispose();
        obs.dispose();
        birb.dispose();
    }
}
