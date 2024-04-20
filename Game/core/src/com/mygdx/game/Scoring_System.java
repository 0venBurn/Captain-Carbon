package com.mygdx.game;
import com.badlogic.gdx.Gdx;

import java.io.FileWriter;
import java.io.IOException;
import com.badlogic.gdx.files.FileHandle;

public class Scoring_System {
    private int busCount, trainCount;
    private int CARBON_EMISSION_WALK  = 0, CARBON_EMISSION_BIKE  = 10, CARBON_EMISSION_BUS  = 2500, CARBON_EMISSION_TRAIN  = 5000;
    private int SPEED_WALK  = 1, SPEED_BIKE  = 5,SPEED_BUS  = 10,SPEED_TRAIN  = 20;
    private float totalBikeDistanceTraveled = 0.0f, totalPlayerDistanceTraveled = 0.0f;
    private static Scoring_System instance;
    private boolean endOfLevel = false;

    private Scoring_System() {
        this.totalPlayerDistanceTraveled = 0;
        this.totalBikeDistanceTraveled = 0;

    }

    public static Scoring_System getInstance() {
        if (instance == null) {
            synchronized (Scoring_System.class) {
                if (instance == null) {
                    instance = new Scoring_System();
                }
            }
        }
        return instance;
    }
    public void reset() {
        busCount = 0;
        trainCount = 0;
        totalBikeDistanceTraveled = 0.0f;
        totalPlayerDistanceTraveled = 0.0f;
        endOfLevel = false;
    }
    public float calculateTotalCarbonEmissions() {
        float busEmissions = busCount * CARBON_EMISSION_BUS ;
        float trainEmissions = trainCount * CARBON_EMISSION_TRAIN ;
        float bikeEmissions = totalBikeDistanceTraveled/100000 * CARBON_EMISSION_BIKE;
        float walkEmissions = totalPlayerDistanceTraveled/10000 * CARBON_EMISSION_WALK;
        float totalEmissions = busEmissions + trainEmissions + bikeEmissions + walkEmissions;
        return totalEmissions;
    }

    public float calculateTotalTime() {
        float busTime = (busCount * 500) / (SPEED_BUS);
        float trainTime = (trainCount * 1000) / (SPEED_TRAIN);
        float bikeTime = (totalBikeDistanceTraveled) / (SPEED_BIKE);
        float walkTime = (totalPlayerDistanceTraveled)/(SPEED_WALK) ;
        float totalTime = busTime + trainTime + bikeTime + walkTime;
        return totalTime;
    }


    public void incrementBusCount() {
        busCount++;
    }

    public void incrementTrainCount() {
        trainCount++;
    }

    public float getScore() {
        float totalEmissions = calculateTotalCarbonEmissions();
        float totalTime = calculateTotalTime();
        float score = totalTime + totalEmissions;
        return score;
    }

    public int getBusCount() {
        return busCount;
    }

    public int getTrainCount() {
        return trainCount;
    }

    public void setTotalPlayerDistanceTraveled(float totalPlayerDistanceTraveled) {
        this.totalPlayerDistanceTraveled = totalPlayerDistanceTraveled;
    }

    public void setTotalBikeDistanceTraveled(float totalBikeDistanceTraveled) {
        this.totalBikeDistanceTraveled = totalBikeDistanceTraveled;
    }

    public float getTotalBikeDistanceTraveled() {
        return this.totalBikeDistanceTraveled;
    }
    public float getTotalPlayerDistanceTraveled() {
        return this.totalPlayerDistanceTraveled;
    }

    // will need this for writing to file maybe unless i do in level manager class
    public void setEndOfLevel(boolean endOfLevel) {
        this.endOfLevel = endOfLevel;
    }


    public void outputToFile(String fileName) {
        float currentScore = getScore();
        FileHandle file = Gdx.files.local(fileName);
        float existingScore = -1;

        if (file.exists() && !file.readString().isEmpty()) {
            try {
                existingScore = Float.parseFloat(file.readString().trim());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing existing score from file: " + e.getMessage());
            }
        }

        if (currentScore > existingScore) {
            file.writeString(Float.toString(currentScore), false);
        }
    }



    public String readScoreFromFile(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        if (file.exists()) {
            return file.readString();
        } else {
            return "None";
        }
    }

}

