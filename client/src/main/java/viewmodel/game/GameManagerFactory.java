package viewmodel.game;

import game.engine.modules.GeometryModule;
import game.engine.modules.GeometryModuleImplementation;
import game.engine.modules.GeometrySystem;

public class GameManagerFactory {
    public static ClientGameManager createGameManager(GeometrySystem geometrySystem, EntityViewFactory entityViewFactory) {
        return new ClientGameManagerImplementation(geometrySystem, entityViewFactory);
    }
}
