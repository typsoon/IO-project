package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.inventory.IHaveInventory;
import game.engine.entities.inventory.ISlot;
import game.engine.entities.items.ItemInfo;
import game.engine.entities.items.ItemSpriteID;

public class BasicBow extends BasicItem {
    ItemInfo itemInfo = new ItemInfo(
            "Basic Bow",
            "A simple bow. Reliable and sturdy.",
            ItemSpriteID.BASIC_SWORD
    );

    private final int attackTime = 7; // in ticks
    private int attackClock = 0;

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    @Override
    public void itemUnequip(IWorldView view, IEntity user, IUsageModifiers modifiers) {
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
