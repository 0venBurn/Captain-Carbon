package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ILevel {

        void load();
        void update();
        void render();
        void dispose();
        void show();
        void hide();
        void pause();
        void resume();
        void resize(int width, int height);
        void checkPlayerTransportInteraction(SpriteBatch spriteBatch);
        void updateBuses(float deltaTime);
        void spawnGem();

    }
