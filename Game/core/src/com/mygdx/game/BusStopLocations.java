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