package game.engine;

import game.engine.components.EntityFactory;
import game.engine.components.GeometryModuleImplementation;

import java.util.Collection;

public class GameEngineFactory {
    public static GameEngine createGameEngine(Collection<EnginePlayerData> players) {
        GeometryModuleImplementation geometryModule = new GeometryModuleImplementation();
        EntityFactory entityFactory = new EntityFactory(geometryModule);
        return new GameEngineImplementation(
                players,
                geometryModule,
                entityFactory,
                java.util.List.of(geometryModule)
        );
    }
}
