package game.engine.entities;

import game.engine.modules.IGeometryRepresentation;
import game.gamestates.EntityState;

public interface IEntity {
    EntityState getEntityState();

    IGeometryRepresentation geometryRepresentation();
}
