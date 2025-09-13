package game.engine.entities.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;

public interface IItem {
    int stackSize();

    ItemInfo getItemInfo();

    void itemEquip(IWorldView view, IEntity user, UsageModifiers modifiers);

    void itemUnequip(IWorldView view, IEntity user, UsageModifiers modifiers);

    void primaryAction(IWorldView view, IEntity user, UsageModifiers modifiers);

    void secondaryAction(IWorldView view, IEntity user, UsageModifiers modifiers);

    void specialAction(IWorldView view, IEntity user, UsageModifiers modifiers);
}
