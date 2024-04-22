package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TheProgressBars {
    private ProgressBar progressBar;

    public TheProgressBars(Skin skin) {
        ProgressBar.ProgressBarStyle progressBarStyle = skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        progressBar = new ProgressBar(0, 6000, 1, false, progressBarStyle);
        progressBar.setWidth(500);
        progressBar.setHeight(50);
        progressBar.setValue(6000);
        progressBar.setX(0);
        progressBar.setY(Gdx.graphics.getHeight() - progressBar.getHeight() - 50);
    }

    public void setBarColor() {
        float value = this.getProgressBar().getValue();
        float progress = value / this.getProgressBar().getMaxValue();
        ProgressBar.ProgressBarStyle newStyle = new ProgressBar.ProgressBarStyle();
        newStyle.background = createDrawableWithOutline(Color.DARK_GRAY, 500, 40, Color.BLACK);
        newStyle.knobBefore = createDrawableWithOutline(getColorForValue(progress), 500, 40, Color.BLACK);

        this.getProgressBar().setStyle(newStyle);
    }

    private Drawable createDrawableWithOutline(Color color, int width, int height, Color outlineColor) {
        int outlineWidth = 2;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(outlineColor);
        pixmap.fill();

        Pixmap innerPixmap = new Pixmap(width - 2 * outlineWidth, height - 2 * outlineWidth, Pixmap.Format.RGBA8888);
        innerPixmap.setColor(color);
        innerPixmap.fill();

        pixmap.drawPixmap(innerPixmap, outlineWidth, outlineWidth);
        Texture texture = new Texture(pixmap);
        innerPixmap.dispose();
        pixmap.dispose();

        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private Color getColorForValue(float progress) {
        if (progress >= 0.7) {
            return Color.GREEN;
        } else if (progress >= 0.5) {
            return Color.YELLOW;
        } else if (progress >= 0.3) {
            return Color.ORANGE;
        } else {
            return Color.RED;
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public float getValue() {
        return progressBar.getValue();
    }

    public void setValue(float value) {
        progressBar.setValue(value);
    }

    public void create(Stage stage) {
        stage.addActor(progressBar);
    }

    public void render() {
        setValue(progressBar.getValue() - 0.01f);
        setBarColor();
    }
}
