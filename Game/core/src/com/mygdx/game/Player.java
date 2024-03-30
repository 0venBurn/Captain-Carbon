package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Texture spriteSheet;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private Vector2 position;    // Current position of the player
    private float stateTime;    // Time since the player started moving
    private Direction currentDirection;
    private static final int frameWidth = 16;
    private static final int frameHeight = 16;
    private static final int framesPerMovement = 3;
    private boolean isMoving;
    private static final float playerSpeed = 10.0f; // We might want to change this later

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
        return new Animation<TextureRegion>(0.25f, animationFrames);
    }

    // Player position based on input
    public void update(float deltaTime) {
        isMoving = false;
        float moveAmount = playerSpeed * deltaTime;
        Vector2 newPosition = new Vector2(position.x, position.y);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentDirection = Direction.UP;
            newPosition.y += moveAmount;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentDirection = Direction.DOWN;
            newPosition.y -= moveAmount;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentDirection = Direction.LEFT;
            newPosition.x -= moveAmount;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentDirection = Direction.RIGHT;
            newPosition.x += moveAmount;
            isMoving = true;
        }
        if (isMoving && checkBounds(newPosition)) {
            position = newPosition;
        }
        // StateTime starts when moving
        if (isMoving) {
            stateTime += deltaTime;
        } else {
            stateTime = 0f;
        }
    }

    // Have to check if the player is on the screen
    private boolean checkBounds(Vector2 newPosition) {
        return newPosition.x >= 0 && newPosition.y >= 0 && newPosition.x <= Gdx.graphics.getWidth() && newPosition.y <= Gdx.graphics.getHeight();
    }

    public void render(SpriteBatch spriteBatch) {
        TextureRegion currentFrame = getCurrentFrame();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, position.x, position.y);
        spriteBatch.end();
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

    public void dispose() {
        spriteSheet.dispose();
    }
}