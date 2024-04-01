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

	@Override
	public void create() {
		font = new BitmapFont();
		font.getData().setScale(1.5f);
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		map = new TmxMapLoader().load("Game/assets/Map.tmx");
		collisionLayer = map.getLayers().get("Collision");
		renderer = new OrthogonalTiledMapRenderer(map);

		transports = new ArrayList<>();
		// Original bus route
		Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(1414, 1500), map, Arrays.asList(
				new Vector2(1414, 1500),
				new Vector2(1414, 1800),
				new Vector2(1414, 2200),
				new Vector2(1414, 3580)
		));

		// New bus route
		Transport upBus2 = new Transport(Transport.Mode.BUS, new Vector2(1414, 2000), map, Arrays.asList(
				new Vector2(1414, 2400),
				new Vector2(1414, 2700),
				new Vector2(1414, 3000)
		));

		Transport horizontalBus = new Transport(Transport.Mode.BUS, new Vector2(1300, 2300), map, Arrays.asList(
				new Vector2(1800, 2300),
				new Vector2(2000, 2300),
				new Vector2(2500, 2300)
		));
		upBus.setCurrentDirection(Transport.Direction.UP);
		upBus2.setCurrentDirection(Transport.Direction.UP);
		horizontalBus.setCurrentDirection(Transport.Direction.RIGHT); // Set initial direction to right
		transports.add(upBus);
		transports.add(upBus2);
		transports.add(horizontalBus);

		player = new Player(1000, 1700);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();

		player.update(deltaTime, collisionLayer);
		updateBuses(deltaTime);

		// Check if the player is on a bus and update the camera position accordingly
		if (player.isOnBus() && currentBus != null) {
			camera.position.set(currentBus.getPosition().x, currentBus.getPosition().y, 0);
		} else {
			camera.position.set(player.getX(), player.getY(), 0);
		}
		camera.update();
		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		if (!player.isOnBus()) {
			player.render((SpriteBatch) renderer.getBatch());
		}
		for (Transport transport : transports) {
			transport.render((SpriteBatch) renderer.getBatch());
		}

		checkPlayerBusInteraction((SpriteBatch) renderer.getBatch());
		renderer.getBatch().end();
	}

	private void checkPlayerBusInteraction(SpriteBatch spriteBatch) {
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
					currentBus = null; // Clear currentBus as the player is no longer on it
					camera.position.set(player.getX(), player.getY(), 0); // Switch the camera back to the player
				}
			}
		}
	}

	private void updateBuses(float deltaTime) {
		for (Transport transport : transports) {
			boolean isActiveBus = transport == currentBus;
			transport.update(deltaTime, isActiveBus);
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
		viewport.update(width, height);
	}
}
