package game.engine.entities;

import game.engine.entities.items.attacks.Damage;

public interface IDamageable {
    void takeDamage(Damage weapon, IEntity source);
}
