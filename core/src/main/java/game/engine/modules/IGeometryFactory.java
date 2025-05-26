package game.engine.modules;

import game.engine.entities.EntityGeometryConfig;

public interface IGeometryFactory {
    IManagingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY);
}
