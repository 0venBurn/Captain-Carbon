
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Map {
    private TiledMap tiledMap;

    public Map(String mapPath) {
        this.tiledMap = new TmxMapLoader().load(mapPath);
    }

    public void addZone(Zone zone) {
        // Adds a new game zone
    }

    public void addLandmark(Landmark landmark) {
        // Manages landmarks
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}