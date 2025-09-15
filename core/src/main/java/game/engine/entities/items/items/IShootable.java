package game.engine.entities.items.items;

import game.engine.entities.IEntity;
import game.engine.entities.items.attacks.DamageModifier;

public interface IShootable {
    void shoot(IEntity shooter, DamageModifier modifiers);
}
