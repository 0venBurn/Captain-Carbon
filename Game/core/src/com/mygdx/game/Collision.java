package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Collision {
    private Texture spriteSheet;
    private Vector2 position;
    public Collision(boolean isPlayer) {
        if (isPlayer) {
            spriteSheet = new Texture("Tilesets/character.png");
        } else {
            spriteSheet = new Texture("Tilesets/bike.png");
        }
    }
    public boolean canMove(float x, float y, MapLayer collisionLayer) {
        Rectangle entityRect = new Rectangle(x, y, getWidth() * 0.01f, getHeight() * 0.01f);
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (entityRect.overlaps(rect)) {
                    return false;
                }
            }
        }
        return true;
    }

    private float getWidth() {
        return spriteSheet.getWidth() * 0.6f; // Adjust scale factor as needed
    }

    private float getHeight() {
        return spriteSheet.getHeight() * 0.6f; // Adjust scale factor as needed
    }



    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }
}
