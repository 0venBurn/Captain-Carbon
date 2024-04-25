package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class LevelManager implements LevelCompletionListener {
    private final MyGdxGame game;
    private int currentLevelIndex;
    public Scoring_System scoringSystem;
    private static Music backgroundMusic;
    private final ILevel[] levels;

    public LevelManager(MyGdxGame game) {
        scoringSystem = Scoring_System.getInstance();
        this.game = game;
        levels = new ILevel[]{new TutorialLevel(this), new LevelOne(this), new LevelTwo(this)};
        currentLevelIndex = 0;

        if (backgroundMusic == null) {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music.mp3"));

        }
        djSpinThatShi();
        loadCurrentLevel();
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void loadCurrentLevel() {
        levels[currentLevelIndex].load();
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public ILevel getCurrentLevel() {
        if (currentLevelIndex >= 0 && currentLevelIndex < levels.length) {
            return levels[currentLevelIndex];
        } else {
            game.setScreen(new MainMenuScreen(game));
            backgroundMusic.pause();
            return new DummyLevel();
        }
    }

    public void onLevelCompleted() {
        currentLevelIndex++;
        if (currentLevelIndex < levels.length) {
            scoringSystem.outputToFile("scores.txt", currentLevelIndex);
            loadCurrentLevel();
            scoringSystem.reset();
        } else {
            scoringSystem.outputToFile("scores.txt", currentLevelIndex);
            game.setScreen(new MainMenuScreen(game));
            scoringSystem.reset();
            disposeMusic();
        }
    }

    public void onLevelFailed() {
        game.setScreen(new MainMenuScreen(game));
        scoringSystem.reset();
        backgroundMusic.pause();
    }


    private void djSpinThatShi() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void disposeMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }
}
