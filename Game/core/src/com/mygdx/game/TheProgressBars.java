package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TheProgressBars {
    private ProgressBar progressBar;


    public TheProgressBars(Skin skin) {
        ProgressBar.ProgressBarStyle progressBarStyle = skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        progressBar = new ProgressBar(0, 5000, 1, false, progressBarStyle);
        progressBar.setWidth(500);
        progressBar.setHeight(50);
        progressBar.setValue(5000);
        progressBar.setX(50);
        progressBar.setY(Gdx.graphics.getHeight() - progressBar.getHeight() - 50);
    }

    public void setBarColor(){
        Color knobColor;
        float value = this.getProgressBar().getValue();
        if (value >= 0.8* progressBar.getMaxValue()) {
            knobColor = Color.GREEN;
        } else if (value >= 0.6*progressBar.getMaxValue()) {
            knobColor = Color.ORANGE;
        } else if (value >= 0.4*progressBar.getMaxValue()) {
            knobColor = Color.YELLOW;
        } else {
            knobColor = Color.RED;
        }
        ProgressBar.ProgressBarStyle newStyle = new ProgressBar.ProgressBarStyle();
        newStyle.background = createColorDrawable(Color.WHITE, 500, 50);
        newStyle.knobBefore = createColorDrawable(knobColor, 500, 50);

        this.getProgressBar().setStyle(newStyle);

    }

    private Drawable createColorDrawable(Color color, int width, int height) {
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
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