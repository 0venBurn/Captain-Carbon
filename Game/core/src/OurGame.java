import com.badlogic.gdx.Game;
public class OurGame extends Game{

    @Override
    public void create(){
        this.setScreen(new MainMenuScreen(this));
//        Called when the app is created
    }
    @Override
    public void render(){
        super.render();
    }
}
