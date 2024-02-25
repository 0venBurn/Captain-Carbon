import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class UserInterface {
    private Stage stage;
    private Table table;

    public UserInterface() {
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Initialize your UI elements here
    }

    public void displayMainMenu() {
        // Code to display main menu
    }

    public void displayRoundMenu() {
        // Code to display round menu
    }

    public void displayFinalScore() {
        // Code to display final score
    }

    public void showPopup(String message) {
        // Code to show popup messages
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}