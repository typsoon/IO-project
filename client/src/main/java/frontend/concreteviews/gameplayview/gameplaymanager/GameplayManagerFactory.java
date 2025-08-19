package frontend.concreteviews.gameplayview.gameplaymanager;

import frontend.gamestate.DisplayableGameState;
import frontend.gamestate.processor.GameStateProcessor;
import frontend.gamestate.processor.GameStateProcessorFactory;
import game.engine.PlayerConfig;
import network.client.DuplexSocketWrapper;
import viewmodel.IViewManager;

public class GameplayManagerFactory {
    public GameplayManager getGameplayManager(IViewManager viewManager,
            DuplexSocketWrapper clientSideSocketWrapper,
            DisplayableGameState gameState,
            PlayerConfig playerConfig) {

        GameStateProcessor gameStateProcessor = new GameStateProcessorFactory().getGameStateProcessor(gameState,
                playerConfig);

        return new GameplayManager(clientSideSocketWrapper, viewManager, gameStateProcessor);
    }
}
