package frontend.concreteviews.gameplayview.gameplaymanager;

import java.io.IOException;
import java.util.logging.Logger;

import frontend.gamestate.updater.GameStateUpdater;
import network.client.ClientSideSocketWrapper;
import network.messages.Sendable;
import utility.CyclePerformer;
import viewmodel.ViewManager;

/**
 * This class receives {@link Sendable}s from the server
 */
public class GameplayManager implements CyclePerformer {
    private final ViewManager viewManager;
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final GameStateUpdater gameStateUpdater;

    public GameplayManager(ClientSideSocketWrapper clientSideSocketWrapper, ViewManager viewManager,
            GameStateUpdater gameStateUpdater) {
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
