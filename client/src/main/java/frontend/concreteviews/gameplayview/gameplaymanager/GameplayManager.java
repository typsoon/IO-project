package frontend.concreteviews.gameplayview.gameplaymanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import frontend.gamestate.processor.IGameStateProcessor;
import game.gamestates.IGameState;
import network.client.DuplexSocketWrapper;
import utility.ICycleTimedPerformer;
import utils.ISendable;
import viewmodel.IViewManager;

/**
 * This class receives {@link ISendable}s from the server
 */
public class GameplayManager implements ICycleTimedPerformer {
    @SuppressWarnings("unused")
    private final IViewManager viewManager;
    private final DuplexSocketWrapper clientSideSocketWrapper;
    private final IGameStateProcessor gameStateProcessor;

    public GameplayManager(DuplexSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            IGameStateProcessor gameStateProcessor) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.gameStateProcessor = gameStateProcessor;
    }

    // private long lastLogTimeNs = 0;
    // private long second = 1_000_000_000L;

    @Override
    public void performCycle(float deltaTime) {
        // long now = System.nanoTime();

        // if (now - lastLogTimeNs > second) {
        // double secondsSinceLast = (now - lastLogTimeNs) / second;
        // Logger.getGlobal().info(
        // String.format("performCycle called, last call was %.3f seconds ago",
        // secondsSinceLast));
        // lastLogTimeNs = now;
        // }
        //
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
