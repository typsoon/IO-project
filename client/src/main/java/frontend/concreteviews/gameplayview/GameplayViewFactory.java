package frontend.concreteviews.gameplayview;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithEventLoop;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerInjector;
import frontend.gamestate.DisplayableGameState;
import network.client.ClientSideSocketWrapper;
import viewmodel.TextureManager;
import viewmodel.View;
import viewmodel.ViewManager;

public class GameplayViewFactory {
    View getGameplayView(
            Game game, ViewManager viewManager, ClientSideSocketWrapper clientSideSocketWrapper,
            TextureManager textureManager) {

        // TODO: create with factory, injector or constructor (idk if DI will be needed,
        // it depends on the contents of DisplayableGameState)
        DisplayableGameState gameState = null;

        var gameplayManager = new GameplayManagerInjector().getGameplayManager(viewManager, clientSideSocketWrapper,
                gameState);

        // see other screens (Login, GameClient) for clues about how these work
        var listeners = new ArrayList<EventListener>();

        // see libgdx docs
        var processors = new ArrayList<InputProcessor>();

        var view = new GameplayView(game, listeners, processors, textureManager, gameState);

        return new ViewWithEventLoop(gameplayManager, view, game);
    }
}
