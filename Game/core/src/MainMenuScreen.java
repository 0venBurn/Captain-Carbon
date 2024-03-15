import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MainMenuScreen implements Screen {
    private OurGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private TextButton playButton;
    private TextButton quitButton;

    public MainMenuScreen(OurGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Set the stage to process input

//      Create a TextButton Style
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;

//        Create the play button
        playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

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
        quitButton.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - 100);

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

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}