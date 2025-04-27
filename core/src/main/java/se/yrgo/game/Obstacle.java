package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Obstacle {

    private Texture texture;
    private Rectangle bottomRect;
    private Rectangle topRect;
    private Rectangle gapRect;

    private boolean isScored;

    public Obstacle(Texture texture, int x, int y, int width, int height, int gap) {
        this.texture = texture;
        bottomRect = new Rectangle(x, y, width, height);
        topRect = new Rectangle(x, y + height + gap, width, 720);
        gapRect = new Rectangle(x + 100f, 0, width, 720);
        isScored = false;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBottomRect() {
        return bottomRect;
    }

    public Rectangle getTopRect() {
        return topRect;
    }

    public Rectangle getGapRect() {
        return gapRect;
    }

    public void setIsScored(boolean isScored) {
        this.isScored = isScored;
    }

    public boolean getIsScored() {
        return isScored;
    }

    public void move(int speed) {
        bottomRect.x += speed;
        topRect.x += speed;
        gapRect.x += speed;
    }

    public static void drawObstacles(SpriteBatch batch, ArrayList<Obstacle> obstacleList) {
        for (Obstacle o : obstacleList) {
            batch.draw(o.texture, o.bottomRect.x, o.bottomRect.y,
                0, 0, (int) o.bottomRect.width, (int) o.bottomRect.height);

            batch.draw(o.texture, o.topRect.x, o.topRect.y, o.topRect.width,
                o.topRect.height, 0, 0, 100, 720, false, true);
        }
    }
}
