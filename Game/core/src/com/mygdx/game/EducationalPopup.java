package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class EducationalPopup {
    private final Window window;
    private final Label messageLabel;
    private boolean isVisible;
    private final Skin skin;
    private OrthographicCamera camera;
    private final Random random;
    public enum TransportMode {
        BIKE,
        BUS,
        METRO
    }
    Map<TransportMode, List<String>> facts;


    public EducationalPopup() {
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        messageLabel = new Label("", skin, "default");
        window = new Window("Did you know?", skin);
        window.setSize(200, 100);
        messageLabel.setSize(window.getWidth() - 10, window.getHeight() - 50);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.top | Align.left);
        messageLabel.setVisible(false);
        window.setVisible(false);
        random = new Random();
        facts = loadFacts();
    }
    private Map<TransportMode, List<String>> loadFacts() {
        Map<TransportMode, List<String>> facts = new HashMap<>();
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.internal("transport_facts.json"));

        for (TransportMode mode : TransportMode.values()) {
            JsonValue modeFacts = base.get(mode.name());
            if (modeFacts != null) {
                List<String> factList = new ArrayList<>();
                for (JsonValue factValue : modeFacts) {
                    factList.add(factValue.asString());
                }
                facts.put(mode, factList);
            }
        }

        return facts;
    }
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;  // Set the camera from Player
    }

    public void setVisible(boolean isVisible) {
        window.setVisible(isVisible);
        messageLabel.setVisible(isVisible);
    }

    public void updatePosition() {
        if (camera != null) {
            float x = camera.position.x + camera.viewportWidth / 2 - window.getWidth() - 10;
            float y = camera.position.y + camera.viewportHeight / 2 - window.getHeight() - 10;
            window.setPosition(x, y);
            messageLabel.setPosition(window.getX() + 5, window.getY() + 15);
        }
    }

    public void show(TransportMode mode, float duration) {
        List<String> modeFacts = facts.get(mode);
        if (modeFacts == null || modeFacts.isEmpty()) {
            System.out.println("No facts available for this mode.");
            return;
        }
        String fact = modeFacts.get(random.nextInt(modeFacts.size()));
        messageLabel.setText(fact);
        setVisible(true);
        isVisible = true;
        // Add a delay to hide the popup
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                setVisible(false);
                isVisible = false;
            }
        }, duration);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Window getWindow() {
        return window;
    }

    public void draw(SpriteBatch batch) {
        if (isVisible && this.camera != null) {
            updatePosition();  // Update position based on the camera
            this.window.draw(batch, 1);
            this.messageLabel.draw(batch, 1);
        }
    }
}
