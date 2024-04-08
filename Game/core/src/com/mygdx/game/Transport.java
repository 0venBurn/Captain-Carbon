package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Transport {
    public enum Direction {UP, DOWN, LEFT, RIGHT}
    public enum Mode {BUS, TRAIN, BIKE}
    private Texture spriteSheet;
    public Vector2 position;
    private float stateTime;
    private Direction currentDirection = Direction.UP;
    private float speed;
    private final Mode mode;

    // Bus
    private TextureRegion busFrameLeft;
    private TextureRegion busFrameRight;
    private TextureRegion busFrameUp;
    private TextureRegion busFrameDown;
    private final List<Vector2> waypoints;
    private int currentWaypointIndex = 0;
    private boolean waitingAtWaypoint = true;


    // Bike
    private Animation<TextureRegion> cycleLeftAnimation;
    private Animation<TextureRegion> cycleUpAnimation;
    private Animation<TextureRegion> cycleRightAnimation;
    private Animation<TextureRegion> cycleDownAnimation;
    private Animation<TextureRegion> movementAnimation;
    private boolean isFinalStopReached = false;
    private boolean canLeaveBus = false;
    private float batteryCharge;

    public Transport(Mode mode, Vector2 startPosition, List<Vector2> waypoints) {
        this.mode = mode;
        this.position = startPosition;
        this.waypoints = waypoints;
        this.batteryCharge = 100.0f;
        configureMode();
    }

    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
        switch (direction) {
            case UP:
                movementAnimation = cycleUpAnimation;
                break;
            case DOWN:
                movementAnimation = cycleDownAnimation;
                break;
            case LEFT:
                movementAnimation = cycleLeftAnimation;
                break;
            case RIGHT:
                movementAnimation = cycleRightAnimation;
                break;
        }
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
        speed = 500.0f;
        TextureRegion[][] bikeFrames = TextureRegion.split(spriteSheet, 32, 32);
        cycleLeftAnimation = createAnimation(bikeFrames, 0);
        cycleUpAnimation = createAnimation(bikeFrames, 2);
        cycleRightAnimation = createAnimation(bikeFrames, 4);
        cycleDownAnimation = createAnimation(bikeFrames, 6);
        movementAnimation = cycleUpAnimation;
        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[][] frames, int row) {
        TextureRegion[] animationFrames = new TextureRegion[7];
        System.arraycopy(frames[row], 0, animationFrames, 0, 7);
        return new Animation<>(0.1f, animationFrames);
    }

    public void update(float deltaTime, boolean isActiveBus, MapLayer collisionLayer) {
        if (GameScreen.gameState == GameScreen.GameState.PAUSED){
            return;
        }
        stateTime += deltaTime;

        if (mode == Mode.BUS && isActiveBus) {
            updateBus(deltaTime);
        } else if (mode == Mode.BIKE) {
            updateBikeMovement(deltaTime, collisionLayer);
        } else if (mode == Mode.TRAIN) {
            //updateTrain();
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

    private void updateBikeMovement(float deltaTime, MapLayer collisionLayer) {
        Vector2 moveVector = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveVector.y += speed * deltaTime;
            if (currentDirection != Direction.UP) {
                setCurrentDirection(Direction.UP);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveVector.y -= speed * deltaTime;
            if (currentDirection != Direction.DOWN) {
                setCurrentDirection(Direction.DOWN);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveVector.x -= speed * deltaTime;
            if (currentDirection != Direction.LEFT) {
                setCurrentDirection(Direction.LEFT);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveVector.x += speed * deltaTime;
            if (currentDirection != Direction.RIGHT) {
                setCurrentDirection(Direction.RIGHT);
            }
        }

        // Calculate the new position to move to
        Vector2 newPosition = position.cpy().add(moveVector);
        if (this.canMove(newPosition.x, newPosition.y, collisionLayer)) {
            position.set(newPosition);
            batteryCharge -= deltaTime * 10;
        }
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
                currentFrame = getCurrentFrameBike();
                break;
        }
        if (currentFrame != null) {
            spriteBatch.draw(currentFrame, position.x, position.y);
        }
    }

    public boolean canMove(float x, float y, MapLayer collisionLayer) {
        Rectangle transportRect = new Rectangle(x, y, this.getWidth() * .01f, this.getHeight()* .01f);

        // Iterate through all objects in the collision layer to check for collisions
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (transportRect.overlaps(rect)) {
                    // Collision detected
                    return false;
                }
            }
        }
        return true;
    }

    public TextureRegion getCurrentFrameBike() {
        if (mode == Mode.BIKE) {
            return movementAnimation.getKeyFrame(stateTime, true);
        }
        return null;
    }

    public void setActive(boolean active) {
    }

    public boolean hasBattery() {
        return batteryCharge > 0;
    }

    private float getWidth() {
        return spriteSheet.getWidth() * 0.6f;
    }
    private float getHeight() {
        return spriteSheet.getHeight() * 0.6f;
    }

    public void setVisible() {
    }

    public boolean isFinalStopReached() {
        return isFinalStopReached;
    }
    public void setPosition(float x, float y) {
        position.set(x, y);
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