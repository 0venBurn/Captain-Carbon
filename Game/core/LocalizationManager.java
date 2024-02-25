import com.badlogic.gdx.utils.I18NBundle;

public class LocalizationManager {
    private I18NBundle bundle;

    public LocalizationManager(I18NBundle bundle) {
        this.bundle = bundle;
    }

    public void setLanguage(Language language) {
        // Code to set the game's language, potentially by loading a new bundle
    }

    public String get(String key) {
        return bundle.get(key); // Get a localized string by its key
    }
}