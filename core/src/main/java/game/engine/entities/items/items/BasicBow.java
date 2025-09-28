package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.inventory.IHaveInventory;
import game.engine.entities.inventory.ISlot;
import game.engine.entities.items.ItemInfo;

public class BasicBow extends BasicItem {
    private final int attackTime = 7; // in ticks
    private int attackClock = 0;

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public ItemInfo getItemInfo() {
        EntityAction action;
        if (attackClock > 0) {
            action = EntityAction.ACTION_ONE;
        } else {
            action = EntityAction.NONE;
        }
        return new ItemInfo(
                "Basic Bow",
                "A simple bow. Reliable and sturdy.",
                EntityGroupID.BASIC_BOW,
                action,
                attackClock
        );
    }

    @Override
    public void itemUnequip(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock = 0;
    }

    @Override
    public void stopAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock = 0;
    }

    @Override
    public void primaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock++;
        if (attackClock >= attackTime) {
            shoot(view, user, modifiers);
            attackClock = 0;
        }
    }

    private void shoot(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        if (user instanceof IHaveInventory haveInventory) {
            for (ISlot slot : haveInventory.getInventory().getSlots()) {
                if (!slot.isEmpty() && slot.getItem() instanceof IArrow arrow) {
                    arrow.fire(view, user, modifiers.getDamageModifier());
                    slot.removeItems(1);
                    break;
                }
            }
        }
    }
}
