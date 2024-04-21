package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusStopLocations {

    public enum Level {
        TUTORIAL,
        LEVEL_ONE,
        LEVEL_TWO,
        LEVEL_THREE
    }


    public static List<Transport> defineBusLocations(Level level) {
        List<Transport> busLocations = new ArrayList<>();
        switch (level) {
            case TUTORIAL:
                busLocations.add(createBus(new Vector2(900, 50), Arrays.asList(new Vector2(900, 100), new Vector2(900, 375), new Vector2(900, 920)), Transport.Direction.UP));
                busLocations.add(createBus(new Vector2(810, 850), Arrays.asList(new Vector2(810, 850), new Vector2(1600, 850), new Vector2(2030, 850)), Transport.Direction.RIGHT));
                break;
            case LEVEL_ONE:
                busLocations.add(createBus(new Vector2(3660, 750), Arrays.asList(new Vector2(3540, 750), new Vector2(2500, 750), new Vector2(1390, 750), new Vector2(230, 750)), Transport.Direction.LEFT));
                break;
            case LEVEL_TWO:
                busLocations.add(createBus(new Vector2(2540, 2700), Arrays.asList(new Vector2(2970, 2700), new Vector2(3950, 2700)), Transport.Direction.RIGHT));
                busLocations.add(createBus(new Vector2(4350, 2040), Arrays.asList(new Vector2(4350, 1600), new Vector2(4350, 1430), new Vector2(4350, 750), new Vector2(3650, 750), new Vector2(2500, 750)), Transport.Direction.DOWN));
                busLocations.add(createBus(new Vector2(1145, 2430), Arrays.asList(new Vector2(1145, 2430), new Vector2(1145, 1510), new Vector2(1145, 850), new Vector2(1600, 850), new Vector2(2030, 850)), Transport.Direction.DOWN));


                break;
        }
        return busLocations;
    }

    // Helper method to create a Transport object with a set direction
    private static Transport createBus(Vector2 position, List<Vector2> route, Transport.Direction direction) {
        Transport bus = new Transport(Transport.Mode.BUS, position, route);
        bus.setCurrentDirection(direction);
        return bus;
    }
}