package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelTwo implements ILevel{

    private Player player;

    public LevelTwo() {
        player = new Player(250, 150);
    }

    @Override
    public int getMapWidth() {
        // Replace with the actual width of your map for LevelOne
        return 0;
    }

    @Override
    public int getMapHeight() {
        // Replace with the actual height of your map for LevelOne
        return 0;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void load() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }




    @Override
    public void dispose() {

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

    }

    @Override
    public void updateBuses(float deltaTime) {

    }

    @Override
    public void spawnGem() {

    }
}
