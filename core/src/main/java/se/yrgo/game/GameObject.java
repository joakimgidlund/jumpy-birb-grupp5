package se.yrgo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {

    private Texture texture;
    private Rectangle position;
    private Animation<TextureRegion> animation;
    private TextureRegion[] regions;

    private float yVelocity;
    private float gravity;
    private float stateTime = 0;
    private boolean wingsIsFlapping = false;

    public GameObject(Texture texture, int x, int y, int height, int width, float gravity) {
        this.texture = texture;
        position = new Rectangle(x, y, width, height);
        yVelocity = 0;
        this.gravity = gravity;
        this.regions = createRegions(texture, 50, 38);
        this.animation = new Animation<>(0.15f, regions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private TextureRegion[] createRegions(Texture texture, int spriteWidth, int spriteHeight) {
        int sheetWidth = texture.getWidth();
        int sheetHeight = texture.getHeight();
        int cols = sheetWidth / spriteWidth;
        int rows = sheetHeight / spriteHeight;

        TextureRegion[][] tempRegions = TextureRegion.split(texture, spriteWidth, spriteHeight);
        TextureRegion[] frames = new TextureRegion[rows * cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tempRegions[i][j];
            }
        }
        return frames;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public void yMove(float yVelocity) {
        position.y += yVelocity;
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
        yMove(yVelocity);
    }

    public void updateAnimation(float deltaTime) {
        if (wingsIsFlapping) {
            stateTime += deltaTime;
            if (animation.isAnimationFinished(stateTime)) {
                wingsIsFlapping = false;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        if (wingsIsFlapping) {
            return animation.getKeyFrame(stateTime, false);
        }
        return regions[0];
    }

    public void setWingFlap(boolean wingFlap) {
        this.wingsIsFlapping = wingFlap;
        if (wingsIsFlapping) {
            stateTime = 0;
        }
    }
}
