package viewmodel.game;

import game.engine.modules.IGeometrySystem;

public class GameManagerFactory {
    public static IClientGameManager createGameManager(IGeometrySystem geometrySystem, IEntityViewFactory entityViewFactory) {
        return new ClientGameManager(geometrySystem, entityViewFactory);
    }
}
