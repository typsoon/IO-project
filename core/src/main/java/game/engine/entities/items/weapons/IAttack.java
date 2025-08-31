package game.engine.entities.items.weapons;

import game.engine.IWorldView;
import game.engine.entities.IEntity;

public interface IAttack{
    void attack(IWorldView view, IEntity user, DamageModifier modifier);
}
