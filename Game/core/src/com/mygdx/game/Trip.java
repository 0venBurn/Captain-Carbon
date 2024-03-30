package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Trip {
    private int sourceX;
    private int sourceY;
    private int destinationX;
    private int destinationY;

    public Trip() {
        // Initialize the trip with random source and destination
        setSource();
        setDestination();
    }

    public void calculateCarbonFootprint() {
        // Calculation logic for carbon footprint
    }

    public void setSource() {
        // Set source coordinates randomly within the screen bounds
        sourceX = MathUtils.random(0, Gdx.graphics.getWidth());
        sourceY = MathUtils.random(0, Gdx.graphics.getHeight());
    }

    public void setDestination() {
        // Set destination coordinates randomly within the screen bounds
        destinationX = MathUtils.random(0, Gdx.graphics.getWidth());
        destinationY = MathUtils.random(0, Gdx.graphics.getHeight());
    }

    // Getters for source and destination coordinates
    public int getSourceX() {
        return sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }
}
