import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class TransportMode {

    public Texture dotTexture;
    public Texture bikeTexture;
    public Texture busTexture;
    public Texture walkTexture;

    private static final int BLOCK_SIZE = 32;

    public int x = 0;
    public int y = 0;
    public int timeCounter = 0;
    public int carbonCounter = 0;
    private static final int MODE_BASIC = 0;
    private static final int MODE_BIKE = 1;
    private static final int MODE_BUS = 2;
    public int currentMode = MODE_BASIC;



public void basic_movement() {

    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        x++;
        timeCounter += 5;
        carbonCounter++;

        if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
            x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        y++;
        timeCounter += 5;
        carbonCounter++;
        if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
            y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        x--;
        timeCounter += 5;
        carbonCounter++;
        if (x < 0)
            x = 0;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        y--;
        timeCounter += 5;
        carbonCounter++;
        if (y < 0)
            y = 0;
    }
}



public void bike_movement() {

    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        x+=2;
        timeCounter += 3;
        carbonCounter++;
        // Ensure player doesn't move out of bounds
        if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
            x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        y+=2;
        timeCounter += 3;
        carbonCounter++;
        if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
            y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        x-=2;
        timeCounter += 3;
        carbonCounter++;
        if (x < 0)
            x = 0;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        y-=2;
        timeCounter += 3;
        carbonCounter++;
        if (y < 0)
            y = 0;
    }
}




public void bus_movement() {
    // Check arrow key inputs
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
        x+=4;
        timeCounter += 2;
        carbonCounter+=3;
        if (x >= Gdx.graphics.getWidth() / BLOCK_SIZE)
            x = Gdx.graphics.getWidth() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        y+=4;
        timeCounter += 2;
        carbonCounter+=3;
        if (y >= Gdx.graphics.getHeight() / BLOCK_SIZE)
            y = Gdx.graphics.getHeight() / BLOCK_SIZE - 1;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
        x-=4;
        timeCounter += 2;
        carbonCounter+=3;
        if (x < 0)
            x = 0;
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        y-=4;
        timeCounter += 5;
        carbonCounter+=3;
        if (y < 0)
            y = 0;
    }
}

}