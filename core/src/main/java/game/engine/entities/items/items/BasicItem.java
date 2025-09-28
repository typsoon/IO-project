package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.IUsageModifiers;
import game.engine.entities.items.IItem;

public abstract class BasicItem implements IItem {

    @Override
    public void itemEquip(IWorldView view, IEntity user, IUsageModifiers modifiers) {

    }

    @Override
    public void itemUnequip(IWorldView view, IEntity user, IUsageModifiers modifiers) {

    }

    @Override
    public void stopAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {
        
    }

    @Override
    public void primaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {

    }

    @Override
    public void secondaryAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {

    }

    @Override
    public void specialAction(IWorldView view, IEntity user, IUsageModifiers modifiers) {

    }
}
