package game.engine.entities.concreteentities;

import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.IEntity;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.inventory.IHaveInventory;
import game.engine.entities.inventory.ResourceInfo;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.IDamageable;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;

import java.util.function.Consumer;

public class ResourceDeposit implements IEntity, IDamageable {

    private final IManagingGeometryRepresentation geometryRepresentation;
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final Consumer<IEntity> onDeath;

    private final ResourceInfo resourceInfo;
    private int health;

    public ResourceDeposit(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                           EntityGroupID entityGroupID, Consumer<IEntity> onDeath, int health, ResourceInfo resourceInfo) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = geometryConfigID;
        this.entityGroupID = entityGroupID;
        this.onDeath = onDeath;

        this.health = health;
        this.resourceInfo = resourceInfo;
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
            if (source instanceof IHaveInventory inventory) {
                inventory.getInventory().addResource(resourceInfo);
            }
            geometryRepresentation.dispose();
            onDeath.accept(this);
        }
    }
}
