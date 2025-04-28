package se.yrgo.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import se.yrgo.game.Difficulty.Diff;

public class MainMenu implements Screen {

    Birb game;
    Stage stage;

    Texture splash;
    Texture background;

    Texture playUp;
    Texture playDown;
    Texture quitUp;
    Texture quitDown;
    Texture diffSelectTexture;

    Sound buttonPress;

    Difficulty difficultyLevel;

    private BitmapFont difficultyFont;

    public MainMenu(Birb game) {
        this.game = game;

        difficultyLevel = new Difficulty(Diff.MEDIUM);

        splash = new Texture("doris.png");
        background = new Texture("menu-assets/start_menu.png");

        playUp = new Texture("menu-assets/play_up.png");
        playDown = new Texture("menu-assets/play_down.png");
        quitUp = new Texture("menu-assets/quit_up.png");
        quitDown = new Texture("menu-assets/quit_down.png");
        diffSelectTexture = new Texture("menu-assets/listBg.png");

        buttonPress = Gdx.audio.newSound(Gdx.files.internal("sounds/button_press.mp3"));

        stage = new Stage(game.viewport);
        Gdx.input.setInputProcessor(stage);

        game.viewport.apply();

        difficultyFont = new BitmapFont(Gdx.files.internal("fonts/Font_ErasBoldV4small.fnt")); // font
    }

    @Override
    public void show() {
        Table table = new Table();

        table.setFillParent(true);
        table.top();
        table.setPosition(50, 0);

        Drawable playUpDraw = new TextureRegionDrawable(playUp);
        Drawable playDownDraw = new TextureRegionDrawable(playDown);
        Button playButton = new Button(playUpDraw, playDownDraw);

        Drawable quitUpDraw = new TextureRegionDrawable(quitUp);
        Drawable quitDownDraw = new TextureRegionDrawable(quitDown);
        Button quitButton = new Button(quitUpDraw, quitDownDraw);

        Drawable listBg = new TextureRegionDrawable(diffSelectTexture);
        ListStyle listStyle = new ListStyle(difficultyFont, new Color(1f, 1f, 1f, 1f), new Color(1f, 1f, 1f, 1f),
                listBg);

        List diffSelectionList = new List(listStyle);
        Difficulty[] diffs = { new Difficulty(Diff.EASY), new Difficulty(Diff.MEDIUM), new Difficulty(Diff.HARD) };
        diffSelectionList.setItems((Object[]) diffs);
        diffSelectionList.setSelectedIndex(1);

        diffSelectionList.addCaptureListener(new InputListener() {
            public boolean keyUp(InputEvent event, int button) {
                if (button == Keys.SPACE) {
                    buttonPress.play(0.5f);
                    difficultyLevel = (Difficulty) diffSelectionList.getSelected();
                    Timer.schedule(new Task() {
                        @Override
                        public void run() {
                            game.setScreen(new GameScreen(game, difficultyLevel));
                        }
                    }, 0.5f);
                }
                return false;
            }
        });

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonPress.play(0.5f);
                difficultyLevel = (Difficulty) diffSelectionList.getSelected();
                Timer.schedule(new Task() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, difficultyLevel));
                    }
                }, 0.5f);
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonPress.play(0.5f);
                Timer.schedule(new Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.5f);
            }
        });

        table.padTop(300);
        table.add(playButton).height(100).width(200).padBottom(30);
        table.add(diffSelectionList).height(100).padLeft(10);
        table.row();
        table.add(quitButton).height(100).width(200);

        stage.setKeyboardFocus(diffSelectionList);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.draw(splash, Birb.SCREEN_WIDTH / 2f - 25f, 600);
        game.batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resizing is not available so not applicable
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
        buttonPress.dispose();
        difficultyFont.dispose();
    }
}
