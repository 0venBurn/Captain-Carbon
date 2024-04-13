package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;


public class EducationalPopup {
    private final Window window;
    private final Label messageLabel;
    private boolean isVisible;
    private Skin skin;

    public EducationalPopup() {
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        window = new Window("Info", skin);
        window.setSize(200, 100);
        window.setPosition(300, 400); // Customize as needed
        messageLabel = new Label("", skin);
        window.add(messageLabel);
        isVisible = false;
    }

    public void show(String message, float duration) {
        messageLabel.setText(message);
        window.setVisible(true);
        isVisible = true;
        // Add a delay to hide the popup
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                window.setVisible(false);
                isVisible = false;
            }
        }, duration);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void draw(SpriteBatch batch) {
        if (isVisible) {
            window.draw(batch, 1);
        }
    }
}
