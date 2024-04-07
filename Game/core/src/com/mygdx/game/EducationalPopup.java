package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EducationalPopup extends Dialog {

    public EducationalPopup(String title, Skin skin) {
        super(title, skin);
    }

    public void setContent(String content) {
        Label label = new Label(content, getSkin());
        getContentTable().add(label).pad(20f);
    }
}
