package viewmodel.game;

import game.engine.modules.GeometrySystem;
import viewmodel.game.ClientGameManagerImpl;

public class GameManagerFactory {
    public static ClientGameManager createGameManager(GeometrySystem geometrySystem, EntityViewFactory entityViewFactory) {
        return new ClientGameManagerImpl(geometrySystem, entityViewFactory);
    }
}
