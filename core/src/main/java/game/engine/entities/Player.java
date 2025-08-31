package game.engine.entities;

import game.actions.Direction;
import game.actions.PlayerSlotUse;
import game.actions.UsageType;
import game.engine.IWorldView;
import game.engine.PlayerConfig;
import game.engine.entities.weapons.IWeapon;
import game.engine.entities.weapons.Sword;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;
import game.gamestates.PlayerState;
import game.utility.Rectangle2F;
import game.utility.Vector2F;

public class Player implements IAIEntity {
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final MoveSet moveset = new MoveSet();

    private final IWeapon weapon = new Sword();
    private final int attackTime = 60; // in ticks
    private int attackClock = 0;

    // should be from file or config
    private final float speed = 8f;
    private final Vector2F sightRange = new Vector2F(10, 10);

    public Player(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, int entityId) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = config.geometryConfigID();
        this.entityGroupID = config.entityGroupID();
    }

    public MoveSet getMoveSet(){
        return moveset;
    }

    @Override
    public void think(IWorldView view) {
        move(moveset.move.direction());
        slotUse(view);
    }

    private void move(Direction direction) {
        geometryRepresentation.move(direction.vector().multiply(speed));
    }

    private void slotUse(IWorldView view) {
        System.out.println(moveset.slotUse.direction());
        geometryRepresentation.setRotation(moveset.slotUse.direction().angle());
        if(moveset.slotUse.usageType() == UsageType.PRIMARY){
            attackClock++;
            if(attackClock >= attackTime){
                weapon.attack(view,this);
                attackClock = 0;
                moveset.slotUse = new PlayerSlotUse(UsageType.NONE, moveset.slotUse.direction(), 0);
            }
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
                EntityAction.Idle);
    }

    public PlayerState getPlayerState() {
        return new PlayerState(
                entityId,
                geometryConfigID,
                geometryRepresentation.getPosition(),
                geometryRepresentation.getVelocity(),
                geometryRepresentation.getRotation(),
                entityGroupID,
                EntityAction.Idle,
                sightRange);
    }

    public Rectangle2F getSightRange() {
        return new Rectangle2F(
                geometryRepresentation.getPosition().subtract(sightRange),
                geometryRepresentation.getPosition().add(sightRange));
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }

}
