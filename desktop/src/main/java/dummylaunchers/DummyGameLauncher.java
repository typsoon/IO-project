package dummylaunchers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.impl.AtlasLoader;
import frontend.assetsloading.impl.BasicTextureManager;
import frontend.concreteviews.gameplayview.GameplayViewFactory;
import game.engine.PlayerConfig;
import game.engine.entities.EntityGroupID;
import game.engine.entities.geometry.GeometryConfigID;
import game.session.GameSessionFactory;
import game.session.IPlayerConnector;
import game.session.PlayerData;
import utils.ISendable;

class GameLauncher extends Game {
    @Override
    public void create() {
        var playerConfig = new PlayerConfig(GeometryConfigID.HUMAN, EntityGroupID.HUMAN_BASIC);

        var sendablesSentToClient = new ArrayList<ISendable>();
        IPlayerConnector dummyPlayerConnector = gameStates -> {
            synchronized (sendablesSentToClient) {
                Logger.getGlobal().finer("Game states sent %s".formatted(gameStates));
                sendablesSentToClient.addAll(gameStates);
            }
        };
        var dummyPlayerData = new PlayerData(dummyPlayerConnector, playerConfig);

        var gameSessionManager = GameSessionFactory.createGameSessionManager(List.of(dummyPlayerData));

        var dummySocketWrapper = new Utility.DummySocketWrapper(sendablesSentToClient, gameSessionManager,
                dummyPlayerData.connector());
        var viewManager = new Utility.NoInteractionViewManager();

        Constructor<? extends ITextureManager> basicTexManagerConstructor;
        ITextureManager textureManager;
        try {
            basicTexManagerConstructor = BasicTextureManager.class
                    .getDeclaredConstructor();
            basicTexManagerConstructor.setAccessible(true);
            textureManager = basicTexManagerConstructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        var texturesProvider = new AtlasLoader();

        var gameplayView = new GameplayViewFactory().getGameplayView(this, viewManager, dummySocketWrapper,
                textureManager, playerConfig, texturesProvider, List.of(), 5);

        gameplayView.display();
    }
}

public class DummyGameLauncher {
    public static void main(final String[] arg) {
        var applog = Logger.getGlobal();
        Handler systemOut = new ConsoleHandler();
        var level = Level.INFO;
        // var level = Level.FINER;
        systemOut.setLevel(level);
        applog.addHandler(systemOut);
        applog.setLevel(level);

        applog.setUseParentHandlers(false);

        final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Dummy IO Game");
        // config.setWindowedMode(800, 720);
        // TODO: remove magic numbers and strings
        config.setWindowedMode(720, 720);
        new Lwjgl3Application(new GameLauncher(), config);
    }
}
