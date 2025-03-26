package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {

    private Texture texture;
    private Rectangle position;

    private float yVelocity;
    private float gravity;


    public GameObject(Texture texture, int x, int y, int height, int width, float gravity) {
        this.texture = texture;
        position = new Rectangle(x, y, width, height);
        yVelocity = 0;
        this.gravity = gravity;
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

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void movement() {
        if(yVelocity > 0) {
            yVelocity += gravity;  //Applies gravity
        }
        else {
            yVelocity = gravity * 1.5f;
        }
        //Update the birbs position with the new y value
        setPosition(0, (int) yVelocity);
    }
}
