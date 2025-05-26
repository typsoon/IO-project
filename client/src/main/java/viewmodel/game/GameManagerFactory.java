package viewmodel.game;

import game.engine.modules.GeometrySystem;

public class GameManagerFactory {
    public static IClientGameManager createGameManager(GeometrySystem geometrySystem, IEntityViewFactory entityViewFactory) {
        return new ClientGameManager(geometrySystem, entityViewFactory);
    }
}
