package frontend.concreteviews.gameplayview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.ViewWithTimedEventLoop;
import frontend.assetsloading.ITextureManager;
import frontend.assetsloading.TexturesProvider;
import frontend.concreteviews.gameplayview.gameplaymanager.GameplayManagerFactory;
import frontend.concreteviews.gameplayview.listeners.ExitTheGameEventListener;
import frontend.concreteviews.gameplayview.processors.ProcessorFactoriesCreator;
import frontend.gamestate.DisplayableGameState;
import game.engine.PlayerConfig;
import game.gamestates.IGameState;
import game.session.ISendableConsumer;
import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper.ConnectionEndedException;
import network.messages.defaultmessage.ConcreteObjectDecoder;
import network.messages.defaultmessage.ObjectToMessageDecoder;
import utility.IActionSender;
import utility.ICycleTimedPerformer;
import utils.ObserverWithATwist.ObserverImpl;
import viewmodel.IView;
import viewmodel.IViewManager;

public class GameplayViewFactory {
    public IView getGameplayView(
            final Game game, final IViewManager viewManager, final ClientSideSocketWrapper clientSideSocketWrapper,
            final ITextureManager textureManager, final PlayerConfig playerConfig,
            final TexturesProvider texturesProvider, Collection<IGameState> initialGameStates, int id) {

        final DisplayableGameState gameState = new DisplayableGameState();

        final var gameCycles = new ObserverImpl();
        final var gameplayManager = new GameplayManagerFactory().getGameplayManager(viewManager,
                clientSideSocketWrapper,
                gameState, playerConfig, initialGameStates);

        final ICycleTimedPerformer cyclePerformer = deltaTime -> {
            gameCycles.notifySubscribers(deltaTime);
            gameplayManager.performCycle(deltaTime);
        };

        // see other screens (Login, GameClient) for clues about how these work

        final ObjectToMessageDecoder objectDecoder = new ConcreteObjectDecoder();
        final ISendableConsumer sendableConsumer = action -> {
            try {
                final var msg = objectDecoder.decodeFromRecord(action);

                Logger.getGlobal().finer("Message dispatched. Payload: %s".formatted(action));
                // Logger.getGlobal().info("Message dispatched. Payload: %s".formatted(action));
                clientSideSocketWrapper.dispatchMessage(msg);
            } catch (final ConnectionEndedException e) {
                Logger.getGlobal().severe("Connection ended here %s".formatted(e));
                throw new IllegalStateException("Connection ended", e);
            } catch (final IOException e) {
                Logger.getGlobal().severe("IOException caught here %s".formatted(e));
                throw new IllegalStateException("Error while sending message", e);
            } catch (final Throwable e) {
                Logger.getGlobal().severe("Throwable caught here %s".formatted(e));
            }
        };

        final var listeners = new ArrayList<EventListener>();
        listeners.add(new ExitTheGameEventListener(() -> viewManager.moveToGameClient(clientSideSocketWrapper, id),
                sendableConsumer));

        final IActionSender actionSender = action -> sendableConsumer.processSendable(action);

        // see libgdx docs
        final var processorsFactories = new ProcessorFactoriesCreator(actionSender).getProcessorsFactories(gameState);
        final var view = new GameplayView(game, listeners, processorsFactories, textureManager, gameState,
                texturesProvider, gameCycles);

        return new ViewWithTimedEventLoop(cyclePerformer, view, game);
    }
}
