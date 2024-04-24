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
    private TiledMap map;
    private static final Vector2 WORLD_SIZE;
    public static boolean MinimapDisplayed = false;
    public static boolean isMinimapDisplayed() {
        return MinimapDisplayed;
    }
    private Image playerMarkerImage;
    private Image gemMarkerImage;

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
        playerMarkerImage.setSize(15, 15);

        Texture gemMarker = new Texture(Gdx.files.internal("Tilesets/gem.png"));
        gemMarkerImage = new Image(gemMarker);
        gemMarkerImage.setSize(15, 15);


        updateMinimap(stage);
        stage.addActor(playerMarkerImage);

    }

    public Vector2 getGemPosition() {
        return this.gemPosition;
    }

    static {
        TiledMap map = new TmxMapLoader().load("Game/assets/Map.tmx");
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        int mapWidth = map.getProperties().get("width", Integer.class) * tileWidth;
        int mapHeight = map.getProperties().get("height", Integer.class) * tileHeight;
        WORLD_SIZE = new Vector2(mapWidth, mapHeight);
    }

    public Vector2 getWorldSize() {
        return WORLD_SIZE;
    }



    public void updateMinimap2(Stage stage) {

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

    }

    public void updateMinimap(Stage stage) {
        Vector2 worldSize = getWorldSize();
        float worldWidth = worldSize.x;
        float worldHeight = worldSize.y;

        float mapWidth = 1800;
        float mapHeight = 800;

        if (playerMarkerImage != null && player != null) {
            float playerX = (player.getX() / worldWidth) * mapWidth;
            float playerY = (player.getY() / worldHeight) * mapHeight;
            playerMarkerImage.setPosition(
                    (Gdx.graphics.getWidth() - mapWidth) / 2 + playerX,
                    (Gdx.graphics.getHeight() - mapHeight) / 2 + playerY);
        }
        if (gemMarkerImage != null && gemPosition != null) {
            float gemX = (gemPosition.x / worldWidth) * mapWidth;
            float gemY = (gemPosition.y / worldHeight) * mapHeight;
            gemMarkerImage.setPosition(
                    (Gdx.graphics.getWidth() - mapWidth) / 2 + gemX,
                    (Gdx.graphics.getHeight() - mapHeight) / 2 + gemY);
            stage.addActor(gemMarkerImage);
        }
    }

    public void HideMinimap(Stage stage) {
        if (MinimapDisplayed) {
            stage.clear();
            MinimapDisplayed = false;
        }
    }

}

