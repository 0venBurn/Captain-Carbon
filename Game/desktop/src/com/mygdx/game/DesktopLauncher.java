package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Ride Sharer");
//		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

		 config.setWindowedMode(1000, 800); // Uncomment this line to switch to windowed mode


		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
