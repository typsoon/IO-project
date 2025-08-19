package frontend.gamestate.processor;

import frontend.gamestate.DisplayableGameState;
import game.engine.PlayerConfig;
import game.engine.modules.GeometryModule;
import viewmodel.game.RenderableObjectFactory;

public class GameStateProcessorFactory {
    public GameStateProcessor getGameStateProcessor(DisplayableGameState gameState, PlayerConfig playerConfig) {
    GeometryModule geometryModule = new GeometryModule();
    RenderableObjectFactory objectFactory = new RenderableObjectFactory(geometryModule);
    return new GameStateProcessor(geometryModule, objectFactory, gameState, playerConfig);
    }
}
