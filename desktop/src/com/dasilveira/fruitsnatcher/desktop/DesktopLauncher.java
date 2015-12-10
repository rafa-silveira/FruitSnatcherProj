package com.dasilveira.fruitsnatcher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dasilveira.fruitsnatcher.FruitSnatcher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Fruit Snatcher v2.0";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new FruitSnatcher(), config);
	}
}
