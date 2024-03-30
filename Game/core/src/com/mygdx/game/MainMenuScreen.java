package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.*;

public class MainMenuScreen implements Screen {
    private MyGdxGame game;
    private Stage stage;
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private Skin skin;
    private Viewport backgroundViewport;
    private Viewport uiViewport;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        initializeStage();
        loadAssets();
        createUI();
    }

    private void initializeStage() {
        batch = new SpriteBatch();
        uiViewport =  new FitViewport(1500,900, new OrthographicCamera());
        stage = new Stage(uiViewport, batch);
        Gdx.input.setInputProcessor(stage);
        backgroundViewport = new FitViewport(1500 , 900, new OrthographicCamera());
    }

    private void loadAssets() {

        AssetManager assetManager = new AssetManager();
        assetManager.load("flat-earth/skin/flat-earth-ui.atlas", TextureAtlas.class);
        assetManager.finishLoading(); // Blocks until all assets are loaded
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        backgroundTexture = new Texture(Gdx.files.internal("MainMenuBackground.png"));
    }

    private void createUI() {
        TextButton playButton = new TextButton("Play", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.add(playButton).fillX().uniformX();
        table.row().pad(20, 0, 20, 0);
        table.add(quitButton).fillX().uniformX();
        stage.addActor(table);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen());
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        backgroundViewport.apply();
        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    backgroundViewport.update(width, height, true);
    uiViewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        batch.dispose();
        skin.dispose(); // Don't forget to dispose of the skin
    }

}
