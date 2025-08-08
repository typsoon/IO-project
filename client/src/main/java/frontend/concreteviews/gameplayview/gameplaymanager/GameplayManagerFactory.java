package frontend.concreteviews.gameplayview.gameplaymanager;

import frontend.gamestate.IDisplayableGameState;
import frontend.gamestate.updater.GameStateUpdaterFactory;
import network.client.ClientSideSocketWrapper;
import viewmodel.IViewManager;

public class GameplayManagerFactory {
    public GameplayManager getGameplayManager(IViewManager viewManager,
                                              ClientSideSocketWrapper clientSideSocketWrapper,
                                              IDisplayableGameState displayableGameState) {
        var updater = new GameStateUpdaterFactory().getGameStateUpdater(displayableGameState);
        return new GameplayManager(clientSideSocketWrapper, viewManager, updater);
    }
}
