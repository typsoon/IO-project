package game.engine.entities;

import game.engine.modules.IManagingGeometryRepresentation;

public record DeathData(
        IEntity entity,
        IManagingGeometryRepresentation geometryRepresentation
) {
}
