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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
public class Minimap {
    private Player player;
    private Vector2 gemPosition;
    public static boolean MinimapDisplayed = false;
    public static boolean isMinimapDisplayed() {
        return MinimapDisplayed;
    }
    private Image playerMarkerImage;
    private Image gemMarkerImage;
    private ArrayList<Image> busStationMarkers = new ArrayList<>();

    public void DisplayMinimap(Stage stage, Player player, Vector2 gemPosition) {
        this.player = player;
        this.gemPosition = gemPosition;
        Texture miniMaptexture = new Texture(Gdx.files.internal("miniMap.png"));
        Image miniMap = new Image(miniMaptexture);
        miniMap.setSize(2000, 800);
        miniMap.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);

        stage.clear();
        MinimapDisplayed = true;
        stage.addActor(miniMap);


        Texture playerMarker = new Texture(Gdx.files.internal("head.png"));
        playerMarkerImage = new Image(playerMarker);
        playerMarkerImage.setSize(40, 40);

        Texture gemMarker = new Texture(Gdx.files.internal("Tilesets/gem.png"));
        gemMarkerImage = new Image(gemMarker);
        gemMarkerImage.setSize(40, 40);


        updateMinimap(stage);
        stage.addActor(playerMarkerImage);

    }

    public Vector2 getGemPosition() {
        return this.gemPosition;
    }


    public void addBusStationMarker(float x, float y) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillCircle(0, 0, 10);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        Image marker = new Image(texture);
        marker.setSize(10, 10);
        busStationMarkers.add(marker);

    }


    public void updateMinimap(Stage stage) {

//        float playerX = (float) (player.getX() / 3.0);
//        float playerY = (float) (player.getY() / 4.0);

        if (playerMarkerImage != null && player != null) {
            playerMarkerImage.setPosition(
                    Gdx.graphics.getWidth() / 140f + player.getX() / 3,
                    Gdx.graphics.getHeight() / 9f + player.getY() / 4);
        }
        if (gemMarkerImage != null && gemPosition != null) {
            gemMarkerImage.setPosition(
                    Gdx.graphics.getWidth() / 140f + gemPosition.x / 3,
                    Gdx.graphics.getHeight() / 9f + gemPosition.y / 4);
            stage.addActor(gemMarkerImage);
        }


        for (Image marker : busStationMarkers) {
            stage.addActor(marker);
        }
    }

    public void HideMinimap(Stage stage) {
        if (MinimapDisplayed) {
            stage.clear();
            MinimapDisplayed = false;
        }
    }

}

