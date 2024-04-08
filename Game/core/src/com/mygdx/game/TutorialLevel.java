package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TutorialLevel implements ILevel {
    private LevelCompletionListener completionListener;
    public void LevelOne(LevelCompletionListener listener) {
        this.completionListener = listener;
    }
    @Override
    public void load() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(SpriteBatch batch) {

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
//    public void checkEndCondition() {
//        if (/* level completion condition */) {
//            completionListener.onLevelCompleted();
//        }
//    }
}