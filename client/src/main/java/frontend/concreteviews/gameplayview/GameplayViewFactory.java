package frontend.concreteviews.gameplayview;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithEventLoop;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerFactory;
import frontend.gamestate.DisplayableGameState;
import frontend.gamestate.IDisplayableGameState;
import network.client.ClientSideSocketWrapper;
import viewmodel.ITextureManager;
import viewmodel.IView;
import viewmodel.IViewManager;

public class GameplayViewFactory {
    IView getGameplayView(
            Game game, IViewManager viewManager, ClientSideSocketWrapper clientSideSocketWrapper,
            ITextureManager textureManager) {

        IDisplayableGameState gameState = new DisplayableGameState();

        var gameplayManager = new GameplayManagerFactory().getGameplayManager(viewManager, clientSideSocketWrapper,
                gameState);

        // see other screens (Login, GameClient) for clues about how these work
        var listeners = new ArrayList<EventListener>();

        // see libgdx docs
        var processors = new ArrayList<InputProcessor>();

        var view = new GameplayView(game, listeners, processors, textureManager, gameState);

        return new ViewWithEventLoop(gameplayManager, view, game);
    }
}
