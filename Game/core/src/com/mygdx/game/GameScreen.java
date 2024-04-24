// Import necessary modules
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Set interface for the implementation of a screen and initialise variables
public class GameScreen implements Screen {


    private final LevelManager levelManager;
    public static GameState gameState;
    private Viewport viewport;
    private Stage stage;
    private final MyGdxGame game;
    private boolean isPaused;
    private Table pauseMenu;
    private Gem gem;
    public Scoring_System scoringSystem;
    private Skin skin;
    private ILevel currentLevel;
    private OrthographicCamera camera;
    private OrthographicCamera minimapCamera;
    private Viewport minimapViewport;
    private TiledMapRenderer minimapRenderer;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private int minimapWidth;
    private int minimapHeight;
    public static boolean isMinimapVisible = true;

    private SpriteBatch batch2;
    private SpriteBatch batchMinimap;
    private OrthographicCamera cameraMinimap;
    private TiledMap map;
    private TiledMapRenderer renderer2;
    private Minimap minimap;
    private SpriteBatch minimapBatch;






    public GameScreen(MyGdxGame game) {
        // Initialize camera and viewport
        levelManager = new LevelManager(game);
        levelManager.loadCurrentLevel();
        GameState gameState = com.mygdx.game.GameState.RUNNING;
      // Set initial camera position
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        stage = new Stage();
        isPaused = false;
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.game = game;


//        TiledMap minimap = new TmxMapLoader().load("Game/assets/Map.tmx");
//        minimapRenderer = new OrthogonalTiledMapRenderer(minimap, 1 / 24f);
//
//        spriteBatch = new SpriteBatch();
//
//        shapeRenderer = new ShapeRenderer();
//
//        player = levelManager.getCurrentLevel().getPlayer();
//
//

        minimapCamera = new OrthographicCamera();
        minimapCamera.zoom = 2;
        minimapBatch = new SpriteBatch();



        // Delegate rendering to the current level

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
                gameState = com.mygdx.game.GameState.RUNNING;
            }
        });
        pauseMenu.add(resumeButton).fillX().uniformX();
        pauseMenu.row().pad(20, 0, 20, 0);
        pauseMenu.add(exitButton).fillX().uniformX();


        stage.addActor(pauseMenu);
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
        levelManager.getCurrentLevel().render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
            Gdx.app.log("Game State", "Game paused: " + isPaused);
        }


        if (isPaused) {
            renderPauseMenu();
            return;

        }

        if (minimap != null && Minimap.MinimapDisplayed) {
            System.out.println("Updating minimap...................................................");
            minimap.updateMinimap(stage);
        } else {
            System.out.println("Minimap.MinimapDisplayed: " + Minimap.MinimapDisplayed + "______________________________________________________");
            System.out.println("Minimap not updated______________________________________________________");
            System.out.println("minimap: " + minimap + "______________________________________________________");
            System.out.println("Minimap.MinimapDisplayed: " + Minimap.MinimapDisplayed + "______________________________________________________");
        }

        minimapCamera.position.set(camera.position);
        minimapCamera.update();
        minimapBatch.setProjectionMatrix(minimapCamera.combined.cpy().translate(3, 3, 0));
        minimapBatch.begin();
        levelManager.loadCurrentLevel().renderMinimap(minimapBatch);
        minimapBatch.end();


//        if (isMinimapVisible) {
//            Matrix4 minimapMatrix = new Matrix4(camera.combined);
//            float minimapScale = 1 / 24f; // Scale down the minimap
//            minimapMatrix.scl(minimapScale);
//            minimapMatrix.translate(3, 3, 0); // Move the minimap to the corner of the screen
//            spriteBatch.setProjectionMatrix(minimapMatrix);
//            minimapRenderer.setView(camera);
//            minimapRenderer.render();
//
//
//
//            float minimapPlayerX = player.getX();
//            float minimapPlayerY = player.getY();
//
//            shapeRenderer.setProjectionMatrix(minimapMatrix);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle(minimapPlayerX, minimapPlayerY, 40);
//
//
//            shapeRenderer.end();
//
//            spriteBatch.setProjectionMatrix(camera.combined);
//
//        }





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
        if (isPaused) {
            gameState = com.mygdx.game.GameState.PAUSED;
            Gdx.input.setInputProcessor(stage);

        }else{
            gameState = com.mygdx.game.GameState.RUNNING;
        }


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
    private void spawnGem() {

    }

    @Override
    public void dispose() {

    }

}
