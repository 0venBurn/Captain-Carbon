package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static com.mygdx.game.EducationalPopup.TransportMode;

public class Player {
    private final Texture spriteSheet;
    private final Game_Animations animations;
    private final Vector2 position;
    private Game_Animations.Direction currentDirection;

    private static final int frameWidth = 16, frameHeight = 16;
    private boolean isMoving;
    private static final float playerSpeed = 600.0f;
    private boolean onBus = false,onBike = false;
    private Transport currentBike;
    private float totalPlayerDistanceTraveled = 0.0f;
    private float totalBikeDistanceTraveled = 0.0f;

    private Transport mountedBike;
    public Scoring_System scoringSystem;
    public EducationalPopup popup;
    private Collision playercollision   ;

    private TrainStation currentStation;

    public Player(float x, float y) {
        spriteSheet = new Texture("Tilesets/character.png");
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        animations = new Game_Animations();
        animations.playerAnimations(frames);
        position = new Vector2(x, y);
        currentDirection = Game_Animations.Direction.DOWN;
        isMoving = false;
        playercollision = new Collision(true);
        scoringSystem = Scoring_System.getInstance();
        popup = new EducationalPopup();

    }

    public void updatePlayerMovement(float deltaTime, MapLayer collisionLayer) {
        if (GameScreen.gameState == GameState.PAUSED) {
            return;
        }
        Vector2 oldPosition = new Vector2(position);
        Vector2 newPosition = new Vector2(position);

        if (!onBus) {
            if (onBike && currentBike != null) {
                currentBike.update(deltaTime, false, collisionLayer);
                if (!currentBike.hasBattery()) {
                    dismountBike(); // Dismount if the bike runs out of battery
                } else {
                    position.set(currentBike.getPosition());
                    totalBikeDistanceTraveled += oldPosition.dst(position);
                    scoringSystem.setTotalBikeDistanceTraveled(totalBikeDistanceTraveled);
                }
            } else {
                isMoving = false;
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    newPosition.y += playerSpeed * deltaTime;
                    currentDirection = Game_Animations.Direction.UP;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    newPosition.y -= playerSpeed * deltaTime;
                    currentDirection = Game_Animations.Direction.DOWN;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    newPosition.x -= playerSpeed * deltaTime;
                    currentDirection = Game_Animations.Direction.LEFT;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    newPosition.x += playerSpeed * deltaTime;
                    currentDirection = Game_Animations.Direction.RIGHT;
                    isMoving = true;
                }

                if (playercollision.canMove(newPosition.x, newPosition.y, collisionLayer, true)) {
                    position.set(newPosition);
                    totalPlayerDistanceTraveled += oldPosition.dst(newPosition);
                    scoringSystem.setTotalPlayerDistanceTraveled(totalPlayerDistanceTraveled);
                }
            }
        }
    }

  
    public void setPopupCamera(OrthographicCamera camera) {
        this.popup.setCamera(camera);
    }

  
    public void render(SpriteBatch spriteBatch) {
        TextureRegion currentFrame;
        if (!onBus) {
            if (onBike && currentBike != null) {
                currentFrame = currentBike.getCurrentFrameBike();
            } else {
                currentFrame = animations.getCurrentFramePlayer(currentDirection, isMoving, Gdx.graphics.getDeltaTime());
            }
            spriteBatch.draw(currentFrame, position.x, position.y);
        }
        // all the popup drawing logic is encapsulated here
        popup.draw(spriteBatch);
    }





    public void mountBike(Transport bike) {
        mountedBike = bike;
        this.onBike = true;
        this.currentBike = bike;
        bike.setActive(true);
        bike.setVisible();
        // Increment bike count
    }

    public Transport getMountedBike() {
        return mountedBike;
    }

    public void dismountBike() {
        if (onBike && currentBike != null) {
            this.mountedBike = null;
            this.currentBike.setActive(false);
            this.currentBike.setVisible();
            this.currentBike.setPosition(position.x + 10, position.y);
            this.onBike = false;
            this.currentBike = null;
            popup.show(TransportMode.BIKE, 3);
        }
    }

    public void setOnBus(boolean onBus) {
        this.onBus = onBus;
        if (!this.onBus) {
            popup.show(TransportMode.BUS, 3);
        }
    }

    public void setCurrentStation(TrainStation station) {
        this.currentStation  = station;
    }

    public TrainStation getCurrentStation() {
        return currentStation ;
    }

    public void setOnBike(boolean onBike) {
        this.onBike = onBike;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public boolean isOnBus() {

        return onBus;
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

    public void enterMetro() {
    }

    public void exitMetro(Vector2 newPosition) {
        setPosition(newPosition.x, newPosition.y);
        popup.show(TransportMode.METRO, 3);
    }

    public boolean isOnBike() {
        return onBike;
    }
}