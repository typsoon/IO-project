package game.engine.entities.items.items;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.engine.entities.items.attacks.DamageModifier;

public interface IArrow {
    void fire(IWorldView view, IEntity user, DamageModifier modifiers);
}
