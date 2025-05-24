package game.engine.modules;

import game.engine.entities.EntityGeometryConfig;

public interface GeometryFactory {
    MovingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY);
}
