package com.mygdx.game;
import java.io.FileWriter;
import java.io.IOException;

public class Scoring_System {
    private int busCount;
    private int trainCount;

    private static Scoring_System instance;

    private Scoring_System() {
        this.busCount = 0;

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
