// Import necessary modules
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Arrays;

// Set interface for the implementation of a screen and initialise variables
public class GameScreen implements Screen {


    public static GameState gameState;
    public static com.mygdx.game.GameState GameState;
    private OrthographicCamera camera;
    private Viewport viewport;
    private final BitmapFont font;
    private final TiledMap map;
    private final MapLayer collisionLayer;
    private final OrthogonalTiledMapRenderer renderer;
    private final ArrayList<Transport> transports;
    private final Player player;
    private Transport currentBus = null;
    private final ArrayList <Transport> bikes;
    private final ArrayList <TrainStation> trainStations;
    private Stage stage;
    private final Skin skin;
    private final MyGdxGame game;
    private final SpriteBatch batch;
    private final BitmapFont pauseFont;
    private boolean isPaused;
    private Table pauseMenu;
    private Gem gem;
    public Scoring_System scoringSystem;

    public GameScreen(MyGdxGame game) {
        // Initialize camera and viewport
        GameState gameState = GameState.RUNNING;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(1000, 1700, 0);// Set initial camera position

        camera.update();
        batch = new SpriteBatch();
        pauseFont = new BitmapFont();
        isPaused = false;
        this.game = game;
        font = new BitmapFont();
        stage = new Stage(new ScreenViewport());


        Gdx.input.setInputProcessor((stage));
        font.getData().setScale(1.5f);



        // Load TMX map and create a renderer for it
        map = new TmxMapLoader().load("Game/assets/Map.tmx");
        collisionLayer = map.getLayers().get("Collision");
        renderer = new OrthogonalTiledMapRenderer(map);
        transports = new ArrayList<>();
        Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(1414, 1500), Arrays.asList(
                new Vector2(1414, 1500),
                new Vector2(1414, 1800),
                new Vector2(1414, 2360)
        ));
        Transport horizontalBus = new Transport(Transport.Mode.BUS, new Vector2(1300, 2300), Arrays.asList(
                new Vector2(1800, 2300),
                new Vector2(2000, 2300),
                new Vector2(2500, 2300)
        ));
        // Begin batch and draw text




        upBus.setCurrentDirection(Transport.Direction.UP);
        horizontalBus.setCurrentDirection(Transport.Direction.RIGHT);
        transports.add(upBus);
        transports.add(horizontalBus);

        player = new Player(1000, 1700);
        spawnGem();
        // Define each train station and its coordinates
        trainStations = new ArrayList<>();
        TrainStation stationA = new TrainStation(new Vector2(2650, 2350), "Station A", trainStations, player);//center map station
        TrainStation stationB = new TrainStation(new Vector2(4400, 3050), "Station B", trainStations, player);//mid right water station
        trainStations.add(stationA);
        trainStations.add(stationB);




        bikes = new ArrayList<>();
        // Spawn groups of bikes
        for (int i = 0; i < 2; i++) {
            bikes.add(new Transport(Transport.Mode.BIKE, new Vector2(800 + i * 50, 2020), null));
        }

        AssetManager assetManager = new AssetManager();
        assetManager.load("flat-earth/skin/flat-earth-ui.atlas", TextureAtlas.class);
        assetManager.finishLoading(); // Blocks until all assets are loaded
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        camera.update();

        stage = new Stage(new ScreenViewport());

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
                gameState = GameState.RUNNING;
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
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (!TrainStation.isUiDisplayed()) {
            player.updatePlayerMovement(deltaTime, collisionLayer);
        }
        updateBuses(deltaTime);

        if (player.isOnBus() && currentBus != null) {
            camera.position.set(currentBus.getPosition().x, currentBus.getPosition().y, 0);
        } else {
            camera.position.set(player.getX(), player.getY(), 0);
        }
        adjustCameraPosition();
        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();


        player.render((SpriteBatch) renderer.getBatch());

        for (Transport transport : transports) {
            transport.render((SpriteBatch) renderer.getBatch());
        }
        for (Transport bike : bikes) {
            bike.render((SpriteBatch) renderer.getBatch());
        }

        checkPlayerTransportInteraction((SpriteBatch) renderer.getBatch());

        if (gem != null && gem.isCollected() && player.getBounds().overlaps(gem.getBounds())) {
            gem.collect();
        }

        // Render the gem if it's not collected
        if (gem != null && gem.isCollected()) {
            gem.render((SpriteBatch) renderer.getBatch());
        }
        adjustCameraPosition();
        updateBuses(deltaTime);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause(); {
            }
        }



        renderer.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        adjustCameraPosition();
        renderer.setView(camera);
        batch.begin();
        font.draw(batch, "Bikes: " + player.scoringSystem.getBikeCount(), 10, Gdx.graphics.getHeight() - 30);
        font.draw(batch, "Buses: " + player.scoringSystem.getBusCount(), 10, Gdx.graphics.getHeight() - 10);

        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (isPaused) {
            renderPauseMenu();

        }

    }
    private void checkPlayerTransportInteraction(SpriteBatch spriteBatch) {
        Rectangle playerBounds = player.getBounds();

        for (Transport transport : transports) {
            Rectangle transportBounds = transport.getBounds();

            // Check for boarding the bus
            if (playerBounds.overlaps(transportBounds) && !player.isOnBus() && !transport.isFinalStopReached()) {
                font.draw(spriteBatch, "Press 'E' to enter the bus", player.getX(), player.getY() + 50);
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    player.setOnBus(true);
                    currentBus = transport; // Set the current bus to this transport
                    camera.position.set(transport.getPosition().x, transport.getPosition().y, 0); // Ensure the camera follows this bus
                }
            }

            // Display the "bus at final stop" message if applicable
            else if (playerBounds.overlaps(transportBounds) && !player.isOnBus() && transport.isFinalStopReached()) {
                font.draw(spriteBatch, "Bus at final stop!", player.getX(), player.getY() - 20);
            }

            // Check for leaving the bus
            if (player.isOnBus() && transport == currentBus && transport.canPlayerDisembark()) {
                font.draw(spriteBatch, "Press 'Q' to exit the bus", transport.getPosition().x, transport.getPosition().y + 50);
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    player.setOnBus(false);
                    player.setPosition(transport.getPosition().x - 50, transport.getPosition().y);
                    currentBus = null;
                    camera.position.set(player.getX(), player.getY(), 0); // Switch the camera back to the player
                }
            }
        }

        // Interaction with bikes
        for (Transport bike : bikes) {
            Rectangle bikeBounds = bike.getBounds();
            if (playerBounds.overlaps(bikeBounds)) {
                if (!player.isOnBike()) {
                    if (bike.hasBattery()) {
                        font.draw(spriteBatch, "Press 'E' to use the bike", player.getX(), player.getY() + 50);
                        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                            player.setOnBike(true); // Make the bike active again
                            player.mountBike(bike);
                            font.draw(spriteBatch, "", player.getX(), player.getY() + 50); // Clear the message
                        }
                    } else {
                        font.draw(spriteBatch, "No battery remaining!", player.getX(), player.getY() + 50);
                    }
                } else if (player.isOnBike() && player.getMountedBike() == bike) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                        player.dismountBike();
                    }
                }
            }

            // Interaction with train stations
            for (TrainStation station : trainStations) {
                Rectangle stationBounds = station.getBounds();
                if (playerBounds.overlaps(stationBounds)) {
                    font.draw(spriteBatch, "Press 'E' to interact with the station", player.getX(), player.getY() + 50);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        player.enterMetro();
                        station.displayStationUI(stage);
                    }
                }
            }
        }
    }
    private void renderPauseMenu(){
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void updateBuses(float deltaTime) {
        for (Transport transport : transports) {
            boolean isActiveBus = transport == currentBus;
            transport.update(deltaTime, isActiveBus, collisionLayer);
        }
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
            gameState = GameState.PAUSED;

        }else{
            gameState = GameState.RUNNING;
        }


    }
    private void adjustCameraPosition(){


        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        int mapWidth = map.getProperties().get("width", Integer.class) * tileWidth;
        int mapHeight = map.getProperties().get("height", Integer.class) * tileHeight;

        float cameraHalfWidth = camera.viewportWidth / 2f;
        float cameraHalfHeight = camera.viewportHeight / 2f;

        float minX = cameraHalfWidth;
        float maxX = mapWidth - cameraHalfWidth;
        float minY = cameraHalfHeight;
        float maxY = mapHeight - cameraHalfHeight;

        // Debug print statements
        System.out.println("minX: " + minX);
        System.out.println("maxX: " + maxX);
        System.out.println("minY: " + minY);
        System.out.println("maxY: " + maxY);

        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);

        System.out.println("Camera position: " + camera.position);
        System.out.println("Camera viewport width: " + camera.viewportWidth);
        System.out.println("Camera viewport height: " + camera.viewportHeight);
        System.out.println("Viewport world width: " + viewport.getWorldWidth());
        System.out.println("Viewport world height: " + viewport.getWorldHeight());
        camera.update();
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
        Vector2 gemPosition = new Vector2(4830, 2900); // tutorial spot
        gem = new Gem(gemPosition);
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
