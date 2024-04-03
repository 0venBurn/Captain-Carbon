package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapLayer;
import java.util.ArrayList;
import java.util.Arrays;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class MyGdxGame extends Game {
	private BitmapFont font;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private ArrayList<Transport> transports;
	private Player player;
	private MapLayer collisionLayer;
	private Transport currentBus = null;
	private ArrayList<Transport> bikes;
	private ArrayList<TrainStation> trainStations;
	private Stage uiStage;

	@Override
	public void create() {
		font = new BitmapFont();
		uiStage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(uiStage);
		font.getData().setScale(1.5f);
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		map = new TmxMapLoader().load("Game/assets/Map.tmx");
		collisionLayer = map.getLayers().get("Collision");
		renderer = new OrthogonalTiledMapRenderer(map);

		transports = new ArrayList<>();
		Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(1414, 1500), Arrays.asList(
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
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (!TrainStation.isUiDisplayed()) {
			player.update(deltaTime, collisionLayer);
		}
		updateBuses(deltaTime);

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
		uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		uiStage.draw();
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
						station.displayStationUI(uiStage);
					}
				}
			}
		}
	}
	private void updateBuses(float deltaTime) {
		for (Transport transport : transports) {
			boolean isActiveBus = transport == currentBus;
			transport.update(deltaTime, isActiveBus, collisionLayer);
		}
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		font.dispose();
		for (Transport transport : transports) {
			transport.dispose();
		}
		player.dispose();
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		viewport.update(width, height);
	}
}