package game.engine;

import game.engine.entities.EntityFactory;
import game.engine.modules.GeometryModuleImplementation;

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
