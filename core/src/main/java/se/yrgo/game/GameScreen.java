package se.yrgo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class GameScreen implements Screen {
    final Birb game;

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

    private GameObject player;
    private int score;

    private boolean isCollided; // Currently used for collision testing, might impact game flow later
    private boolean stopGame;   // Stops the game if hit a obstacle

    private int speed;

    float gravity = -2.5f;
    float jumpStrength = 30;

    public GameScreen(final Birb game) {
        this.game = game;

        loadTextures();

        speed = 5;

        player = new GameObject(birb, 50, 335, 50, 50, -2.5f);
        obstacleList = new ArrayList<>();
        isCollided = false;
        score = 0;
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {
        //insert music?
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        if (stopGame) {
            // Start a new game with "N" or mouse right-click. all other buttons cease to work
            if (Gdx.input.isKeyJustPressed(Input.Keys.N)|| Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
                newGame();
            }
        }
        if (obstacleList.isEmpty()) {
            Obstacle firstObstacle = new Obstacle(textureList.get(0), 1380, 0, 100, 200, GAP);
            obstacleList.add(firstObstacle);
        }
        if (!stopGame) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            input();
            updateGameObjects();
            obstacleLogic();
    
            collision();
        }
        drawing();
    }

    public void input() {

        //Control the birb with SPACE key and mouse click
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
            //|| Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
            ) {
            player.setyVelocity(jumpStrength);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game));
        }

    }

    public void updateGameObjects() {
        player.movement();
    }


    private void drawing() {
        game.batch.begin();
        game.batch.draw(bg, 0, 0);

        Obstacle.drawObstacles(game.batch, obstacleList);

        // Draw player character
        game.batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);

        // Draw a text with current score
        game.font.draw(game.batch, "Score: " + score, Birb.SCREEN_WIDTH / 2, Birb.SCREEN_HEIGHT - 50);

        // When collision occurs: stop game
        if (isCollided) {
            //font.draw(batch, "OBSTACLE HIT", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 100);
            stopGame = true;
        }

        //Game over screen
        if(stopGame){
            game.font.draw(game.batch, "GAME OVER", Birb.SCREEN_WIDTH / 2, Birb.SCREEN_HEIGHT /2);
            game.font.draw(game.batch, "Your score is: " + score, Birb.SCREEN_WIDTH / 2, Birb.SCREEN_HEIGHT /2 - 50);
            //game.font.draw(batch, "Press N for new game", SCREEN_WIDTH / 2, SCREEN_HEIGHT /2 - 100);
        }

        game.batch.end();
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
        player.setPosition(50, Birb.SCREEN_HEIGHT / 2);
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
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        bg.dispose();
        lappstiftet.dispose();
        karlatornet.dispose();
        lisebergstornet.dispose();
        masthugg.dispose();
        poseidon.dispose();
        birb.dispose();
    }
}
