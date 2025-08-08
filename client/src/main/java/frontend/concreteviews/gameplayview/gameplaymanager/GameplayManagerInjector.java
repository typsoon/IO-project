package frontend.concreteviews.gameplayview.gameplaymanager;

import frontend.gamestate.DisplayableGameState;
import frontend.gamestate.updater.GameStateUpdaterInjector;
import network.client.ClientSideSocketWrapper;
import viewmodel.ViewManager;

public class GameplayManagerInjector {
    public GameplayManager getGameplayManager(ViewManager viewManager,
            ClientSideSocketWrapper clientSideSocketWrapper,
            DisplayableGameState displayableGameState) {
        var updater = new GameStateUpdaterInjector().getGameStateUpdater(displayableGameState);
        return new GameplayManager(clientSideSocketWrapper, viewManager, updater);
    }
}
