
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;



public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Skin skin;
    private MyGdxGame game;
    private SpriteBatch batch;
    private BitmapFont pauseFont;
    private boolean isPaused;
    private Stage stage;
    private Table pauseMenu;


    public GameScreen(MyGdxGame game) {
        // Initialize camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(1000, 1700, 0); // Set initial camera position
        camera.update();
        batch = new SpriteBatch();
        pauseFont = new BitmapFont();
        isPaused = false;
        this.game = game;


        // Load TMX map and create a renderer for it
        map = new TmxMapLoader().load("Game/assets/Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        AssetManager assetManager = new AssetManager();
        assetManager.load("flat-earth/skin/flat-earth-ui.atlas", TextureAtlas.class);
        assetManager.finishLoading(); // Blocks until all assets are loaded
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));


        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createPauseMenu();

    }

    private void createPauseMenu(){
        pauseMenu = new Table();

        pauseMenu.center();
        pauseMenu.pad(10);
        pauseMenu.setSize(300, 200);
        pauseMenu.defaults().width(200).height(50).pad(10);

        pauseMenu.setPosition(
                (Gdx.graphics.getWidth() - pauseMenu.getWidth()) / 2,
                (Gdx.graphics.getHeight() - pauseMenu.getHeight()) / 2

        );// Set the default size for the children


        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        pauseMenu.add(resumeButton).fillX().uniformX();
        pauseMenu.row().pad(20, 0, 20, 0);
        pauseMenu.add(exitButton).fillX().uniformX();

        stage.addActor(pauseMenu);
    }


    private Drawable createRoundedDrawable(int width, int height, int radius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        // Creating the rounded corners
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius - 1, radius, radius);
        pixmap.fillCircle(radius, height - radius - 1, radius);
        pixmap.fillCircle(width - radius - 1, height - radius - 1, radius);


        // Draw the four edges
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);


        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }

    // Usage
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        pauseMenu.setVisible(false);

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause(); {
            };
        }
        camera.update();
        renderer.setView(camera);
        renderer.render();
        batch.begin();
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (isPaused) {
            renderPauseMenu();

        }

    }
    private void renderPauseMenu(){
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);


    }

    @Override
    public void pause() {
        pauseMenu.setVisible(false);
    }

    @Override
    public void resume() {
        isPaused = false;

    }

    @Override
    public void hide() {
        pauseMenu.setVisible(false);

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        skin.dispose();
        stage.dispose();
        pauseFont.dispose();
        batch.dispose();
    }

}
