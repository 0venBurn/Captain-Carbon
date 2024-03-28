// OpenGL texture loader to load the texture from the file
import com.badlogic.gdx.graphics.Texture;
// OpenGL texture region to manage the texture region
import com.badlogic.gdx.graphics.g2d.TextureRegion;
// OpenGL animation to manage the animation
import com.badlogic.gdx.graphics.g2d.Animation;
// OpenGL vector2 to manage the 2D vector
import com.badlogic.gdx.math.Vector2;

public class Player {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    private Texture spriteSheet;
    private Animation<TextureRegion> WalkupAnimation;
    private Animation<TextureRegion> WalkdownAnimation;
    private Animation<TextureRegion> WalkleftAnimation;
    private Animation<TextureRegion> WalkrightAnimation;
    // Current position of the player
    private Vector2 position;
    // Time since the player started moving
    private float stateTime;
    // Current direction of the player
    private Direction currentDirection;
    private static final int frameWidth = 16;
    private static final int frameHeight = 16;
    private static final int framesPerMovement = 3;
    // the character sprite sheet starts at 24 frames in
    private static final int startFrameIndex = 23;
    private static final int startX = frameWidth * startFrameIndex;
        public Player(float x, float y) {
            // Load the sprite sheet
            spriteSheet = new Texture("Tilesets/character.png");

            int leftX = startX;
            int downX = leftX + frameWidth;
            int upX = downX + frameWidth;
            int rightX = upX + frameWidth;

            TextureRegion[][] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

            walkLeftAnimation = createAnimation(frames, leftX, 0);
            walkDownAnimation = createAnimation(frames, downX, 0);
            walkUpAnimation = createAnimation(frames, upX, 0);
            walkRightAnimation = createAnimation(frames, rightX, 0);

            // Initialisations
            position = new Vector2(x, y);
            stateTime = 0f;
            curDirection = Direction.DOWN;

            //constructor end
        }
        // Extract the animations from the sprite sheet
        private Animation<TextureRegion> createAnimation(TextureRegion[][] frames, int column, int startY) {
            TextureRegion[] animationFrames = new TextureRegion[framesPerMovement];
            for (int i = 0; i < framesPerMovement; i++) {
                animationFrames[i] = frames[startY + i][column];
            }
            return new Animation<TextureRegion>(0.25f, animationFrames);

}

