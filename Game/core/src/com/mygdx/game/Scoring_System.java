package com.mygdx.game;
import java.io.FileWriter;
import java.io.IOException;

public class Scoring_System {
    private int busCount;
    private int trainCount;
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

    public void incrementBusCount() {
        busCount++;
    }

    public void incrementTrainCount() {
        trainCount++;
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

