package se.yrgo.game;

import java.util.ArrayList;
import java.util.Collections;
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
import com.badlogic.gdx.Preferences;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends ApplicationAdapter {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final int GAP = 250;

    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Texture> textureList;
    private SpriteBatch batch;
    private Texture lappstiftet;
    private Texture karlatornet;
    private Texture lisebergstornet;
    private Texture masthugg;
    private Texture poseidon;
    private Texture bg;
    private Texture birb;

    private BitmapFont font;

    private FitViewport viewport;

    private GameObject player;
    private int score;

    private boolean isCollided; // Currently used for collision testing, might impact game flow later
    private boolean stopGame;   // Stops the game if hit a obstacle

    private int speed;
    private float yVelocity = 0;
    float gravity = -2.5f;
    float jumpStrength = 30;
    private Preferences prefs;
    private int highScore;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        Gdx.graphics.setResizable(false);

        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        speed = 5;

        loadTextures();

        font = new BitmapFont();


        player = new GameObject(birb, 50, 335, 50, 50, -2.5f);
        obstacleList = new ArrayList<>();


        isCollided = false;

        score = 0;

        prefs = Gdx.app.getPreferences("HighScoreDataFile");
        highScore = prefs.getInteger("highscore", 0); //default score if no score yet
        prefs.flush();

        System.out.println("Highscore from prefs: " + highScore); //print out to see if its working
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

//    public int getHighScore() { //Maybe not needed?
//        return highScore;
//    }

    public void setHighScore(int score) {
        prefs = Gdx.app.getPreferences("HighScoreDataFile"); //HighScore is being saved in this file.
        prefs.putInteger("highscore", score);
        highScore = score;
        prefs.flush();

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        viewport.apply();
        if (obstacleList.isEmpty()) {
            Obstacle firstObstacle = new Obstacle(textureList.get(0), 1380, 0, 100, 200, GAP);
            obstacleList.add(firstObstacle);
        }
        if (!stopGame) {
            input();
            updateGameObjects();
            obstacleLogic();

            collision();
        }
        if(stopGame && score > highScore) {  //Sets new high score if the new score is higher
            setHighScore(score);
        }

        drawing();

    }

    public void input() {

        // skips any further inputs if the game is over
        if (stopGame) {
            // Start a new game with "N" or mouse right-click. all other buttons cease to work
            if (Gdx.input.isKeyJustPressed(Input.Keys.N)|| Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
                newGame();
            }
            return;
        }

        //Control the birb with SPACE key and mouse click
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
            //|| Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
            ) {
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

        // When collision occurs: stop game
        if (isCollided) {
            //font.draw(batch, "OBSTACLE HIT", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
            stopGame = true;
        }

        //Game over screen
        if(stopGame){
             font.draw(batch, "GAME OVER", SCREEN_WIDTH / 2, SCREEN_HEIGHT /2 + 100);
             font.draw(batch, "Your score is: " + score, SCREEN_WIDTH / 2, SCREEN_HEIGHT /2 + 50);
             font.draw(batch, "Press N for new game", SCREEN_WIDTH / 2, SCREEN_HEIGHT /2);
            font.draw(batch, "The highscore is: " + highScore, SCREEN_WIDTH / 2, SCREEN_HEIGHT /2 - 50);
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
            int texture = ThreadLocalRandom.current().nextInt(textureList.size());
            obstacleList.add(new Obstacle(textureList.get(texture), 1280, 0, 100, height, GAP));

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

    // Start a new game
    private void newGame(){

        // resets the flags
        stopGame = false;
        isCollided = false;
        score = 0;

        // reposition birb to initial position & values
        player.setPosition(50, SCREEN_HEIGHT / 2);
        player.setyVelocity(0);

        // resets obstacles
        obstacleList.clear();

    }


    private void loadTextures(){
        batch = new SpriteBatch();
        bg = new Texture("bg_blurred.png");
        karlatornet = new Texture("karlatornet.png");
        lappstiftet = new Texture("lappstiftet.png");
        lisebergstornet = new Texture("lisebergstornet.png");
        masthugg = new Texture("masthuggskyrkan.png");
        poseidon = new Texture("poseidon.png");
        birb = new Texture("doris.png");
        textureList = new ArrayList<>();
        Collections.addAll(textureList, karlatornet, lappstiftet, lisebergstornet, masthugg, poseidon);

    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        font.dispose();
        lappstiftet.dispose();
        karlatornet.dispose();
        lisebergstornet.dispose();
        masthugg.dispose();
        poseidon.dispose();
        birb.dispose();
    }
}
