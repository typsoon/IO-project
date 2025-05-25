import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import viewmodel.AbstractViewManager;
import viewmodel.ViewManagerInjector;

class GameLauncher extends Game {
    @Override
    public void create() {
        final AbstractViewManager viewManager = new ViewManagerInjector(this).getViewManager();
        viewManager.start();
    }
}

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("IO Game");
        // config.setWindowedMode(800, 720);
        // TODO: remove magic numbers and strings
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new GameLauncher(), config);
    }
}
