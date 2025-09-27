package game.engine.entities;

import game.engine.IWorldView;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.IDamageable;

public interface IDamageEffect {
    void applyEffect(IWorldView view, IEntity source, IDamageable target, Damage damage);
}
