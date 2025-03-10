package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {

    private Texture texture;
    private Rectangle position;
    
    
    public GameObject(Texture texture, int x, int y, int height, int width) {
        this.texture = texture;
        position = new Rectangle(x, y, width, height);
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.y += y;
    }
}
