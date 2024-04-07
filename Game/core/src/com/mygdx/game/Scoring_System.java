package com.mygdx.game;
import java.io.FileWriter;
import java.io.IOException;

public class Scoring_System {
    private int busCount;
    private int bikeCount;

    public Scoring_System() {
        this.busCount = 0;
        this.bikeCount = 0;

    }

    public void incrementBusCount() {
        busCount++;
    }

    public void incrementBikeCount() {
        bikeCount++;
    }
    public int getBusCount() {
        return busCount;
    }

    public int getBikeCount() {
        return bikeCount;
    }

    public void outputToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("Modes of Transport Used:\n");
            writer.write("Buses: " + busCount + "\n");
            writer.write("Bikes: " + bikeCount + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
