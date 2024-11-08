// Import necessary modules
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Set interface for the implementation of a screen and initialise variables
public class GameScreen implements Screen {


    private final LevelManager levelManager;
    public static GameState gameState;
    private Viewport viewport;
    private Stage stage;
    private final MyGdxGame game;
    private boolean isPaused;
    private Table pauseMenu;

    private Skin skin;
    private ILevel currentLevel;
    private OrthographicCamera camera;
    private Minimap minimap;
    private Player player;
    private Stage minimapStage;
    public Scoring_System scoringSystem;



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
        scoringSystem = Scoring_System.getInstance();


        // Delegate rendering to the current level

        minimap = new Minimap();
        minimapStage = new Stage();

        String levelName = null;
        if (levelManager.getCurrentLevel() instanceof TutorialLevel) {
            levelName = "TutorialLevel";
        } else if (levelManager.getCurrentLevel() instanceof LevelOne) {
            levelName = "LevelOne";
        } else if (levelManager.getCurrentLevel() instanceof LevelTwo) {
            levelName = "LevelTwo";
        }



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
                scoringSystem.reset();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (Minimap.MinimapDisplayed) {
                minimap.HideMinimap(minimapStage); // Hide the minimap
            } else {
                minimap.DisplayMinimap(
                        minimapStage,
                        player,
                        levelManager.getCurrentLevel().getGemPosition(),
                        levelManager.getCurrentLevelIndex());
            }
        }

        System.out.println(levelManager.getCurrentLevelIndex());




        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
            Gdx.app.log("Game State", "Game paused: " + isPaused);
        }


        if (isPaused) {
            renderPauseMenu();
            return;

        }
        levelManager.getCurrentLevel().render();


//        if (minimap != null && Minimap.MinimapDisplayed) {
//            System.out.println("Updating minimap...................................................");
//            minimap.updateMinimap(stage);
//        } else {
//            System.out.println("Minimap not updated______________________________________________________");
//            System.out.println("minimap: " + minimap + "______________________________________________________");
//            System.out.println("Minimap.MinimapDisplayed: " + Minimap.MinimapDisplayed + "______________________________________________________");
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