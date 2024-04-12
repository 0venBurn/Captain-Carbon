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

    public Scoring_System scoringSystem;

    public TrainStation(Vector2 position, String name, ArrayList<TrainStation> trainStations, Player player) {
        super(Mode.TRAIN, position, null);
        this.name = name;
        this.trainStations = trainStations;
        this.player = player;
        scoringSystem = Scoring_System.getInstance();

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


    // Responsible for TrainStation UI
    public void displayStationUI(Stage stage) {
        Texture texture = new Texture(Gdx.files.internal("miniMap.png"));
        Texture stationButtonTexture = new Texture(Gdx.files.internal("stationButton.png"));
        Image miniMapImage = new Image(texture);
        miniMapImage.setSize(2000, 800);
        miniMapImage.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);

        stage.clear();
        uiDisplayed = true;

        // Add minimap image
        stage.addActor(miniMapImage);

        final float MAP_WIDTH = 9094f;
        final float MAP_HEIGHT = 8063f;


        float buttonX = 0;
        float buttonY = 0;
        for (TrainStation station : trainStations) {
            Image button = new Image(stationButtonTexture);

            if (station.getName().equals("Station A")) {
                buttonX = 715;
                buttonY = 350;
            } else if (station.getName().equals("Station B")) {
                buttonX = 1450;
                buttonY = 550;
            }

            button.setPosition(buttonX, buttonY);
            button.setSize(200, 133);
            System.out.println("Button position for station " + station.getName() + ": X = " + buttonX + ", Y = " + buttonY);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.exitMetro(station.getPosition());
                    scoringSystem.incrementTrainCount();

                    player.setPosition(station.getPosition().x, station.getPosition().y - 100);
                    stage.clear();

                    uiDisplayed = false;
                    Gdx.input.setInputProcessor(null);
                }
            });

            stage.addActor(button);

        }
    }
}