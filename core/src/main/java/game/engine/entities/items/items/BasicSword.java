package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.items.ItemInfo;
import game.engine.entities.items.attacks.IAttack;
import game.engine.entities.items.attacks.RectangleSlash;

public class BasicSword extends BasicItem {
    IAttack attack = new RectangleSlash();
    private final int attackTime = 30; // in ticks
    private int attackClock = 0;


    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public void stopAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock = 0;
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
                "Basic Sword",
                "A simple sword. Reliable and sturdy.",
                EntityGroupID.BASIC_SWORD,
                action,
                attackClock
        );
    }

    @Override
    public void itemUnequip(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock = 0;
    }

    @Override
    public void primaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        attackClock++;
        if (attackClock >= attackTime) {
            attack.attack(view, user, modifiers.getDamageModifier());
            attackClock = 0;
        }
    }
}
