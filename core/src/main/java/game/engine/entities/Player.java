package game.engine.entities;

import game.actions.Direction;
import game.actions.PlayerSlotUse;
import game.engine.IWorldView;
import game.engine.PlayerConfig;
import game.engine.entities.inventory.IInventory;
import game.engine.entities.inventory.Inventory;
import game.engine.entities.items.UsageModifiers;
import game.engine.entities.items.items.BasicSword;
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

    private final IInventory inventory;
    private final UsageModifiers modifiers;


    // should be from file or config
    private final float speed = 8f;
    private final Vector2F sightRange = new Vector2F(10, 10);

    public Player(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, int entityId) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = config.geometryConfigID();
        this.entityGroupID = config.entityGroupID();
        this.inventory = new Inventory(1);
        inventory.getSlot(0).addItems(1, new BasicSword());
        this.modifiers = () -> Damage -> Damage;
    }

    public MoveSet getMoveSet() {
        return moveset;
    }

    @Override
    public void think(IWorldView view) {
//        System.out.println(moveset.slotUse);
        move(moveset.move.direction());
        slotUse(view);
    }

    private void move(Direction direction) {
        geometryRepresentation.move(direction.vector().multiply(speed));
    }

    private void slotUse(IWorldView view) {
        geometryRepresentation.setRotation(moveset.slotUse.direction().angle());
        PlayerSlotUse ps = moveset.slotUse;
        inventory.useSlot(ps.slot(), ps.usageType(), view, this, modifiers);
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
                EntityAction.IDLE);
    }

    public PlayerState getPlayerState() {
        return new PlayerState(
                entityId,
                geometryConfigID,
                geometryRepresentation.getPosition(),
                geometryRepresentation.getVelocity(),
                geometryRepresentation.getRotation(),
                entityGroupID,
                EntityAction.IDLE,
                sightRange);
//                inventory.getInventoryInfo());
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
