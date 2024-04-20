package Screens;

import Screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OurGame extends Game{
    public SpriteBatch batch;
    @Override
    public void create(){
        batch = new SpriteBatch(); {
        this.setScreen(new MainMenuScreen(this));
//        Called when the app is created
        }
    }
    @Override
    public void render(){
        super.render();
    }
}
