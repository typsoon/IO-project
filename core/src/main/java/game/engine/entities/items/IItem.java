package game.engine.entities.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;

public interface IItem {
    int stackSize();

    ItemInfo getItemInfo();

    void itemEquip(IWorldView view, IEntity user, IUsageModifiers modifiers);

    void itemUnequip(IWorldView view, IEntity user, IUsageModifiers modifiers);

    void primaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers);

    void secondaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers);

    void specialAction(IWorldView view, IEntity user, IUsageModifiers modifiers);

    void stopAction(IWorldView view, IEntity user, IUsageModifiers modifiers);
}
