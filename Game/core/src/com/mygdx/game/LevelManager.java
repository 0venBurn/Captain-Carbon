package com.mygdx.game;


public class LevelManager implements LevelCompletionListener {
    private MyGdxGame game;
    private int currentLevelIndex;
    public Scoring_System scoringSystem;

    private ILevel[] levels;

    public LevelManager(MyGdxGame game) {
        scoringSystem = Scoring_System.getInstance();
        this.game = game;
        levels = new ILevel[]{new TutorialLevel(this), new LevelOne(this), new LevelTwo(this)};
        currentLevelIndex = 0;
        loadCurrentLevel();
    }

    public void loadCurrentLevel() {
        levels[currentLevelIndex].load();
    }

    public ILevel getCurrentLevel() {
        if (currentLevelIndex >= 0 && currentLevelIndex < levels.length) {
            return levels[currentLevelIndex];
        } else {
            game.setScreen(new MainMenuScreen(game));
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
        }
    }

    public void onLevelFailed() {
        game.setScreen(new MainMenuScreen(game));
        scoringSystem.reset();

    }
}