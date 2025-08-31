package game.engine.entities;

import game.engine.entities.weapons.Damage;

public interface IDamageable {
    void takeDamage(Damage weapon, IEntity source);
}
