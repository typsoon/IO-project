package frontend.concreteviews.gameplayview.gameplaymanager;

import java.util.Collection;

import frontend.gamestate.DisplayableGameState;
import frontend.gamestate.processor.GameStateProcessor;
import frontend.gamestate.processor.GameStateProcessorFactory;
import game.engine.PlayerConfig;
import game.gamestates.IGameState;
import network.client.DuplexSocketWrapper;
import viewmodel.IViewManager;

public class GameplayManagerFactory {
    public GameplayManager getGameplayManager(IViewManager viewManager,
            DuplexSocketWrapper clientSideSocketWrapper,
            DisplayableGameState gameState,
            PlayerConfig playerConfig, Collection<IGameState> initialGameStates) {

        GameStateProcessor gameStateProcessor = new GameStateProcessorFactory().getGameStateProcessor(gameState,
                playerConfig, initialGameStates);

        return new GameplayManager(clientSideSocketWrapper, viewManager, gameStateProcessor);
    }
}
