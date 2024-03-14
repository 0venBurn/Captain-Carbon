import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import jdk.tools.jmod.Main;

public class MainMenuScreen implements Screen{
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(){
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show(){
//        Method to show the current screen from Screen interface
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,1); // Black Background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }
}
