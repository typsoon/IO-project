package frontend.concreteviews.gameplayview.gameplaymanager;

import java.io.IOException;
import java.util.logging.Logger;

import frontend.gamestate.updater.IGameStateUpdater;
import network.client.ClientSideSocketWrapper;
import network.messages.Sendable;
import utility.ICyclePerformer;
import viewmodel.IViewManager;

/**
 * This class receives {@link Sendable}s from the server
 */
public class GameplayManager implements ICyclePerformer {
    private final IViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final IGameStateUpdater gameStateUpdater;

    public GameplayManager(ClientSideSocketWrapper clientSideSocketWrapper, IViewManager viewManager,
            IGameStateUpdater gameStateUpdater) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
        this.gameStateUpdater = gameStateUpdater;
    }

    @Override
    public void performCycle() {
        try {
            var sendables = clientSideSocketWrapper.getSendables();

            for (Sendable sendable : sendables) {
                switch (sendable) {

                    default -> throw new IllegalStateException(
                            "We should not have received this message right now %s".formatted(sendable));
                }
            }
        } catch (IOException e) {

            Logger.getGlobal().severe("IOException caught!");
        }

    }
}
