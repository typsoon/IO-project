package game.engine;

import game.engine.entities.EntityFactory;
import game.engine.modules.GeometryModule;

import java.util.Collection;

public class GameEngineFactory {
    public static IGameEngine createGameEngine(Collection<EnginePlayerData> players) {
        GeometryModule geometryModule = new GeometryModule();
        EntityFactory entityFactory = new EntityFactory(geometryModule);
        return new GameEngine(
                players,
                geometryModule,
                entityFactory,
                java.util.List.of(geometryModule)
        );
    }
}
