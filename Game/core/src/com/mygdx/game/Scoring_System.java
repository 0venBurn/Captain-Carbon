package com.mygdx.game;
import java.io.FileWriter;
import java.io.IOException;

public class Scoring_System {
    private int busCount;
    private int bikeCount;
    // Add more variables to track other modes of transport if needed

    public Scoring_System() {
        this.busCount = 0;
        this.bikeCount = 0;

        // Initialize other variables if needed
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
    // Add methods to update other modes of transport if needed

    public void outputToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("Modes of Transport Used:\n");
            writer.write("Buses: " + busCount + "\n");
            writer.write("Bikes: " + bikeCount + "\n");
            // Write other modes of transport if needed
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
