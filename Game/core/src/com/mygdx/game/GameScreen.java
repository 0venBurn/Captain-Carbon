
package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Arrays;


public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private TiledMap map;
    private MapLayer collisionLayer;
    private OrthogonalTiledMapRenderer renderer;
    private ArrayList<Transport> transports;
    private Player player;
    private Transport currentBus = null;

    private ArrayList <Transport> bikes;

    private ArrayList <TrainStation> trainStations;
    private Stage stage;

    private Skin skin;
    private MyGdxGame game;
    private SpriteBatch batch;
    private BitmapFont pauseFont;
    private boolean isPaused;
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
        font = new BitmapFont();
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor((stage));
        font.getData().setScale(1.5f);


        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.update();
        // Load TMX map and create a renderer for it
        map = new TmxMapLoader().load("Game/assets/Map.tmx");
        collisionLayer = map.getLayers().get("Collision");
        renderer = new OrthogonalTiledMapRenderer(map);
        transports = new ArrayList<>();
        Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(1414, 1500),  Arrays.asList(
                new Vector2(1414, 1500),
                new Vector2(1414, 1800),
                new Vector2(1414, 2200),
                new Vector2(1414, 3580)
        ));
        Transport upBus2 = new Transport(Transport.Mode.BUS, new Vector2(1414, 2000), Arrays.asList(
                new Vector2(1414, 2400),
                new Vector2(1414, 2700),
                new Vector2(1414, 3000)
        ));
        Transport horizontalBus = new Transport(Transport.Mode.BUS, new Vector2(1300, 2300), Arrays.asList(
                new Vector2(1800, 2300),
                new Vector2(2000, 2300),
                new Vector2(2500, 2300)
        ));

        player = new Player(1000, 1700);

// Define each train station and its coordinates
        trainStations = new ArrayList<>();
        TrainStation station1 = new TrainStation(new Vector2(1000, 2000), "Station A", trainStations, player);
        TrainStation station2 = new TrainStation(new Vector2(2500, 2500), "Station B", trainStations, player);
        TrainStation station3 = new TrainStation(new Vector2(3000, 3000), "Station C", trainStations, player);
        TrainStation station4 = new TrainStation(new Vector2(3500, 3500), "Station D", trainStations, player);
        trainStations.add(station1);
        trainStations.add(station2);
        trainStations.add(station3);
        trainStations.add(station4);
        upBus.setCurrentDirection(Transport.Direction.UP);
        upBus2.setCurrentDirection(Transport.Direction.UP);
        horizontalBus.setCurrentDirection(Transport.Direction.RIGHT); // Set initial direction to right

        transports.add(upBus);
        transports.add(upBus2);
        transports.add(horizontalBus);


        bikes = new ArrayList<>();
        // Spawn groups of bikes
        for (int i = 0; i < 5; i++) {
            bikes.add(new Transport(Transport.Mode.BIKE, new Vector2(2400 + i * 50, 2300), null));
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
            player.update(deltaTime, collisionLayer);
        }
        // Update the camera's position
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        // Getting the dimensions of the map in pixels
        int mapWidthInPixels = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        int mapHeightInPixels = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);

        // Ensuring the camera's position is within the bounds
        camera.position.x = Math.max(camera.position.x, camera.viewportWidth / 2);
        camera.position.y = Math.max(camera.position.y, camera.viewportHeight / 2);
        camera.position.x = Math.min(camera.position.x, mapWidthInPixels - camera.viewportWidth / 2);
        camera.position.y = Math.min(camera.position.y, mapHeightInPixels - camera.viewportHeight / 2);

        camera.update();
        updateBuses(deltaTime);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause(); {
            };
        }
        if (player.isOnBus() && currentBus != null) {
            camera.position.set(currentBus.getPosition().x, currentBus.getPosition().y, 0);
        } else {
            camera.position.set(player.getX(), player.getY(), 0);
        }

        camera.update();
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
        renderer.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
