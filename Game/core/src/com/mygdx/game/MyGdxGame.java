package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;



public class MyGdxGame extends ApplicationAdapter {
	BitmapFont font;
	SpriteBatch batch;
	Texture dotTexture;
	Texture bikeTexture;
	Texture busTexture;
	Texture walkTexture;

	Texture img;
	private static final int BLOCK_SIZE = 32;
	private int x = 0;
	private int y = 0;
	private int timeCounter = 0;
	private int carbonCounter = 0;
	private static final int MODE_BASIC = 0;
	private static final int MODE_BIKE = 1;
	private static final int MODE_BUS = 2;
	private int currentMode = MODE_BASIC;
	@Override
	public void create () {

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		walkTexture = new Texture("black_dot.png");
		bikeTexture = new Texture("bike.png");
		busTexture = new Texture("bus.png");
		font = new BitmapFont();

	}



	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 0);
		System.out.print("" + Gdx.graphics.getDeltaTime());


		handleMovement();
		toggleMode();
		batch.begin();
		batch.draw(dotTexture, x * BLOCK_SIZE, y * BLOCK_SIZE);
		font.draw(batch, "Time: " + timeCounter,  10, Gdx.graphics.getHeight() - 10);
		font.draw(batch, "Carbon: " + carbonCounter, 10, Gdx.graphics.getHeight() - 30);
		font.draw(batch, "currentMode: " + currentMode, 10, Gdx.graphics.getHeight() - 50);

		batch.end();
	}

	private void toggleMode() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			currentMode = (currentMode + 1) % 3;
		}

	}

	private void handleMovement() {
		if (currentMode == MODE_BASIC) {
			basic_movement();
			dotTexture = new Texture("black_dot.png");

		} else if (currentMode == MODE_BIKE) {
			bike_movement();
			dotTexture = new Texture("bike.png");

		} else if (currentMode == MODE_BUS) {
			bus_movement();
			dotTexture = new Texture("bus.png");

		} else {
		}
	}

	private void basic_movement() {

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			x++;
			timeCounter += 5;
			carbonCounter++;

			if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
				x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			y++;
			timeCounter += 5;
			carbonCounter++;
			if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
				y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			x--;
			timeCounter += 5;
			carbonCounter++;
			if (x < 0)
				x = 0;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			y--;
			timeCounter += 5;
			carbonCounter++;
			if (y < 0)
				y = 0;
		}
	}



	private void bike_movement() {

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			x+=2;
			timeCounter += 3;
			carbonCounter++;
			// Ensure player doesn't move out of bounds
			if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
				x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			y+=2;
			timeCounter += 3;
			carbonCounter++;
			if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
				y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			x-=2;
			timeCounter += 3;
			carbonCounter++;
			if (x < 0)
				x = 0;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			y-=2;
			timeCounter += 3;
			carbonCounter++;
			if (y < 0)
				y = 0;
		}
	}




	private void bus_movement() {
		// Check arrow key inputs
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			x+=4;
			timeCounter += 2;
			carbonCounter+=3;
			if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
				x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			y+=4;
			timeCounter += 2;
			carbonCounter+=3;
			if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
				y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			x-=4;
			timeCounter += 2;
			carbonCounter+=3;
			if (x < 0)
				x = 0;
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			y-=4;
			timeCounter += 5;
			carbonCounter+=3;
			if (y < 0)
				y = 0;
		}
	}



	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
