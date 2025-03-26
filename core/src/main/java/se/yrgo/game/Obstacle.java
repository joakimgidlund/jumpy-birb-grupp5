package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

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
        gapRect = new Rectangle(x, y + height, width, gap);
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

    public void setPosition(int speed) {
        bottomRect.x += speed;
        topRect.x += speed;
        gapRect.x += speed;
    }
}
