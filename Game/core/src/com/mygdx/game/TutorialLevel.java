package com.mygdx.game;
// Import Necessary Modules
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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



public class TutorialLevel implements ILevel {
    private LevelCompletionListener completionListener;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final BitmapFont font;
    private BitmapFont tutorialFont;
    private TiledMap map;
    private MapLayer collisionLayer;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private Transport currentBus = null;
    private ArrayList<Transport> transports,bikes;
    private ArrayList <TrainStation> trainStations;
    private Stage stage;
    private Skin skin;
    private final SpriteBatch batch;
    private BitmapFont pauseFont;
    private Gem gem;
    private BitmapFont co2BarFont,timeBarFont;
    public Scoring_System scoringSystem;
    private TheProgressBars timeBar, co2Bar;
    private float co2BarValue,timeBarValue;
    private Minimap minimap;


    public TutorialLevel(LevelCompletionListener listener) {
        this.completionListener = listener;
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();
        camera.update();
        scoringSystem = Scoring_System.getInstance();
        font = new BitmapFont();
        tutorialFont = new BitmapFont();
        tutorialFont.getData().setScale(3f);
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        font.getData().setScale(1.5f);
        map = new TmxMapLoader().load("Game/assets/Map.tmx");
        collisionLayer = map.getLayers().get("Collision");
        renderer = new OrthogonalTiledMapRenderer(map);
        transports = new ArrayList<>(BusStopLocations.defineBusLocations(BusStopLocations.Level.TUTORIAL));
      
        player = new Player(100, 150);
        player.setPopupCamera(camera);

        minimap = new Minimap();

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



        batch = new SpriteBatch();


        timeBar = new TheProgressBars(skin);
        timeBarFont = new BitmapFont();


        co2Bar = new TheProgressBars(skin);

        co2Bar.getProgressBar().setY(timeBar.getProgressBar().getY() - timeBar.getProgressBar().getHeight() - 10);
        co2BarFont = new BitmapFont();



        stage.addActor(timeBar.getProgressBar());
        stage.addActor(co2Bar.getProgressBar());

    }
    @Override
    public void load() {

    }

    @Override
    public void update() {

    }


    @Override
    public void render() {
        checkEndCondition();


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

        renderer.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        adjustCameraPosition();
        renderer.setView(camera);
        batch.begin();
        co2BarValue = 6000 - scoringSystem.calculateTotalCarbonEmissions();
        co2Bar.setValue(co2BarValue);
        timeBarValue = 6000 - scoringSystem.calculateTotalTime();
        timeBar.setValue(timeBarValue);
        timeBar.render();
        co2Bar.render();
        font.draw(batch, "Time Bar" , 10, Gdx.graphics.getHeight() - 40);
        font.draw(batch, "Co2 Bar", 10, Gdx.graphics.getHeight() - 90);
        font.draw(batch, "dist travlled: " + scoringSystem.getTotalPlayerDistanceTraveled(), 10, Gdx.graphics.getHeight() - 150);
        font.draw(batch, "bike dist travlled: " + scoringSystem.getTotalBikeDistanceTraveled(), 10, Gdx.graphics.getHeight() - 200);
        font.draw(batch, "bus dist travlled: " + scoringSystem.getBusCount(), 10, Gdx.graphics.getHeight() - 250);
        font.draw(batch, "train dist travlled: " + scoringSystem.getTrainCount(), 10, Gdx.graphics.getHeight() - 300);
        font.draw(batch, "Score: " + scoringSystem.getScore(), 10, Gdx.graphics.getHeight() - 350);
        font.draw(batch, "co2barvalue: " +  co2BarValue, 10, Gdx.graphics.getHeight() - 400);
        font.draw(batch, "timebarvalue: " +  timeBarValue, 10, Gdx.graphics.getHeight() - 450);


        Rectangle playerBounds = player.getBounds();




        String[] messages = new String[] {
                "Use W, A, S, D keys to move.",
                "Welcome to Captain Carbon!\nFollow the text tutorial and arrows for guidance!",
                "Check the car park for a bike!",
                "The goal of this game is to learn about how we can\n travel more effectively and reduce our carbon footprint.",
                "There are many different means of transport available\n to us that are more eco friendly than our own car",
                "Like bikes! Take a bike out of the car park and \n find a bus!",

                "EBikes produce less carbon!",
                "But be careful,\n They run out of battery!",
                "Hey look a bus!",
                "Get in and press SPACE to move to the next stop!",

                "Buses can be chained too!",
                "Oh look a train station!\nUse these to quickly naviate using its map",
                "Oh look a train station!\nUse these to quickly naviate using its map",

                "Don't forget to keep an eye on your\ncarbon emissions and time!\nOr the game will end!",
                "Don't forget to keep an eye on your\ncarbon emissions and time!\nOr the game will end!",
                "Collect the gem to complete the level!",


        };

        Vector2[] messagePositions = new Vector2[] {
                new Vector2(140, 150),
                new Vector2(220, 150),
                new Vector2(300, 150),
                new Vector2(350, 250),
                new Vector2(350, 350),
                new Vector2(340, 450),// Bike message find a bus

                new Vector2(570, 150),
                new Vector2(650, 150),
                new Vector2(740, 150),
                new Vector2(830, 150),//"Get in and press SPACE to move to the next stop!",

                new Vector2(890, 910), // "Buses can be chained too!",
                new Vector2(2030, 840),
                new Vector2(2050, 830),
                new Vector2(2050, 830),

                new Vector2(3860, 1550),
                new Vector2(3920, 1400),
                new Vector2(3860, 1500),


        };

        // Iterate through each tutorial area
        for (int i = 0; i < messages.length; i++) {
            if (player.getBounds().overlaps(new Rectangle(messagePositions[i].x, messagePositions[i].y, 20, 20))) {
                GlyphLayout layout = new GlyphLayout(tutorialFont, messages[i]);
                float offsetFromRight = 150; // Increase this value to move text more to the right
                float textX = MathUtils.clamp(player.getX() + offsetFromRight, 100, camera.viewportWidth - layout.width + 190);
                float textY = MathUtils.clamp(player.getY() + 100, layout.height, camera.viewportHeight - layout.height - 20);

                tutorialFont.draw(batch, layout, textX, textY);
            }
        }


        batch.end();


        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();


//        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//                minimap.DisplayMinimap(stage, player); // Display the minimap
//            }
//
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (Minimap.MinimapDisplayed) {
                minimap.HideMinimap(stage); // Hide the minimap
            } else {
                minimap.DisplayMinimap(stage, player, getGemPosition()); // Display the minimap
            }
        }

    }

    @Override
    public Player getPlayer(){
        return player;
    }


    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        skin.dispose();
        stage.dispose();
        pauseFont.dispose();
        batch.dispose();
        tutorialFont.dispose();

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
                if (Gdx.input.isKeyJustPressed(Input.Keys.E) && GameScreen.gameState != GameState.PAUSED) {
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
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && GameScreen.gameState != GameState.PAUSED)  {
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
                stage.addActor(timeBar.getProgressBar());
                stage.addActor(co2Bar.getProgressBar());
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
        if (gem != null) {
            return gem.getPosition();
        }
        return null;
    }
    public void checkEndCondition() {
        if (timeBar.getValue() <= 0 || co2Bar.getValue() <= 0 ) {
            completionListener.onLevelFailed();


        }
        else if (gem != null && player.getBounds().overlaps(gem.getBounds())){
            completionListener.onLevelCompleted();

        }


    }



}

