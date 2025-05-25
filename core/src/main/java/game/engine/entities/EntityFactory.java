package game.engine.entities;

import game.engine.PlayerConfig;
import game.engine.modules.GeometryFactory;

import java.util.EnumMap;
import java.util.Map;

public class EntityFactory {
    private int nextEntityId = 0;
    private final GeometryFactory geometryFactory;
    private final Map<GeometryConfigID, EntityGeometryConfig> geometryConfigMap = new EnumMap<>(GeometryConfigID.class);
    public EntityFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        //todo loader for geometry configs from files
        //this is placeholder for now (need to configure those values)
        geometryConfigMap.put(GeometryConfigID.HUMAN, new EntityGeometryConfig(
                1,
                1,
                BodyType.DYNAMIC,
                false,
                0f,
                0.0f,
                1,
                50.0f,
                5.0f
        ));
    }
    public Player createPlayer(PlayerConfig playerConfig, float startingX, float startingY) {
        return new Player(playerConfig,geometryFactory.createGeometryRepresentation(
                geometryConfigMap.get(GeometryConfigID.HUMAN),
                startingX, startingY),nextEntityId++);
    }
}
