package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gem {
    private final Vector2 position;
    private final Texture texture;
    private boolean isCollected;

    public Gem(Vector2 position) {
        this.position = position;
        this.texture = new Texture("Tilesets/gem.png");
        this.isCollected = false;
    }
    public Vector2 getPosition() {
        return position;
    }
    public void render(SpriteBatch spriteBatch) {
        if (!isCollected) {
            spriteBatch.draw(texture, position.x, position.y);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }
    public boolean isCollected() {
        return !isCollected;
    }
    public void collect() {
        isCollected = true;
    }
}