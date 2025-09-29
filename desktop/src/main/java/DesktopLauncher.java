import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import viewmodel.impl.BasicViewManagerInjector;

class GameLauncher extends Game {
    @Override
    public void create() {
        new BasicViewManagerInjector(this).getViewManager().start();
    }
}

public class DesktopLauncher {
    public static void main(final String[] arg) {
        var applog = Logger.getGlobal();
        Handler systemOut = new ConsoleHandler();
        // var level = Level.FINEST;
        // var level = Level.INFO;
        var level = Level.INFO;
        systemOut.setLevel(level);
        applog.addHandler(systemOut);
        applog.setLevel(level);

        applog.setUseParentHandlers(false);

        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("IO Game");
        // config.setWindowedMode(800, 720);
        // TODO: remove magic numbers and strings
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new GameLauncher(), config);
    }
}
