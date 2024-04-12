package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StationLocations {
    public static List<Vector2> defineTrainStationLocations() {
        List<Vector2> trainStationLocations = new ArrayList<>();
        trainStationLocations.add(new Vector2(1000, 2000)); // Station A
        trainStationLocations.add(new Vector2(2500, 2500)); // Station B
        trainStationLocations.add(new Vector2(3000, 3000)); // Station C
        trainStationLocations.add(new Vector2(3500, 3500)); // Station D
        return trainStationLocations;
    }

    public static List<Transport> defineBusLocations() {
        List<Transport> busLocations = new ArrayList<>();
        Transport upBus = new Transport(Transport.Mode.BUS, new Vector2(1414, 1500), Arrays.asList(
                new Vector2(1414, 1500),
                new Vector2(1414, 1800),
                new Vector2(1414, 2200),
                new Vector2(1414, 3580)
        ));
        Transport upBus2 = new Transport(Transport.Mode.BUS, new Vector2(1414, 2000), Arrays.asList(
                new Vector2(1414, 2400),
                new Vector2(1414, 2700),
                new Vector2(1414, 3000)
        ));
        Transport horizontalBus = new Transport(Transport.Mode.BUS, new Vector2(1300, 2300), Arrays.asList(
                new Vector2(1800, 2300),
                new Vector2(2000, 2300),
                new Vector2(2500, 2300)
        ));

        busLocations.add(upBus);
        busLocations.add(upBus2);
        busLocations.add(horizontalBus);

        return busLocations;
    }

}