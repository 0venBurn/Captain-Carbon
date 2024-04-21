package com.mygdx.game;
// Import Necessary Modules
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.Arrays;
import com.badlogic.gdx.graphics.Color;



public class TutorialLevel implements ILevel {
    private LevelCompletionListener completionListener;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final BitmapFont font;
    private TiledMap map;
    private MapLayer collisionLayer;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private Transport currentBus = null;
    private ArrayList<Transport> transports;
    private ArrayList <Transport> bikes;
    private ArrayList <TrainStation> trainStations;
    private Stage stage;
    private Skin skin;
    private final SpriteBatch batch;
    private BitmapFont pauseFont;
    private boolean isPaused;
    private Table pauseMenu;
    private Gem gem;
    public Scoring_System scoringSystem;
    private OrthographicCamera minimapCamera;
    private Viewport minimapViewport;
    private ShapeRenderer shapeRenderer;
    private float minimapFraction;
    private Minimap minimap;


    public TutorialLevel(LevelCompletionListener listener) {
        this.completionListener = listener;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();
        camera.update();

        font = new BitmapFont();
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        font.getData().setScale(1.5f);
        map = new TmxMapLoader().load("Game/assets/Map.tmx");
        collisionLayer = map.getLayers().get("Collision");
        renderer = new OrthogonalTiledMapRenderer(map);
        transports = new ArrayList<>();
        Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(900, 50), Arrays.asList(
                new Vector2(900, 100),
                new Vector2(900, 375),
                new Vector2(900, 900)
        ));
        Transport horizontalBus = new Transport(Transport.Mode.BUS, new Vector2(810, 850), Arrays.asList(
                new Vector2(810, 850),
                new Vector2(1600, 850),
                new Vector2(2030, 850)
        ));

        minimap = new Minimap();

        // Begin batch and draw text




        upBus.setCurrentDirection(Transport.Direction.UP);
        horizontalBus.setCurrentDirection(Transport.Direction.RIGHT);
        transports.add(upBus);
        transports.add(horizontalBus);

        player = new Player(250, 150);
        spawnGem();
        // Define each train station and its coordinates
        trainStations = new ArrayList<>();
        TrainStation stationA = new TrainStation(new Vector2(2140, 920), "Station A", trainStations, player);//center map station
        TrainStation stationB = new TrainStation(new Vector2(3820, 1630), "Station B", trainStations, player);//mid right water station
        trainStations.add(stationA);
        trainStations.add(stationB);

        bikes = new ArrayList<>();
        // Spawn groups of bikes
        for (int i = 0; i < 2; i++) {
            bikes.add(new Transport(Transport.Mode.BIKE, new Vector2(230 + i * 50, 570), null));
        }

        AssetManager assetManager = new AssetManager();
        assetManager.load("flat-earth/skin/flat-earth-ui.atlas", TextureAtlas.class);
        assetManager.finishLoading(); // Blocks until all assets are loaded
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());

    }
    @Override
    public void load() {

    }

    @Override
    public void update() {

    }
    @Override
    public int getMapWidth() {
        return map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
    }

    @Override
    public int getMapHeight() {
        return map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);
    }


    @Override
    public void render() {

        Gdx.app.log("Render Method", "Start of render method");
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
        camera.update();

        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();

        Gdx.app.log("Rendering", "Rendering player");
        player.render((SpriteBatch) renderer.getBatch());

        Gdx.app.log("Rendering", "Rendering transports");
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
        Gdx.app.log("Render Method", "End of render method");


        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (Minimap.MinimapDisplayed) {
                minimap.HideMinimap(stage); // Hide the minimap
            } else {
                minimap.DisplayMinimap(stage, player); // Display the minimap
            }
        }


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

    @Override
    public Player getPlayer(){
        return player;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void checkPlayerTransportInteraction(SpriteBatch spriteBatch) {
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
                        player.setCurrentStation(station);
                        player.enterMetro();
                        station.displayStationUI(stage);
                        Gdx.input.setInputProcessor(stage);
                    }
                }
            }
        }
    }

    @Override
    public void updateBuses(float deltaTime) {
        for (Transport transport : transports) {
            boolean isActiveBus = transport == currentBus;
            transport.update(deltaTime, isActiveBus, collisionLayer);
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
//        System.out.println("minX: " + minX);
//        System.out.println("maxX: " + maxX);
//        System.out.println("minY: " + minY);
//        System.out.println("maxY: " + maxY);

        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);

        camera.update();
    }

    @Override
    public void spawnGem() {
        Vector2 gemPosition = new Vector2(4327, 1450); // tutorial spot
        gem = new Gem(gemPosition);

    }

    public Vector2 getGemPosition() {
        return gem.getPosition();
    }


}


//    public void checkEndCondition() {
//        if (/* level completion condition */) {
//            completionListener.onLevelCompleted();
//        }
//    }
