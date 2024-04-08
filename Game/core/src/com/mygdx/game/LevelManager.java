package com.mygdx.game;

public class LevelManager implements LevelCompletionListener {
    private MyGdxGame game;
    private int  currentLevelIndex;
    private ILevel[] levels;

    public LevelManager(MyGdxGame game){
        levels = new ILevel[] {new TutorialLevel(), new LevelOne(), new LevelTwo()};
        currentLevelIndex = 0;
        this.game = game;
    }

    public void loadCurrentLevel(){
        levels[currentLevelIndex].load();
    }

    public void onLevelCompleted(){
        currentLevelIndex++;
        if (currentLevelIndex < levels.length){
            loadCurrentLevel();
        }else{
            game.setScreen(new MainMenuScreen(game));
        }
    }
}