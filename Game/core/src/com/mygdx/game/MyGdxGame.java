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
public class MyGdxGame extends Game {
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Transport transport;
	private Player player;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera);
		camera.position.set(1000, 1700, 0);
		camera.update();

		map = new TmxMapLoader().load("Game/assets/Map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);

		// Initialize transport with some default starting position
		transport = new Transport(Transport.Mode.BUS, 1410, 1500, map);
		player = new Player(1000, 1700);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();

		// Update player position based on input
		player.update(deltaTime);
		transport.update(deltaTime);

		// Camera
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		renderer.setView(camera);
		renderer.render();

		// Render the player and transport
		renderer.getBatch().begin();
		player.render((SpriteBatch) renderer.getBatch());
		transport.render((SpriteBatch) renderer.getBatch());
		renderer.getBatch().end();
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