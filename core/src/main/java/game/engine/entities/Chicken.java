package game.engine.entities;

import game.engine.IWorldView;
import game.engine.entities.behaviours.IBehaviour;
import game.engine.entities.items.attacks.Damage;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;

import java.util.function.Consumer;

public class Chicken implements IAIEntity, IDamageable {
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final IBehaviour behaviour;

    private final Consumer<IEntity> onDeath;

    private int health = 50;

    public Chicken(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                   EntityGroupID entityGroupID, IBehaviour behaviour) {
        this(geometryRepresentation, entityId, geometryConfigID, entityGroupID, behaviour, entity -> {
        });
    }

    public Chicken(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                   EntityGroupID entityGroupID, IBehaviour behaviour, Consumer<IEntity> onDeath) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = geometryConfigID;
        this.entityGroupID = entityGroupID;
        this.behaviour = behaviour;
        this.onDeath = onDeath;
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
                EntityAction.IDLE
        );
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }

    @Override
    public void takeDamage(Damage damage, IEntity source) {
        health -= damage.value();
        if (health <= 0) {
            geometryRepresentation.dispose();
            onDeath.accept(this);
        }
    }
}
