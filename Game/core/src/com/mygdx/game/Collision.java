package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;


public class Collision {
    private final Texture spriteSheet;
    public Collision(boolean isPlayer) {
        if (isPlayer) {
            spriteSheet = new Texture("Tilesets/character.png");
        } else {
            spriteSheet = new Texture("Tilesets/bike.png");
        }
    }
    public boolean canMove(float x, float y, MapLayer collisionLayer, boolean isPlayer) {
        float scaleFactor = isPlayer ? 0.1f : 0.01f;
        Rectangle entityRect = new Rectangle(x, y, getWidth() * scaleFactor, getHeight() * scaleFactor);
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
        return spriteSheet.getWidth() * 0.6f;
    }

    private float getHeight() {
        return spriteSheet.getHeight() * 0.6f;
    }



    public void dispose() {
        spriteSheet.dispose();
    }
}
