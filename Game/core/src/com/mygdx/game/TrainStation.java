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

    public void  displayStationUI(Stage stage) {
        Texture texture = new Texture(Gdx.files.internal("miniMap.png"));
        Image miniMapImage = new Image(texture);
        miniMapImage.setSize(2000, 800);
        miniMapImage.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);
        stage.clear();
        uiDisplayed = true;

        // Add minimap image
        stage.addActor(miniMapImage);

        TrainStation currentStation = player.getCurrentStation();
        Vector2 currentStationPosition = currentStation.getPosition();

        Texture playerMarker = new Texture(Gdx.files.internal("head.png"));
        Image playerMarkerImage = new Image(playerMarker);
        playerMarkerImage.setSize(50, 50);

        stage.addActor(playerMarkerImage);

        Texture stationButtonTexture = new Texture(Gdx.files.internal("stationButton.png"));
        for (TrainStation station : trainStations) {
            Image button = new Image(stationButtonTexture);
            float buttonX = 0;
            float buttonY = 0;

            float playerMarkerX = 0;
            float playerMarkerY = 0;

            if (station.getName().equals("Station A")) {
                buttonX = 715;
                buttonY = 350;
            } else if (station.getName().equals("Station B")) {
                buttonX = 1450;
                buttonY = 550;
            } else if (station.getName().equals("Station C")) {
                buttonX = 2;
                buttonY = 540;
            } else if (station.getName().equals("Station D")) {
                buttonX = 400;
                buttonY = 530;
            }


            if (currentStation.getName().equals("Station A")) {
                playerMarkerX = 790;
                playerMarkerY = 300;
            } else if (currentStation.getName().equals("Station B")) {
                playerMarkerX = 1525;
                playerMarkerY = 500;
            } else if (currentStation.getName().equals("Station C")) {
                playerMarkerX = 2;
                playerMarkerY = 540;
            } else if (currentStation.getName().equals("Station D")) {
                playerMarkerX = 400;
                playerMarkerY = 530;
            }

            playerMarkerImage.setPosition(playerMarkerX, playerMarkerY);

            button.setPosition(buttonX, buttonY);
            button.setSize(200, 133);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    player.exitMetro(station.getPosition());

                    player.setPosition(station.getPosition().x, station.getPosition().y - 120);
                    scoringSystem.incrementTrainCount();
                    stage.clear();

                    uiDisplayed = false;


                }
            });

            stage.addActor(button);
        }


    }

}