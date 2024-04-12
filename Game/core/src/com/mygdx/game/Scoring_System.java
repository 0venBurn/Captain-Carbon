package com.mygdx.game;
import java.io.FileWriter;
import java.io.IOException;

public class Scoring_System {
    private int busCount;
    private int trainCount;
    private int CARBON_EMISSION_WALK  = 0;
    private int CARBON_EMISSION_BIKE  = 2;
    private int CARBON_EMISSION_BUS  = 3;

    private int CARBON_EMISSION_TRAIN  = 4;

    private int SPEED_WALK  = 1;
    private int SPEED_BIKE  = 2;
    private int SPEED_BUS  = 3;

    private int SPEED_TRAIN  = 4;
    private float totalBikeDistanceTraveled = 0.0f;
    private float totalPlayerDistanceTraveled = 0.0f;
    private static Scoring_System instance;


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

    public float calculateTotalCarbonEmissions() {
        float totalEmissions = 0;
        float busEmissions = busCount * CARBON_EMISSION_BUS * 5;
        float trainEmissions = trainCount * CARBON_EMISSION_TRAIN * 10;
        float bikeEmissions = totalBikeDistanceTraveled/100 * CARBON_EMISSION_BIKE;
        float walkEmissions = totalPlayerDistanceTraveled/100 * CARBON_EMISSION_WALK;

        totalEmissions = busEmissions + trainEmissions + bikeEmissions + walkEmissions;
        return totalEmissions;
    }

    public float calculateTotalTime() {
        float totalTime = 0;
        float busTime = (busCount * 500) / SPEED_BUS*100;
        float trainTime = (trainCount * 1000) / SPEED_TRAIN*100;
        float bikeTime = totalBikeDistanceTraveled / SPEED_BIKE*100;
        float walkTime = totalPlayerDistanceTraveled / 100;

        totalTime = busTime + trainTime + bikeTime + walkTime;
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

    public void outputToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("Modes of Transport Used:\n");
            writer.write("Buses: " + busCount + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

