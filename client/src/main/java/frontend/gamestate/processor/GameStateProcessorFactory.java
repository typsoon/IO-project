package frontend.gamestate.processor;

import java.util.Collection;

import frontend.gamestate.DisplayableGameState;
import game.engine.PlayerConfig;
import game.engine.modules.GeometryModule;
import game.gamestates.IGameState;
import viewmodel.game.RenderableObjectFactory;

public class GameStateProcessorFactory {
    public GameStateProcessor getGameStateProcessor(DisplayableGameState gameState, PlayerConfig playerConfig,
            Collection<IGameState> initialGameStates) {
        GeometryModule geometryModule = new GeometryModule();
        RenderableObjectFactory objectFactory = new RenderableObjectFactory(geometryModule);
        return new GameStateProcessor(geometryModule, objectFactory, gameState, playerConfig, initialGameStates);
    }
}
