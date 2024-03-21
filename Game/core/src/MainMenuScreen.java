import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenuScreen implements Screen {
        //  Create reference to game object
        private OurGame game;
        //    Create a SpriteBatch, BitmapFont, and Stage
        private SpriteBatch batch;
        private BitmapFont font;
        private Stage stage;
        private Texture background;
//    Create TextButtons for play and quit option
    private TextButton playButton;
    private TextButton quitButton;

    public MainMenuScreen(OurGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage();
        background = new Texture(Gdx.files.internal("MainMenuBackground.jpg"));
        Gdx.input.setInputProcessor(stage); // Set the stage to process input


//      White font color for button font
        Color fontColor = new Color(1, 1, 1, 1); // White colour

//        Create a new drawable object for the button
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

//      Create a TextButton Style
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = fontColor;
        textButtonStyle.up = drawable;

//        Create the play button
        playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 + 50);
        playButton.setSize(150, 50);

//        Add a listener to the play button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                Change to the game screen
//                game.setScreen(new GameScreen(game));
            }
        });

//        Create the quit button
        quitButton = new TextButton("Quit", textButtonStyle);
        quitButton.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - 50);
        quitButton.setSize(150, 50);
//        Add a listener to the quit button
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                Quit the game
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
//        Method to show the current screen from Screen interface
        stage.addActor(playButton);
        stage.addActor(quitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Black Background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Press to Play!", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
//        Main menu so no need to pause
    }

    @Override
    public void resume() {
//        Main menu so no need to resume

    }

    @Override
    public void hide() {
//        Main menu so no need to hide

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}