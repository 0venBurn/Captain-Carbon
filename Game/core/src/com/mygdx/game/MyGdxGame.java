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


public class MyGdxGame extends Game {
	private BitmapFont font;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Transport transport;
	private Player player;

	@Override
	public void create() {
		font = new BitmapFont();
		font.getData().setScale(1.5f);
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera);
		camera.position.set(1000, 1700, 0);
		camera.update();
		map = new TmxMapLoader().load("Game/assets/Map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		transport = new Transport(Transport.Mode.BUS, 1410, 1500, map);
		player = new Player(1000, 1700);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();

		player.update(deltaTime);
		transport.update(deltaTime);

		camera.position.set(player.isOnBus() ? transport.getPosition().x : player.getX(),
				player.isOnBus() ? transport.getPosition().y : player.getY(), 0);
		camera.update();

		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		if (!player.isOnBus()) {
			player.render((SpriteBatch) renderer.getBatch());
		}
		transport.render((SpriteBatch) renderer.getBatch());

		// Check if final stop reached
		if (transport.isFinalStopReached()) {
			font.draw(renderer.getBatch(), "Final Stop Reached", camera.position.x - 100, camera.position.y);
		}

		checkPlayerBusInteraction();
		renderer.getBatch().end();
	}


	private void checkPlayerBusInteraction() {
		Rectangle playerBounds = player.getBounds();
		Rectangle transportBounds = transport.getBounds();

		if (playerBounds.overlaps(transportBounds)) {
			if (!player.isOnBus()) {
				// Prompt to enter the bus
				font.draw(renderer.getBatch(), "Press 'E' to enter the bus", player.getX(), player.getY() + 50);
				if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
					player.setOnBus(true);
				}
			} else if (player.isOnBus() && transport.canPlayerDisembark()) {
				// Prompt to exit the bus only when at a waypoint or final stop
				font.draw(renderer.getBatch(), "Press 'Q' to exit the bus", transport.getPosition().x, transport.getPosition().y + 50);
				if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
					player.setOnBus(false);
					player.setPosition(transport.getPosition().x - 50, transport.getPosition().y);
				}
			}
		}

		if (player.isOnBus() && transport.isFinalStopReached() && transport.canPlayerDisembark()) {

			font.draw(renderer.getBatch(), "Final Stop Reached - Press 'Q' to exit", player.getX() + 30, player.getY() + 30);
			if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
				player.setOnBus(false);
				player.setPosition(transport.getPosition().x - 50, transport.getPosition().y);
			}
		}
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		transport.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}