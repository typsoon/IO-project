package game.engine.entities;

import game.engine.modules.GeometryRepresentation;
import game.gamestates.EntityState;

public interface Entity {
    EntityState getEntityState();
    GeometryRepresentation geometryRepresentation();
}
