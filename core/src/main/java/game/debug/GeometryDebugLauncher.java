package game.debug;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class GeometryDebugLauncher {
    public static void main(final String[] arg) {
        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(64);
        config.setTitle("IO Game Debug");
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new DebugApp(), config);
    }
}