package game.engine.entities.geometry;

import org.yaml.snakeyaml.Yaml;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

public class EntityGeometryConfigFactory {

    private static final Logger logger = Logger.getLogger(EntityGeometryConfigFactory.class.getName());

    private static final String CONFIG_PATH = "geometryconfigs/";

    private static final EntityGeometryConfig DEFAULT_CONFIG = new EntityGeometryConfig(
            1f, 1f, BodyType.DYNAMIC, true, 0.5f, 0.5f, 1f, 0f, 0f);

    private static final EnumMap<GeometryConfigID, EntityGeometryConfig> geometryConfigMap;

    static {
        geometryConfigMap = new EnumMap<>(GeometryConfigID.class);
        for (GeometryConfigID id : GeometryConfigID.values()) {
            geometryConfigMap.put(id, loadConfig(id));
        }
    }

    // TODO:
    // this is temporary solution
    // problems
    // record don't have default constructor (pojo is resolving it but i don't think
    // its good solution)
    // default space for resources in java is src/main/resources not assets/
    private static EntityGeometryConfig loadConfig(GeometryConfigID id) {
        String filename = CONFIG_PATH + id.name().toLowerCase() + ".yaml";
        var file = EntityGeometryConfigFactory.class.getClassLoader().getResourceAsStream(filename);

        if (file == null) {
            logger.warning("File not found: " + filename);
            return DEFAULT_CONFIG;
        }

        Yaml yaml = new Yaml();
        // try (InputStream in = new FileInputStream(file)) {
        // Map<String, Object> map = yaml.load(in);
        Map<String, Object> map = yaml.load(file);
        return new EntityGeometryConfig(
                getFloat(map, "width", DEFAULT_CONFIG.width()),
                getFloat(map, "height", DEFAULT_CONFIG.height()),
                getBodyType(map, "bodyType", DEFAULT_CONFIG.bodyType()),
                getBoolean(map, "isRotatable", DEFAULT_CONFIG.isRotatable()),
                getFloat(map, "friction", DEFAULT_CONFIG.friction()),
                getFloat(map, "restitution", DEFAULT_CONFIG.restitution()),
                getFloat(map, "density", DEFAULT_CONFIG.density()),
                getFloat(map, "linearDamping", DEFAULT_CONFIG.linearDamping()),
                getFloat(map, "angularDamping", DEFAULT_CONFIG.angularDamping()));
    }

    // catch (Exception e) {
    // logger.warning("Error while loading file " + file.getAbsolutePath() + ": " +
    // e.getMessage());
    // return DEFAULT_CONFIG;
    // }
    // }

    private static float getFloat(Map<String, Object> map, String key, float defaultValue) {
        Object val = map.get(key);
        if (val instanceof Number n)
            return n.floatValue();
        return defaultValue;
    }

    private static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        Object val = map.get(key);
        if (val instanceof Boolean b)
            return b;
        return defaultValue;
    }

    private static BodyType getBodyType(Map<String, Object> map, String key, BodyType defaultValue) {
        Object val = map.get(key);
        if (val instanceof String s) {
            try {
                return BodyType.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        return defaultValue;
    }

    public static EntityGeometryConfig createEntityGeometryConfig(GeometryConfigID geometryConfigID) {
        EntityGeometryConfig config = geometryConfigMap.get(geometryConfigID);
        if (config == DEFAULT_CONFIG) {
            logger.warning("No geometry config found for ID: " + geometryConfigID);
            return DEFAULT_CONFIG;
        }
        return config;
    }
}
