package game.engine.entities.weapons;

import game.engine.IWorldView;
import game.engine.entities.IEntity;

public interface IWeapon {
    void attack(IWorldView view, IEntity user);
}
