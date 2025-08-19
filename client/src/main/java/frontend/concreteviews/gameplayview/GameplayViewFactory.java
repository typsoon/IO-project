package frontend.concreteviews.gameplayview;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithTimedEventLoop;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerFactory;
import frontend.gamestate.DisplayableGameState;
import game.engine.PlayerConfig;
import network.client.DuplexSocketWrapper;
import viewmodel.ITextureManager;
import viewmodel.IView;
import viewmodel.IViewManager;

public class GameplayViewFactory {
    public IView getGameplayView(
            Game game, IViewManager viewManager, DuplexSocketWrapper clientSideSocketWrapper,
            ITextureManager textureManager, PlayerConfig playerConfig, TexturesProvider texturesProvider) {

        DisplayableGameState gameState = new DisplayableGameState();

        var gameplayManager = new GameplayManagerFactory().getGameplayManager(viewManager, clientSideSocketWrapper,
                gameState, playerConfig);

        // see other screens (Login, GameClient) for clues about how these work
        var listeners = new ArrayList<EventListener>();

        // see libgdx docs
        var processors = new ArrayList<InputProcessor>();

        var view = new GameplayView(game, listeners, processors, textureManager, gameState, texturesProvider);

        return new ViewWithTimedEventLoop(gameplayManager, view, game);
    }
}
