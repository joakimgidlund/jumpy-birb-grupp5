package se.yrgo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class GameScreen implements Screen {
    final Birb game;

    private ArrayList<Obstacle> obstacleList;
    private ArrayList<Texture> textureList;
    private Texture lappstiftet;
    private Texture karlatornet;
    private Texture lisebergstornet;
    private Texture masthugg;
    private Texture poseidon;
    private Texture bg;
    private Texture birb;
    private Texture animatedbirb;
    private Texture drop;
    private ArrayList<GameObject> raindropList;

    private Texture sunsetBg;
    private Texture cloudsBg;
    private Texture skylineBg;
    private Texture oceanBg;
    private Texture boatBg;
    private Background background;

    private GameObject rain;
    private float rainTimer = 0f;
    private float rainSpawnInterval = 0.1f; // 0.1 sek mellan varje droppe

    private Stage stage;
    Table table;

    private GameObject player;
    private int score;

    private Sound sound;
    private Music music;

    private boolean stopGame; // Stops the game if hit a obstacle

    float jumpStrength = 25;

    private Preferences prefs;
    private int highScore;
    private String highScoreString;

    Difficulty difficulty;

    public GameScreen(final Birb game, Difficulty difficulty) {
        this.game = game;

        stage = new Stage(game.viewport);
        this.difficulty = difficulty;

        loadTextures();

        background = new Background(Birb.SCREEN_WIDTH,
            200.0f,
            new TextureRegion(sunsetBg),
            new TextureRegion(boatBg),
            new TextureRegion(oceanBg),
            new TextureRegion(cloudsBg),
            new TextureRegion(skylineBg));

        player = new GameObject(animatedbirb, 50, 335, 38, 50, -2.5f);
        obstacleList = new ArrayList<>();
        raindropList = new ArrayList<>();
        rain = new GameObject(drop, 100, 720, 4, 4, -2f);
        score = 0;
        sound = Gdx.audio.newSound(Gdx.files.internal("Seagull.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("jumpy_birb_theme.mp3"));
        music.setLooping(true);
        music.setVolume(0.5F);

        Gdx.input.setInputProcessor(new InputMultiplexer());

        prefs = Gdx.app.getPreferences("HighScoreDataFile");
        String key = "highscore_" + difficulty.getDifficulty().name();
        highScore = prefs.getInteger(key, 0); // highScore is 0 if not set.
        highScoreString = "Your high score for " + difficulty.getDifficulty().name() + " is: " + highScore;
        prefs.flush();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {
        music.play();
        // insert music?
    }

    public void setHighScore(int score) {
        prefs = Gdx.app.getPreferences("HighScoreDataFile"); // HighScore is being saved in this file.
        String key = "highscore_" + difficulty.getDifficulty().name(); // sets e.g highscore_EASY
        prefs.putInteger(key, score);
        highScore = score;
        highScoreString = "New high score for level " + difficulty.getDifficulty().name() + " is: " + highScore + "!!!";
        prefs.flush();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();

        if (obstacleList.isEmpty()) {
            Obstacle firstObstacle = new Obstacle(textureList.get(0), 1380, 0, 100, 200, difficulty.getGap());
            obstacleList.add(firstObstacle);
        }

        if (!stopGame) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            background.update(delta);
            updateGameObjects();
            obstacleLogic();
            collision();
            if (difficulty.getIsRaining()) {
                rainLogic();
                makeItRain();
            }
        }
        if (stopGame && score > highScore) { // Sets new high score if the new score is higher
            setHighScore(score);
        }

        input();

        drawing();
    }

    private void input() {
        // Control the birb with SPACE key and mouse click
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && !stopGame) {
            player.setyVelocity(jumpStrength);
            player.setWingFlap(true);
            sound.play(1.0f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game));
        }

        if (stopGame
                && Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            // Start a new game with "N" or mouse right-click. all other buttons cease to
            // work
            newGame();
        }
    }

    private void updateGameObjects() {
        player.movement();
        player.updateAnimation(Gdx.graphics.getDeltaTime());
    }

    private void makeItRain() {
        for (GameObject rain : raindropList) {
            rain.movement();

        }
    }

    private void drawing() {
        game.batch.begin();
        // game.batch.draw(bg, 0, 0);
        background.render(game.batch);

        Obstacle.drawObstacles(game.batch, obstacleList);
         // Draw rain
         GameObject.drawRain(game.batch, raindropList);

        // Draw player character
        game.batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);

        // Draw a text with current score
        game.font.draw(game.batch, "Score: " + score, Birb.SCREEN_WIDTH / 2 - 30, Birb.SCREEN_HEIGHT - 50);

        // Game over screen
        if (stopGame) {
            drawGameOver();
        } else {
            game.batch.end();
        }
    }

    private void drawGameOver() {
        table = new Table();
        LabelStyle labelStyle = new LabelStyle(game.font, new Color(255, 255, 255, 1f));
        Label gameOverLabel = new Label("GAME OVER", labelStyle);
        Label scoreLabel = new Label("Your score was: " + score, labelStyle);
        Label newGameLabel = new Label("Press N to start a new game.\nPress ESC to exit to main menu.", labelStyle);
        Label highScoreLabel = new Label(highScoreString, labelStyle);

        table.setFillParent(true);
        table.top();

        Color c = game.batch.getColor();
        game.batch.setColor(c.r, c.g, c.g, 0.5f);
        game.batch.draw(new Texture("tint.png"), 0, 0);
        game.batch.setColor(c.r, c.g, c.g, 1f);
        game.batch.end();

        table.padTop(50);
        table.add(new Image(new Texture("deathcrop.png")));
        table.row();
        table.add(gameOverLabel);
        table.row();
        table.add(scoreLabel);
        table.row();
        table.add(highScoreLabel);
        table.row();
        table.add(newGameLabel).padTop(200);

        stage.addActor(table);
        stage.draw();
    }

    private void obstacleLogic() {

        // Sets the speed (updates position on all elements, "movement")
        for (Obstacle o : obstacleList) {
            o.move(difficulty.getSpeed());
        }

        // Creates a new obstacle when the last obstacle on screen reaches below 700
        // pixels.
        if (obstacleList.get(obstacleList.size() - 1).getBottomRect().x < 700) {
            int height = ThreadLocalRandom.current().nextInt(350) + 100;
            int texture = ThreadLocalRandom.current().nextInt(textureList.size());
            obstacleList.add(new Obstacle(textureList.get(texture), 1280, 0, 100, height, difficulty.getGap()));
        }

        // Removes the first obstacle that goes off-screen.
        // Currently also resets collision flag for testing
        if (obstacleList.get(0).getBottomRect().x < -100) {
            obstacleList.remove(0);
        }
    }
    private void rainLogic() {
        rainTimer += Gdx.graphics.getDeltaTime();

        if (rainTimer >= rainSpawnInterval) {
            int height = ThreadLocalRandom.current().nextInt(720, 800);
            int width = ThreadLocalRandom.current().nextInt(0, 1280);
            raindropList.add(new GameObject(drop, width, height, 4, 4, -2.0f));

            rainTimer = 0f;
        }

        // Removes the first drop that goes off-screen.
        if (!raindropList.isEmpty() && raindropList.get(0).getPosition().y < -100) {
            raindropList.remove(0);
        }
    }

    public void collision() {
        // Saving player position for cleaner if-case
        Rectangle playerPos = player.getPosition();

        // Fetches the first obstacle, since it's the only one we can collide with
        Obstacle firstObstacle = obstacleList.get(0);

        // Adds score if an obstacle is avoided, but only once
        if (player.getPosition().overlaps(firstObstacle.getGapRect())
                && !firstObstacle.getIsScored()) {
            firstObstacle.setIsScored(true);
            score++;
        }

        // This checks collision with obstacles (and stage bounds), ends the round
        if (player.getPosition().overlaps(firstObstacle.getBottomRect())
                || player.getPosition().overlaps(firstObstacle.getTopRect())
                || (playerPos.y < -20 || playerPos.y > 725)) {
            stopGame = true;
        }
    }

    // Start a new game
    private void newGame() {

        highScoreString = "Your high score for level " + difficulty.getDifficulty().name() + " is: " + highScore;

        table.clear();
        stage.clear();

        // resets the flags
        stopGame = false;
        score = 0;

        // reposition birb to initial position & values
        player.setPosition(50, Birb.SCREEN_HEIGHT / 2);
        player.setyVelocity(0);

        // resets obstacles
        obstacleList.clear();
    }

    private void loadTextures() {
        bg = new Texture("softsunset_bg.png");
        karlatornet = new Texture("obs_karlatornet.png");
        lappstiftet = new Texture("obs_lappstiftet.png");
        lisebergstornet = new Texture("obs_liseberg.png");
        masthugg = new Texture("obs_masthuggskyrkan.png");
        poseidon = new Texture("obs_poseidon.png");
        birb = new Texture("doris.png");
        animatedbirb = new Texture("spritesheetbirb.png");
        textureList = new ArrayList<>();
        drop = new Texture("raindrop.png");
        sunsetBg = new Texture("sunset_layer.png");
        skylineBg = new Texture("skylinebg.png");
        cloudsBg = new Texture("clouds.png");
        oceanBg = new Texture("ocean.png");
        boatBg = new Texture("boatbg.png");

        Collections.addAll(textureList, karlatornet, lappstiftet, lisebergstornet, masthugg, poseidon);
    }

    @Override
    public void hide() {
        music.stop();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
        animatedbirb.dispose();
        music.dispose();
        drop.dispose();
    }
}
