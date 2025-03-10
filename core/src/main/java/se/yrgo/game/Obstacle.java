package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private Texture texture;
    private Rectangle down;
    private Rectangle up;
    private int gap;

    public Obstacle(Texture texture, int x, int y, int width, int height, int gap) {
        this.texture = texture;
        this.gap = gap;
        down = new Rectangle(x, y, width, height);
        up = new Rectangle(x, y + gap, width, height);
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getDown() {
        return down;
    }

    public Rectangle getUp() {
        return up;
    }

    public void setPosition(int speed) {
        down.x += speed;
        up.x += speed;
    }
}
