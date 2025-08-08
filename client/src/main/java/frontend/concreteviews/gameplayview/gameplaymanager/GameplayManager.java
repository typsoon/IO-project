package frontend.concreteviews.gameplayview.gameplaymanager;

import java.io.IOException;
import java.util.logging.Logger;

import frontend.gamestate.processor.IGameStateProcessor;
import network.client.ClientSideSocketWrapper;
import game.utility.ISendable;
import game.gamestates.IGameState;
import utility.ICyclePerformer;
import viewmodel.IViewManager;

/**
 * This class receives {@link ISendable}s from the server
 */
public class GameplayManager implements ICyclePerformer {
    private final IViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final IGameStateProcessor gameStateProcessor;

    public GameplayManager(ClientSideSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            IGameStateProcessor gameStateProcessor) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.gameStateProcessor = gameStateProcessor;
    }

    @Override
    public void performCycle() {
        try {
            var sendables = clientSideSocketWrapper.getSendables();

            for (ISendable sendable : sendables) {
                switch (sendable) {
                    case IGameState gameState -> gameStateProcessor.processGameState(gameState);
                    default -> throw new IllegalStateException(
                            "We should not have received this message right now %s".formatted(sendable));
                }
            }
        } catch (IOException e) {

            Logger.getGlobal().severe("IOException caught!");
        }

    }
}
