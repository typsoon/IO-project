package game.engine.entities;

import game.engine.PlayerConfig;
import game.engine.modules.IGeometryFactory;

public class EntityFactory {
    private int nextEntityId = 0;
    private final IGeometryFactory geometryFactory;
    public EntityFactory(IGeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }
    public Player createPlayer(PlayerConfig playerConfig, float startingX, float startingY) {
        return new Player(playerConfig,geometryFactory.createGeometryRepresentation(
                EntityGeometryConfigFactory.createEntityGeometryConfig(GeometryConfigID.HUMAN),
                startingX, startingY),nextEntityId++);
    }
}
