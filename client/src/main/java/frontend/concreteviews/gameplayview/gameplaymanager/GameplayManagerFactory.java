package frontend.concreteviews.gameplayview.gameplaymanager;

import frontend.gamestate.processor.GameStateProcessor;
import network.client.ClientSideSocketWrapper;
import viewmodel.IViewManager;

public class GameplayManagerFactory {
    public GameplayManager getGameplayManager(IViewManager viewManager,
                                              ClientSideSocketWrapper clientSideSocketWrapper,
                                              GameStateProcessor processor){
        return new GameplayManager(clientSideSocketWrapper, viewManager, processor);
    }
}
