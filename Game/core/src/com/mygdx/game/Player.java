package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

public class Player {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private final Texture spriteSheet;
    private final Animation<TextureRegion> walkLeftAnimation;
    private final Animation<TextureRegion> walkDownAnimation;
    private final Animation<TextureRegion> walkUpAnimation;
    private final Animation<TextureRegion> walkRightAnimation;
    private Vector2 position;    // Current position of the player
    private float stateTime;    // Time since the player started moving
    private Direction currentDirection;
    private static final int frameWidth = 16;
    private static final int frameHeight = 16;
    private static final int framesPerMovement = 3;
    private boolean isMoving;
    private static final float playerSpeed = 690.0f; // We will want to change this later
    private boolean onBus = false;

    public Player(float x, float y) {
        spriteSheet = new Texture("Tilesets/character.png");

        TextureRegion[][] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        // 24th to 27th columns for directions
        walkLeftAnimation = createAnimation(frames, 23);
        walkDownAnimation = createAnimation(frames, 24);
        walkUpAnimation = createAnimation(frames, 25);
        walkRightAnimation = createAnimation(frames, 26);

        // Initialize player (x, y) & stateTime to 0
        position = new Vector2(x, y);
        stateTime = 0f;
        currentDirection = Direction.DOWN;
        isMoving = false;
    }

    // Extracts the animations from the sprite sheet
    private Animation<TextureRegion> createAnimation(TextureRegion[][] frames, int column) {
        TextureRegion[] animationFrames = new TextureRegion[framesPerMovement];
        for (int i = 0; i < framesPerMovement; i++) {
            animationFrames[i] = frames[i][column];
        }
        return new Animation<>(0.25f, animationFrames);
    }

    // Player position based on input
    public void update(float deltaTime, MapLayer collisionLayer) {
        if (!onBus) {
            isMoving = false;
            Vector2 newPosition = new Vector2(position);

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                newPosition.y += playerSpeed * deltaTime;
                currentDirection = Direction.UP;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                newPosition.y -= playerSpeed * deltaTime;
                currentDirection = Direction.DOWN;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                newPosition.x -= playerSpeed * deltaTime;
                currentDirection = Direction.LEFT;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                newPosition.x += playerSpeed * deltaTime;
                currentDirection = Direction.RIGHT;
                isMoving = true;
            }
            if (canMove(newPosition.x, newPosition.y, collisionLayer)) {
                position.set(newPosition);
            }
            stateTime = isMoving ? stateTime + deltaTime : 0;
        }
    }

    public boolean canMove(float x, float y, MapLayer collisionLayer) {
        Rectangle playerRect = new Rectangle(x, y, getWidth() * .010f, getHeight() * .010f);
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (playerRect.overlaps(rect)) {
                    return false;
                }
            }
        }
        return true;
    }
    public float getWidth() {
        return spriteSheet.getWidth() * .10f;
    }

    public float getHeight() {
        return spriteSheet.getHeight() * .10f;
    }
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, spriteSheet.getWidth() * .10f, spriteSheet.getHeight() * .10f);
    }


    public void render(SpriteBatch spriteBatch) {
        if (!onBus) {
            TextureRegion currentFrame = getCurrentFrame();
            spriteBatch.draw(currentFrame, position.x, position.y);
        }
    }

    private TextureRegion getCurrentFrame() {
        switch (currentDirection) {
            case UP:
                return isMoving ? walkUpAnimation.getKeyFrame(stateTime, true) : walkUpAnimation.getKeyFrames()[0];
            case DOWN:
                return isMoving ? walkDownAnimation.getKeyFrame(stateTime, true) : walkDownAnimation.getKeyFrames()[0];
            case LEFT:
                return isMoving ? walkLeftAnimation.getKeyFrame(stateTime, true) : walkLeftAnimation.getKeyFrames()[0];
            case RIGHT:
                return isMoving ? walkRightAnimation.getKeyFrame(stateTime, true) : walkRightAnimation.getKeyFrames()[0];
            default:
                return walkDownAnimation.getKeyFrames()[0];
        }
    }
    public void setOnBus(boolean onBus) {
        this.onBus = onBus;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public boolean isOnBus() {
        return onBus;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}