package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private Texture texture;
    private Rectangle bottomRect;
    private Rectangle topRect;
    private int gap;

    public Obstacle(Texture texture, int x, int y, int width, int height, int gap) {
        this.texture = texture;
        this.gap = gap;
        bottomRect = new Rectangle(x, y, width, height);
        topRect = new Rectangle(x, y + height + gap, width, 720);
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

    public void setPosition(int speed) {
        bottomRect.x += speed;
        topRect.x += speed;
    }
}
