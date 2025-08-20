package game.engine.entities;

import game.engine.IWorldView;
import game.engine.entities.behaviours.IBehaviour;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;

public class Chicken implements IAIEntity{
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final IBehaviour behaviour;

    public Chicken(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID, EntityGroupID entityGroupID,IBehaviour behaviour) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = geometryConfigID;
        this.entityGroupID = entityGroupID;
        this.behaviour = behaviour;
    }

    @Override
    public void think(IWorldView view) {
        behaviour.behave(geometryRepresentation, view);
    }

    @Override
    public EntityState getEntityState() {
        return new EntityState(
                entityId,
                geometryConfigID,
                geometryRepresentation.getPosition(),
                geometryRepresentation.getVelocity(),
                geometryRepresentation.getRotation(),
                entityGroupID,
                EntityAction.Idle
        );
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }
}
