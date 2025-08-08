package frontend.concreteviews.gameplayview.gameplaymanager;

import frontend.gamestate.IDisplayableGameState;
import frontend.gamestate.processor.GameStateProcessorFactory;
import network.client.ClientSideSocketWrapper;
import viewmodel.IViewManager;

public class GameplayManagerFactory {
    public GameplayManager getGameplayManager(IViewManager viewManager,
                                              ClientSideSocketWrapper clientSideSocketWrapper,
                                              IDisplayableGameState displayableGameState) {
        var processor = new GameStateProcessorFactory().getGameStateProcessor(displayableGameState);
        return new GameplayManager(clientSideSocketWrapper, viewManager, processor);
    }
}
