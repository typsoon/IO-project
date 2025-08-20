package frontend.concreteviews.gameplayview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithTimedEventLoop;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerFactory;
import frontend.concreteviews.gameplayview.processors.PlayerMovementProcessor;
import frontend.gamestate.DisplayableGameState;
import game.engine.PlayerConfig;
import network.client.DuplexSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import utility.IActionSender;
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

        ObjectToMessageDecoder objectDecoder = new ConcreteObjectDecoder();
        IActionSender actionSender = action -> {
            var msg = objectDecoder.decodeFromRecord(action);
            try {
                Logger.getGlobal().finer("Message dispatched. Payload: %s".formatted(action));
                clientSideSocketWrapper.dispatchMessage(msg);
            } catch (ConnectionEndedException e) {
                throw new IllegalStateException("Connection ended", e);
            } catch (IOException e) {
                throw new IllegalStateException("Error while sending message", e);
            }
        };

        // see libgdx docs
        Collection<InputProcessor> processors = List.of(new PlayerMovementProcessor(actionSender));

        var view = new GameplayView(game, listeners, processors, textureManager, gameState, texturesProvider);

        return new ViewWithTimedEventLoop(gameplayManager, view, game);
    }
}
