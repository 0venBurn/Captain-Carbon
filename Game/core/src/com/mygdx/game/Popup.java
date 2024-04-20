package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Popup {
    private String message;
    private BitmapFont font;
    private SpriteBatch batch;

    public Popup() {
        font = new BitmapFont();
        font.setColor(Color.BLACK); // Set the font color
        batch = new SpriteBatch();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void render() {
        batch.begin();
        if (message != null) { // Check if message is not null
            // Clear the screen with a color
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f); // White color
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            font.draw(batch, message, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0, Align.center, false);
        }
        batch.end();
    }
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
