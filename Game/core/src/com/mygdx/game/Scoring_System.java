package com.mygdx.game;
import com.badlogic.gdx.Gdx;

import java.io.FileWriter;
import java.io.IOException;
import com.badlogic.gdx.files.FileHandle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Scoring_System {
    private int busCount, trainCount;
    private int CARBON_EMISSION_WALK = 0, CARBON_EMISSION_BIKE = 130, CARBON_EMISSION_BUS = 500, CARBON_EMISSION_TRAIN = 1800;
    private int SPEED_WALK = 1, SPEED_BIKE = 5, SPEED_BUS = 25, SPEED_TRAIN = 40;
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
        float busEmissions = busCount * CARBON_EMISSION_BUS;
        float trainEmissions = trainCount * CARBON_EMISSION_TRAIN;
        float bikeEmissions = totalBikeDistanceTraveled / 100000 * CARBON_EMISSION_BIKE;
        float walkEmissions = totalPlayerDistanceTraveled / 10000 * CARBON_EMISSION_WALK;
        float totalEmissions = busEmissions + trainEmissions + bikeEmissions + walkEmissions;
        return totalEmissions;
    }

    public float calculateTotalTime() {
        float busTime = (busCount * 1000) / (SPEED_BUS);
        float trainTime = (trainCount * 1000) / (SPEED_TRAIN);
        float bikeTime = (totalBikeDistanceTraveled) / (SPEED_BIKE);
        float walkTime = (totalPlayerDistanceTraveled) / (SPEED_WALK);
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
        float score = 100000 / (1 + (totalTime / 10) + totalEmissions);
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


    public void outputToFile(String fileName, int currentLevelIndex) {
        float currentScore = getScore();
        FileHandle file = Gdx.files.local(fileName);

        try {
            FileWriter writer = new FileWriter(file.file(), true);
            writer.write("Level," + currentLevelIndex + "\n");
            writer.write("Bike," + (totalBikeDistanceTraveled / 100000 * CARBON_EMISSION_BIKE) / 10 + "kg CO2e" + "\n");
            writer.write("Train," + (trainCount * CARBON_EMISSION_TRAIN) / 10 + "kg CO2e" + "\n");
            writer.write("Bus," + (busCount * CARBON_EMISSION_BUS) / 10 + "kg CO2e" + "\n");
            writer.write("Score," + (getScore()) + "\n\n");

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public String readScoreFromFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        float maxScore = Float.MIN_VALUE;

        if (file.exists()) {
            String[] lines = file.readString().split("\\r?\\n");
            Pattern pattern = Pattern.compile("^Score,([0-9.]+)$");

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    try {
                        float score = Float.parseFloat(matcher.group(1));
                        if (score > maxScore) {
                            maxScore = score;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing score: " + e.getMessage());
                    }
                }
            }
        }
        return String.valueOf(maxScore);
    }
}