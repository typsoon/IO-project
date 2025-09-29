package game.engine.entities.concreteentities;

import game.actions.Direction;
import game.actions.InteractionType;
import game.actions.PlayerInteraction;
import game.actions.PlayerSlotUse;
import game.engine.IWorldView;
import game.engine.PlayerConfig;
import game.engine.entities.*;
import game.engine.entities.geometry.GeometryConfigID;
import game.engine.entities.inventory.IHaveInventory;
import game.engine.entities.inventory.IInventory;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.IDamageable;
import game.engine.entities.upgradetree.IUpgradeTree;
import game.engine.modules.IGeometryRepresentation;
import game.engine.modules.IManagingGeometryRepresentation;
import game.gamestates.EntityState;
import game.gamestates.PlayerInventoryState;
import game.gamestates.PlayerState;
import game.gamestates.UpgradeTreeState;
import game.utility.Point2F;
import game.utility.Rectangle2F;
import game.utility.Vector2F;

import java.util.function.Consumer;

public class Player implements IAIEntity, IHaveInventory, IDamageable {
    private final int entityId;
    private final GeometryConfigID geometryConfigID;
    private final EntityGroupID entityGroupID;
    private final IManagingGeometryRepresentation geometryRepresentation;
    private final MoveSet moveset = new MoveSet();

    private final IInventory inventory;
    private IUsageModifiers modifiers;
    private final Consumer<DeathData> onDeath;

    // should be from file or config
    private final int maxHp = 100;
    private int currentHp = 100;
    private final float speed = 8f;
    private final Vector2F sightRange = new Vector2F(10, 10);
    private final float interactRange = 2f;

    private IInteractable currentInteraction = null;

    private IUpgradeTree upgradeTree;

    public Player(PlayerConfig config, IManagingGeometryRepresentation geometryRepresentation, int entityId, Consumer<DeathData> onDeath,
                  IInventory inventory) {
        this.geometryRepresentation = geometryRepresentation;
        this.entityId = entityId;
        this.geometryConfigID = config.geometryConfigID();
        this.entityGroupID = config.entityGroupID();
        this.inventory = inventory;
        this.onDeath = onDeath;
    }

    public MoveSet getMoveSet() {
        return moveset;
    }

    public boolean isInteracting() {
        return currentInteraction != null;
    }

    public IInteractable getCurrentInteraction() {
        return currentInteraction;
    }

    public void setUsageModifiers(IUsageModifiers usageModifiers) {
        this.modifiers = usageModifiers;
    }

    @Override
    public void think(IWorldView view) {
        modifiers.setWorldView(view);
        move(moveset.move.direction());
        slotUse(view);
        interact(view);
    }

    private void move(Direction direction) {
        geometryRepresentation.move(direction.vector().multiply(speed));
    }

    private void slotUse(IWorldView view) {
        geometryRepresentation.setRotation(moveset.slotUse.direction().angle());
        PlayerSlotUse ps = moveset.slotUse;
        inventory.useSlot(ps.slot(), ps.usageType(), view, this, modifiers);
    }

    private void interact(IWorldView view) {
        if (currentInteraction != null && Point2F.distance(currentInteraction.getGeometryRepresentation().getPosition(), geometryRepresentation.getPosition()) > interactRange) {
            currentInteraction.endInteract(this);
            currentInteraction = null;
            moveset.interaction = new PlayerInteraction(new Point2F(0, 0), InteractionType.NONE);
            return;
        }
        if (moveset.interaction.interactionType() == InteractionType.NONE) {
            if (currentInteraction != null) {
                currentInteraction.endInteract(this);
            }
            currentInteraction = null;

        } else {
            if (currentInteraction == null) {
                for (IEntity entity : view.getEntitiesInArea(geometryRepresentation.getPosition(), interactRange)) {
                    if (entity instanceof IInteractable interactable) {
                        if (Point2F.distance(interactable.getGeometryRepresentation().getPosition(), geometryRepresentation.getPosition()) <= interactRange) {
                            currentInteraction = interactable;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public EntityState getEntityState() {
        int activeSlot = inventory.getActiveSlotIndex();
        if (activeSlot == -1) {
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
        } else {
            return new EntityState(
                    entityId,
                    geometryConfigID,
                    geometryRepresentation.getPosition(),
                    geometryRepresentation.getVelocity(),
                    geometryRepresentation.getRotation(),
                    entityGroupID,
                    EntityAction.NONE,
                    0,
                    inventory.getSlot(activeSlot).getItem().getItemInfo().spriteID(),
                    inventory.getSlot(activeSlot).getItem().getItemInfo().action(),
                    inventory.getSlot(activeSlot).getItem().getItemInfo().timer()
            );
        }
    }

    public PlayerState getPlayerState() {
        int activeSlot = inventory.getActiveSlotIndex();
        if (activeSlot == -1) {
            return new PlayerState(
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
                    0,
                    sightRange,
                    maxHp,
                    currentHp
            );
        } else {
            return new PlayerState(
                    entityId,
                    geometryConfigID,
                    geometryRepresentation.getPosition(),
                    geometryRepresentation.getVelocity(),
                    geometryRepresentation.getRotation(),
                    entityGroupID,
                    EntityAction.NONE,
                    0,
                    inventory.getSlot(activeSlot).getItem().getItemInfo().spriteID(),
                    inventory.getSlot(activeSlot).getItem().getItemInfo().action(),
                    inventory.getSlot(activeSlot).getItem().getItemInfo().timer(),
                    sightRange,
                    maxHp,
                    currentHp
            );
        }
    }

    public PlayerInventoryState getPlayerInventoryState() {
        return new PlayerInventoryState(
                inventory.getInventoryInfo()
        );
    }

    public UpgradeTreeState getUpgradeTreeState() {
        if (upgradeTree != null) {
            return upgradeTree.getState();
        }
        return null;
    }

    public Rectangle2F getSightRange() {
        return new Rectangle2F(
                geometryRepresentation.getPosition().subtract(sightRange),
                geometryRepresentation.getPosition().add(sightRange));
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public IGeometryRepresentation geometryRepresentation() {
        return geometryRepresentation;
    }

    @Override
    public void takeDamage(Damage damage, IEntity source) {
        currentHp -= damage.value();
        if (currentHp <= 0) {
            onDeath.accept(new DeathData(this, geometryRepresentation));
        }
    }
}
