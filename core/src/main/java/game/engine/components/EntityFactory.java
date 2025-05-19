package game.engine.components;

import game.engine.PlayerConfig;

import java.util.EnumMap;
import java.util.Map;

public class EntityFactory {

    private final GeometryFactory geometryFactory;
    private final Map<GeometryConfigID,EntityGeometryConfig> geometryConfigMap = new EnumMap<>(GeometryConfigID.class);
    EntityFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        //todo loader for geometry configs from files
    }
    public Entity createPlayer(PlayerConfig playerConfig) {
        return new Player(playerConfig,geometryFactory.createGeometryRepresentation(geometryConfigMap.get(GeometryConfigID.HUMAN)));
    }
}
