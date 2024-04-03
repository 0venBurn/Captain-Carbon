package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;


public class TrainStation extends Transport {
    private final String name;
    private final ArrayList<TrainStation> trainStations;
    private final Player player;
    public static boolean uiDisplayed = false;


    public TrainStation(Vector2 position, String name, ArrayList<TrainStation> trainStations, Player player) {
        super(Mode.TRAIN, position, null);
        this.name = name;
        this.trainStations = trainStations;
        this.player = player;
    }

    public Rectangle getBounds() {
        float width = 100;
        float height = 100;
        return new Rectangle(position.x - width / 2, position.y - height / 2, width, height);
    }

    public String getName() {
        return name;
    }

    public static boolean isUiDisplayed() {
        return uiDisplayed;
    }

    public void displayStationUI(Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        Texture texture = new Texture(Gdx.files.internal("miniMap.png"));
        Image miniMapImage = new Image(texture);
        miniMapImage.setSize(2000, 800);
        miniMapImage.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);

        stage.clear();
        uiDisplayed = true;
        // Add minimap image
        stage.addActor(miniMapImage);

        final float MAP_WIDTH = 9094f;
        final float MAP_HEIGHT = 8063f;

        for (TrainStation station : trainStations) {
            TextButton button = new TextButton(station.getName(), skin);

            float buttonX = miniMapImage.getX() + station.getPosition().x * (miniMapImage.getWidth() / MAP_WIDTH);
            float buttonY = miniMapImage.getY() + station.getPosition().y * (miniMapImage.getHeight() / MAP_HEIGHT);

            buttonX -= button.getWidth() / 2;
            buttonY -= button.getHeight() / 2;

            button.setPosition(buttonX, buttonY);
            button.setSize(300, 200);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.exitMetro(station.getPosition());
                    player.setPosition(station.getPosition().x, station.getPosition().y);
                    stage.clear();
                    uiDisplayed = false;
                }
            });

            stage.addActor(button);
        }
    }
}