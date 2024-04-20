package com.mygdx.game;


public class LevelManager implements LevelCompletionListener {
    private MyGdxGame game;
    private int currentLevelIndex;
    public Scoring_System scoringSystem;

    private ILevel[] levels;

    public LevelManager(MyGdxGame game) {
        scoringSystem = Scoring_System.getInstance();

        this.game = game;
        levels = new ILevel[]{new TutorialLevel(this)};
        currentLevelIndex = 0;
        loadCurrentLevel();
    }

    public void loadCurrentLevel() {
        levels[currentLevelIndex].load();
    }

    public ILevel getCurrentLevel() {
        if (currentLevelIndex >= 0 && currentLevelIndex < levels.length) {
            return levels[currentLevelIndex];
        }
        return null;
    }

    public void onLevelCompleted() {
        currentLevelIndex++;
        if (currentLevelIndex < levels.length) {
            loadCurrentLevel();
        } else {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    public void onLevelFailed() {
        game.setScreen(new MainMenuScreen(game));
        scoringSystem.outputToFile("scores.txt");
        scoringSystem.reset();





    }
}