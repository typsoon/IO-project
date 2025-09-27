package game.engine.entities.concreteentities;

import game.engine.IWorldView;
import game.engine.entities.*;
import game.engine.entities.behaviours.IBehaviour;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.IDamageable;
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

    private final Consumer<DeathData> onDeath;

    private int health = 50;

    public Chicken(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                   EntityGroupID entityGroupID, IBehaviour behaviour) {
        this(geometryRepresentation, entityId, geometryConfigID, entityGroupID, behaviour, entity -> {
        });
    }

    public Chicken(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                   EntityGroupID entityGroupID, IBehaviour behaviour, Consumer<DeathData> onDeath) {
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
                EntityAction.NONE,
                0,
                EntityGroupID.HUMAN_BASIC,
                EntityAction.NONE,
                0
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
            onDeath.accept(new DeathData(this, geometryRepresentation));
        }
    }
}
