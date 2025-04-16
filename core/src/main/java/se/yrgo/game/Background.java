package se.yrgo.game;

import com.badlogic.gdx.graphics.g2d.*;

public class Background {
    private TextureRegion[] bgLayers;
    private float[] speeds;
    private float[] scrolls;
    private float viewportWidth;
    private float cameraSpeed;

    public Background(float viewportWidth, float cameraSpeed, TextureRegion... bgLayers) {
        this.viewportWidth = viewportWidth;
        this.cameraSpeed = cameraSpeed;
        this.bgLayers = bgLayers;
        this.scrolls = new float[bgLayers.length];
        this.speeds = intervals(0.2f, 0.5f, bgLayers.length);
    }

    public void update(float delta) {
        for (int i = 0; i < scrolls.length; ++i) {
            scrolls[i] += cameraSpeed * speeds[i] * delta;
            scrolls[i] %= bgLayers[i].getRegionWidth();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < scrolls.length; ++i) {
            drawLayer(batch, bgLayers[i], scrolls[i]);
        }
    }

    private void drawLayer(SpriteBatch batch, TextureRegion layer, float scrollX) {
        float x = -scrollX;
        while (x < viewportWidth) {
            batch.draw(layer, x, 0);
            x += layer.getRegionWidth();
        }
    }

    private static float[] intervals(float min, float max, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Count must be at least 2.");
        }
        float[] result = new float[count];
        float step = (max - min) / (count - 1);
        for (int i = 0; i < count; i++) {
            result[i] = min + i * step;
        }
        return result;
    }
}
