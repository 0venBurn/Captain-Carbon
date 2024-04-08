package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Collision {
    private Texture spriteSheet;
    public Vector2 position;
    public Collision() {
        // Initialize the sprite sheet
        spriteSheet = new Texture("Tilesets/bike.png");
    }
    public boolean canMove(float x, float y, MapLayer collisionLayer) {
        Rectangle transportRect = new Rectangle(x, y, this.getWidth() * .01f, this.getHeight()* .01f);
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (transportRect.overlaps(rect)) {
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



    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, spriteSheet.getWidth() * 0.6f, spriteSheet.getHeight() * 0.6f);
    }

}
