package viewmodel.game;

import game.engine.entities.BodyType;
import game.engine.entities.EntityGeometryConfig;
import game.engine.entities.GeometryConfigID;

import java.util.Map;


//TODO this is a temporary solution, should be replaced with a proper configuration system
//and used both in client and server game engine
public class EntityGeometryConfigFactory {
    public static EntityGeometryConfig createEntityGeometryConfig(GeometryConfigID geometryConfigID) {
        EntityGeometryConfig config = geometryConfigMap.get(geometryConfigID);
        if (config == null) {
            throw new IllegalArgumentException("No geometry config found for ID: " + geometryConfigID);
        }
        return config;
    }

    private static final Map<GeometryConfigID, EntityGeometryConfig> geometryConfigMap = Map.of(
            GeometryConfigID.HUMAN, new EntityGeometryConfig(
                    1,
                    1,
                    BodyType.DYNAMIC,
                    false,
                    0f,
                    0.0f,
                    1,
                    50.0f,
                    5.0f
            )
    );
}
