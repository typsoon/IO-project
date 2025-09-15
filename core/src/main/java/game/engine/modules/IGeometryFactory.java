package game.engine.modules;

import game.engine.entities.geometry.EntityGeometryConfig;

public interface IGeometryFactory {
    IManagingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY, boolean isSensor);

    default IManagingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY) {
        return createGeometryRepresentation(config, startingX, startingY, false);
    }
}
