package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.IEntity;
import game.engine.entities.items.ItemInfo;
import game.engine.entities.items.attacks.DamageModifier;

public class BasicArrow extends BasicItem implements IArrow {
    private final IShootable shootable;

    public BasicArrow(IShootable shootable) {
        this.shootable = shootable;
    }

    @Override
    public int stackSize() {
        return 20;
    }

    @Override
    public ItemInfo getItemInfo() {
        return new ItemInfo(
                "Basic Arrow",
                "A simple arrow. Reliable and sturdy.",
                EntityGroupID.BASIC_ARROW,
                EntityAction.NONE,
                0
        );
    }

    @Override
    public void fire(IWorldView view, IEntity user, DamageModifier modifiers) {
        shootable.shoot(user, modifiers);
    }
}
