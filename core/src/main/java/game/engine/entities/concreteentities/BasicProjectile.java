package game.engine.entities.concreteentities;

import game.engine.IWorldView;
import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.IAIEntity;
import game.engine.entities.IEntity;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.items.attacks.DamageModifier;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;

import java.util.function.Consumer;

public class BasicProjectile implements IAIEntity {
    private final int entityId;
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final Consumer<IEntity> onDeath;

    private int flightRange = 100;

    private final IEntity shooter;
    private final DamageModifier modifiers;

    public BasicProjectile(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                           EntityGroupID entityGroupID, Consumer<IEntity> onDeath, IEntity shooter, DamageModifier modifiers) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = geometryConfigID;
        this.entityGroupID = entityGroupID;
        this.onDeath = onDeath;

        this.shooter = shooter;
        this.modifiers = modifiers;
    }

    @Override
    public void think(IWorldView view) {
        flightRange--;
        if (flightRange <= 0) {
            onDeath.accept(this);
            geometryRepresentation.dispose();
        }
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
                EntityAction.IDLE
        );
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }
}
