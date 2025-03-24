package se.yrgo.game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

    private static final int screenWidth = 1280;
    private static final int screenHeight = 720;
    private static final int gap = 250;

    private ArrayList<Obstacle> obstacleList;
    private SpriteBatch batch;
    private Texture obs;
    private Texture bg;
    private Texture birb;

    private FitViewport viewport;

    private Obstacle firstObstacle;
    private GameObject player;

    private int speed;
    private float y = 10;
    private float yVelocity = 0;
    float gravity = -2.5f;
    float jumpStrength = 10;
    boolean onGround = true;

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

        firstObstacle = new Obstacle(obs, 1380, 0, 100, 200, gap);
        player = new GameObject(birb, 50, 360, 50, 50);
        obstacleList = new ArrayList<>();
        obstacleList.add(firstObstacle);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();

        update();
        obstacleLogic();

        drawing();
    }

    public void input() {
        int speed = 10;

        //Control the birb with SPACE key
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            yVelocity = jumpStrength;
        }
        /*Control the birb with left mouse click. You can alternate between Space key and
        mouse click in the same game */
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            yVelocity = jumpStrength;
        }
    }

    public void update() {
        input();   //Handles user input with SPACE kay
        yVelocity += gravity;  //Applies gravity
        y += yVelocity;   //Updates y position

        player.setPosition(0, (int) y);

//        if(y < 0){        was supposed to prevent the birb from falling below ground level - not working
//            y = 0;
//            yVelocity = 0;
//        }
    }


    private void drawing() {
        batch.begin();
        batch.draw(bg, 0, 0);

        // Iterate through the obstacle list and draw them all on screen
        for (Obstacle o : obstacleList) {
            batch.draw(o.getTexture(), o.getBottomRect().x, o.getBottomRect().y, o.getBottomRect().width,
                    o.getBottomRect().height);
            batch.draw(o.getTexture(), o.getTopRect().x, o.getTopRect().y, o.getTopRect().width, o.getTopRect().height);
        }

        //Draw player character
        batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        batch.end();
    }

    public void obstacleLogic() {

        // Sets the speed (updates position on all elements, "movement")
        for (Obstacle o : obstacleList) {
            o.setPosition(-speed);
        }

        // Creates a new obstacle when the last obstacle on screen reaches below 700
        // pixels.
        if (obstacleList.get(obstacleList.size() - 1).getBottomRect().x < 700) {
            int height = ThreadLocalRandom.current().nextInt(350) + 100;
            obstacleList.add(new Obstacle(obs, 1280, 0, 100, height, gap));
        }

        // Removes the first obstacle that goes off-screen.
        if (obstacleList.get(0).getBottomRect().x < -100) {
            obstacleList.remove(0);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        obs.dispose();
        birb.dispose();
    }
}
