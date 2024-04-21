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
    private Texture stationButtonTexture;
    private Texture miniMapTexture;

    public TrainStation(Vector2 position, String name, ArrayList<TrainStation> trainStations, Player player) {
        super(Mode.TRAIN, position, null);
        this.name = name;
        this.trainStations = trainStations;
        this.player = player;
        this.stationButtonTexture = new Texture(Gdx.files.internal("stationButton.png"));
        this.miniMapTexture = new Texture(Gdx.files.internal("miniMap.png"));
    }

    public void displayStationUI(Stage stage) {
        Image miniMapImage = new Image(miniMapTexture);
        miniMapImage.setSize(2000, 800);
        miniMapImage.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);
        stage.addActor(miniMapImage);

        uiDisplayed = true;

        for (TrainStation station : trainStations) {
            Image button = new Image(stationButtonTexture);
            float buttonX = 0;
            float buttonY = 0;

            switch (station.getName()) {
                case "Station A":
                    buttonX = 715;
                    buttonY = 350;
                    break;
                case "Station B":
                    buttonX = 1450;
                    buttonY = 550;
                    break;
                case "Station C":
                    buttonX = 2;
                    buttonY = 540;
                    break;
            }

            button.setPosition(buttonX, buttonY);
            button.setSize(200, 133);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.exitMetro(station.getPosition());
                    player.setPosition(station.getPosition().x, station.getPosition().y - 100);
                    scoringSystem.incrementTrainCount();
                    stage.clear();
                    uiDisplayed = false;
                }
            });
            stage.addActor(button);
        }
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

    public Vector2 getPosition() {
        return position;
    }
}