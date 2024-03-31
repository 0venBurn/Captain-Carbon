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
    private int currentWaypointIndex;
    private boolean waitingAtWaypoint;
    // Bike
    private Animation<TextureRegion> cycleLeftAnimation;
    private Animation<TextureRegion> cycleUpAnimation;
    private Animation<TextureRegion> cycleRightAnimation;
    private Animation<TextureRegion> cycleDownAnimation;
    private Animation<TextureRegion> movementAnimation;
    private TiledMap map;

    public Transport(Mode mode, float x, float y, TiledMap map) {
        this.mode = mode;
        position = new Vector2(x, y);
        stateTime = 0f;
        isMoving = false;
        this.map = map;
        configureMode();
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
        speed = 20.0f;
        busFrameRight = new TextureRegion(spriteSheet, 8, 0, 80, 56);
        busFrameLeft = new TextureRegion(spriteSheet, 16, 56, 80, 56);
        busFrameUp = new TextureRegion(spriteSheet, 96, 72, 40, 72);
        busFrameDown = new TextureRegion(spriteSheet, 104, 0, 40, 72);
        initializeBusRoute();
    }

    private void initializeBusRoute() {
        waypoints = new ArrayList<>();

        // Waypoints here still need to figure these out a bit
        waypoints.add(new Vector2(108, -44));
        waypoints.add(new Vector2(110, -150));

        if (waypoints.isEmpty()) {
            throw new IllegalStateException("No waypoints found");
        }
        currentWaypointIndex = 0;
        waitingAtWaypoint = true;
    }
    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (mode == Mode.BIKE) {
            updateBike(deltaTime);
        } else if (mode == Mode.BUS) {
            updateBus(deltaTime);
        }
    }
    private void updateBus(float deltaTime) {
        if (waitingAtWaypoint) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                waitingAtWaypoint = false;
            }
        } else {
            Vector2 currentWaypoint = waypoints.get(currentWaypointIndex);
            Vector2 direction = new Vector2(currentWaypoint).sub(position).nor();
            float distance = speed * deltaTime;

            if (position.dst2(currentWaypoint) > distance * distance) {
                position.mulAdd(direction, distance);
            } else {
                position.set(currentWaypoint);
                waitingAtWaypoint = true;
                currentWaypointIndex = (currentWaypointIndex + 1) % waypoints.size();
            }
        }
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
    public void dispose() {
        if (spriteSheet != null) spriteSheet.dispose();
    }
}