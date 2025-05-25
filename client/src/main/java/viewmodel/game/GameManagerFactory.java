package viewmodel.game;

import game.engine.modules.GeometryModule;
import game.engine.modules.GeometryModuleImplementation;

public class GameManagerFactory {
    public static ClientGameManager createGameManager(GeometryModuleImplementation geometryModule) {
        return new ClientGameManagerImplementation(geometryModule);
    }
}
