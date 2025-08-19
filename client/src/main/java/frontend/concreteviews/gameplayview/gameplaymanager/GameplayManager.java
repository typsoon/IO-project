package frontend.concreteviews.gameplayview.gameplaymanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import frontend.gamestate.processor.IGameStateProcessor;
import game.gamestates.IGameState;
import game.utility.ISendable;
import network.client.DuplexSocketWrapper;
import utility.ICycleTimedPerformer;
import viewmodel.IViewManager;

/**
 * This class receives {@link ISendable}s from the server
 */
public class GameplayManager implements ICycleTimedPerformer {
    private final IViewManager viewManager;
    private final DuplexSocketWrapper clientSideSocketWrapper;
    private final IGameStateProcessor gameStateProcessor;

    public GameplayManager(DuplexSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            IGameStateProcessor gameStateProcessor) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.gameStateProcessor = gameStateProcessor;
    }

    @Override
    public void performCycle(float deltaTime) {
        Collection<IGameState> gameStates = new ArrayList<>();
        try {
            var sendables = clientSideSocketWrapper.getSendables();
            for (ISendable sendable : sendables) {
                switch (sendable) {
                    case IGameState gameState -> gameStates.add(gameState);
                    default -> throw new IllegalStateException(
                            "We should not have received this message right now %s".formatted(sendable));
                }
            }
        } catch (IOException e) {

            Logger.getGlobal().severe("IOException caught!");
        }
        gameStateProcessor.processGameStates(gameStates, deltaTime);
    }
}
