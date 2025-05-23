package game.engine.components;

import game.engine.PlayerConfig;

import java.util.EnumMap;
import java.util.Map;

public class EntityFactory {

    private final GeometryFactory geometryFactory;
    private final Map<GeometryConfigID,EntityGeometryConfig> geometryConfigMap = new EnumMap<>(GeometryConfigID.class);
    public EntityFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        //todo loader for geometry configs from files
        //this is placeholder for now (need to configure those values)
        geometryConfigMap.put(GeometryConfigID.HUMAN, new EntityGeometryConfig(
                10,
                10,
                1,
                1,
                BodyType.DYNAMIC,
                0.5f,
                0.5f,
                0.5f,
                0.5f,
                0.5f
        ));
    }
    public Player createPlayer(PlayerConfig playerConfig) {
        return new Player(playerConfig,geometryFactory.createGeometryRepresentation(geometryConfigMap.get(GeometryConfigID.HUMAN)));
    }
}
