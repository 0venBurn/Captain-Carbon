import com.badlogic.gdx.maps.MapObject;

public class Landmark {
    private String description;
    private MapObject mapObject;

    public Landmark(MapObject mapObject) {
        this.mapObject = mapObject;
        // Initialization code here
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MapObject getMapObject() {
        return mapObject;
    }
}