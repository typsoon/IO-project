package game.engine.entities.items.attacks;

import game.engine.entities.IEntity;

public interface IDamageable {
    void takeDamage(Damage damage, IEntity source);
}
