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
    private float levelIndex;
    public static boolean MinimapDisplayed = false;
    public static boolean isMinimapDisplayed() {
        return MinimapDisplayed;
    }
    private Image playerMarkerImage;
    private Image gemMarkerImage;
    private ArrayList<Image> busStationMarkers = new ArrayList<>();

    public void DisplayMinimap(Stage stage, Player player, Vector2 gemPosition, float levelIndex)  {
        this.player = player;
        this.gemPosition = gemPosition;
        this.levelIndex = levelIndex;

        Texture miniMaptexture;

        if (levelIndex == 0) {
            miniMaptexture = new Texture(Gdx.files.internal("busmap1.jpg"));

        } else if (levelIndex == 1) {
            miniMaptexture = new Texture(Gdx.files.internal("busmap2.jpg"));
        } else {
            miniMaptexture = new Texture(Gdx.files.internal("busmap3.jpg"));
        }

        Image miniMap = new Image(miniMaptexture);
        miniMap.setSize(1800, 800);
        //miniMap.setPosition(Gdx.graphics.getWidth() / 140f, Gdx.graphics.getHeight() / 9f);

        miniMap.setPosition(
                (Gdx.graphics.getWidth() - miniMap.getWidth()) / 2,
                (Gdx.graphics.getHeight() - miniMap.getHeight()) / 2
        );

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



    public void updateMinimap(Stage stage) {

        if (playerMarkerImage != null && player != null) {
            float playerX = (float) (player.getX() / 2.8);
            float playerY = (float) (player.getY() / 4.0);
            playerMarkerImage.setPosition(
                    Gdx.graphics.getWidth() / 140f + playerX,
                    Gdx.graphics.getHeight() / 9f + playerY);
        }
        if (gemMarkerImage != null && gemPosition != null) {
            float gemX = (float) (gemPosition.x / 2.8);
            float gemY = (float) (gemPosition.y / 4.0);
            gemMarkerImage.setPosition(
                    Gdx.graphics.getWidth() / 140f + gemX,
                    Gdx.graphics.getHeight() / 9f + gemY);
            stage.addActor(gemMarkerImage);
        }

//        if (playerMarkerImage != null && player != null) {
//            playerMarkerImage.setPosition(
//                    Gdx.graphics.getWidth() / 140f + player.getX() / 3,
//                    Gdx.graphics.getHeight() / 9f + player.getY() / 4);
//        }
//        if (gemMarkerImage != null && gemPosition != null) {
//            gemMarkerImage.setPosition(
//                    Gdx.graphics.getWidth() / 140f + gemPosition.x / 3,
//                    Gdx.graphics.getHeight() / 9f + gemPosition.y / 4);
//            stage.addActor(gemMarkerImage);
//        }

    }

    public void HideMinimap(Stage stage) {
        if (MinimapDisplayed) {
            stage.clear();
            MinimapDisplayed = false;
        }
    }

}

