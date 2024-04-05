package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Game_Animations {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    private  Animation<TextureRegion> walkLeftAnimation;
    private  Animation<TextureRegion> walkRightAnimation;
    private  Animation<TextureRegion> walkUpAnimation;
    private  Animation<TextureRegion> walkDownAnimation;
    private Texture spriteSheet;

    private Animation<TextureRegion> cycleLeftAnimation;
    private Animation<TextureRegion> cycleUpAnimation;
    private Animation<TextureRegion> cycleRightAnimation;
    private Animation<TextureRegion> cycleDownAnimation;
    private Animation<TextureRegion> movementAnimation;
    private float speed;

    private final float frameWidth = 16;
    private final float frameHeight = 16;

    private Direction currentDirection;
    private float stateTime;
    public Game_Animations() {

    }
    public void playerAnimations(TextureRegion[][] frames) {
        walkLeftAnimation = createPlayerAnimation(frames, 23);
        walkDownAnimation = createPlayerAnimation(frames, 24);
        walkUpAnimation = createPlayerAnimation(frames, 25);
        walkRightAnimation = createPlayerAnimation(frames, 26);
        currentDirection = Direction.DOWN;
        stateTime = 0f;
    }





    private Animation<TextureRegion> createPlayerAnimation(TextureRegion[][] frames, int column) {
        TextureRegion[] animationFrames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            animationFrames[i] = frames[i][column];
        }
        return new Animation<>(0.25f, animationFrames);
    }


    public TextureRegion getCurrentFramePlayer(Direction direction, boolean isMoving, float deltaTime) {
        currentDirection = direction;
        stateTime += deltaTime;

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
}