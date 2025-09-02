package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.items.ItemInfo;
import game.engine.entities.items.ItemSpriteID;
import game.engine.entities.items.UsageModifiers;
import game.engine.entities.items.attacks.IAttack;
import game.engine.entities.items.attacks.RectangleSlash;

public class BasicSword extends BasicItem
{
    ItemInfo itemInfo = new ItemInfo(
            "Basic Sword",
            "A simple sword. Reliable and sturdy.",
            ItemSpriteID.BASIC_SWORD
    );

    IAttack attack = new RectangleSlash();
    private final int attackTime = 60; // in ticks
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
    public void itemUnequip(IWorldView view, IEntity user, UsageModifiers modifiers) {
        attackClock=0;
    }

    @Override
    public void primaryAction(IWorldView view, IEntity user, UsageModifiers modifiers) {
        attackClock++;
        if(attackClock >= attackTime){
            attack.attack(view,user, modifiers.getDamageModifier());
            attackClock = 0;
        }
    }
}
