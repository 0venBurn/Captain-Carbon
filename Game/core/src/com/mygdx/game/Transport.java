package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;


public class Transport {
    public enum Direction {UP, DOWN, LEFT, RIGHT}
    public enum Mode {BUS, TRAIN, BIKE}

    private Texture spriteSheet;
    private Vector2 position;
    private float stateTime;
    private Direction currentDirection = Direction.UP;
    private boolean isMoving;
    private float speed;
    private Mode mode;

    // Bus
    private TextureRegion busFrameLeft;
    private TextureRegion busFrameRight;
    private TextureRegion busFrameUp;
    private TextureRegion busFrameDown;
    private List<Vector2> waypoints;
    private int currentWaypointIndex = 0;
    private boolean waitingAtWaypoint = true;
    // Bike
    private Animation<TextureRegion> cycleLeftAnimation;
    private Animation<TextureRegion> cycleUpAnimation;
    private Animation<TextureRegion> cycleRightAnimation;
    private Animation<TextureRegion> cycleDownAnimation;
    private Animation<TextureRegion> movementAnimation;
    private TiledMap map;
    private boolean isFinalStopReached = false;
    private boolean canLeaveBus = false;
    public Transport(Mode mode, Vector2 startPosition, TiledMap map, List<Vector2> waypoints) {
        this.mode = mode;
        this.position = startPosition;
        this.map = map;
        this.waypoints = waypoints;
        configureMode();
    }
    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

    private void configureMode() {
        switch (mode) {
            case BUS:
                configureBusMode();
                break;
            case BIKE:
                configureBikeMode();
                break;
        }
    }

    private void configureBusMode() {
        spriteSheet = new Texture("Tilesets/bus.png");
        speed = 90.0f;
        busFrameRight = new TextureRegion(spriteSheet, 8, 0, 79, 48);
        busFrameLeft = new TextureRegion(spriteSheet, 16, 56, 80, 56);
        busFrameUp = new TextureRegion(spriteSheet, 96, 72, 40, 72);
        busFrameDown = new TextureRegion(spriteSheet, 104, 0, 40, 72);
    }

    private void configureBikeMode() {
        spriteSheet = new Texture("Tilesets/bike.png");
        speed = 15.0f;
        TextureRegion[][] bikeFrames = TextureRegion.split(spriteSheet, 32, 32);
        cycleLeftAnimation = createAnimation(bikeFrames, 0, 0, 7);
        cycleUpAnimation = createAnimation(bikeFrames, 2, 0, 7);
        cycleRightAnimation = createAnimation(bikeFrames, 4, 0, 7);
        cycleDownAnimation = createAnimation(bikeFrames, 6, 0, 7);
        movementAnimation = cycleUpAnimation;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[][] frames, int row, int startColumn, int frameCount) {
        TextureRegion[] animationFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            animationFrames[i] = frames[row][startColumn + i];
        }
        return new Animation<>(0.1f, animationFrames);
    }

    public void update(float deltaTime, boolean isActiveBus) {
        stateTime += deltaTime;
        if (mode == Mode.BUS && isActiveBus) {
            updateBus(deltaTime);
        } else if (mode == Mode.BIKE) {
            updateBike(deltaTime);
        }
    }

    private void updateBus(float deltaTime) {
        if (!waitingAtWaypoint && !waypoints.isEmpty()) {
            Vector2 nextWaypoint = waypoints.get(currentWaypointIndex);
            Vector2 moveVector = new Vector2(nextWaypoint).sub(position);
            float distance = speed * deltaTime;

            if (position.dst2(nextWaypoint) > distance * distance) {
                position.mulAdd(moveVector.nor(), distance);
                // Determine direction based on movement
                if (moveVector.x > 0) {
                    currentDirection = Direction.RIGHT;
                } else if (moveVector.x < 0) {
                    currentDirection = Direction.LEFT;
                }
                canLeaveBus = false;
            } else {
                position.set(nextWaypoint);
                waitingAtWaypoint = true;
                canLeaveBus = true;
                if (currentWaypointIndex == waypoints.size() - 1) {
                    isFinalStopReached = true;
                }
            }
        }

        if (waitingAtWaypoint && Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentWaypointIndex < waypoints.size() - 1) {
            waitingAtWaypoint = false;
            currentWaypointIndex++;
            canLeaveBus = false;
        }
    }

    private void updateBike(float deltaTime) {
        Vector2 moveVector = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveVector.y += speed * deltaTime;
            currentDirection = Direction.UP;
            movementAnimation = cycleUpAnimation;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveVector.y -= speed * deltaTime;
            currentDirection = Direction.DOWN;
            movementAnimation = cycleDownAnimation;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveVector.x -= speed * deltaTime;
            currentDirection = Direction.LEFT;
            movementAnimation = cycleLeftAnimation;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveVector.x += speed * deltaTime;
            currentDirection = Direction.RIGHT;
            movementAnimation = cycleRightAnimation;
        }
        position.add(moveVector);
        isMoving = !moveVector.isZero();
    }

    public void render(SpriteBatch spriteBatch) {
        TextureRegion currentFrame = null;
        switch (mode) {
            // Bus mode uses individual frames cause we have no animation
            case BUS:
                switch (currentDirection) {
                    case LEFT:
                        currentFrame = busFrameLeft;
                        break;
                    case RIGHT:
                        currentFrame = busFrameRight;
                        break;
                    case UP:
                        currentFrame = busFrameUp;
                        break;
                    case DOWN:
                        currentFrame = busFrameDown;
                        break;
                }
                break;
            case BIKE:
                // Bike mode uses an animation
                currentFrame = movementAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (currentFrame != null) {
            spriteBatch.draw(currentFrame, position.x, position.y);
        }
    }
    // In your Transport class
    public boolean isFinalStopReached() {
        return isFinalStopReached;
    }

    public void setFinalStopReached(boolean isFinalStopReached) {
        this.isFinalStopReached = isFinalStopReached;
    }

    public boolean canPlayerDisembark() {
        return canLeaveBus || isFinalStopReached;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, spriteSheet.getWidth() * 0.6f, spriteSheet.getHeight() * 0.6f);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        if (spriteSheet != null) spriteSheet.dispose();
    }
}