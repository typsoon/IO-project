package game.engine.entities.concreteentities;

import game.engine.IWorldView;
import game.engine.entities.*;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.geometry.ICollisionAware;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.DamageModifier;
import game.engine.entities.items.attacks.DamageType;
import game.engine.entities.items.attacks.IDamageable;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;

import java.util.function.Consumer;

public class BasicProjectile implements IAIEntity, ICollisionAware {
    private final int entityId;
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final Consumer<DeathData> onDeath;

    private int flightRange = 100;

    private final IEntity shooter;
    private final DamageModifier modifiers;
    private final Damage dmg = new Damage(DamageType.PIERCE, 20);

    public BasicProjectile(IManagingGeometryRepresentation geometryRepresentation, int entityId, GeometryConfigID geometryConfigID,
                           EntityGroupID entityGroupID, Consumer<DeathData> onDeath, IEntity shooter, DamageModifier modifiers) {
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
            onDeath.accept(new DeathData(this, geometryRepresentation));
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
    public void onCollisionBegin(IEntity other) {
        if (other instanceof IDamageable damageable) {
            if (other != shooter) {
                damageable.takeDamage(modifiers.modify(dmg, damageable), shooter);
                onDeath.accept(new DeathData(this, geometryRepresentation));
            }
        }
    }

    @Override
    public void onCollisionEnd(IEntity other) {

    }
}
