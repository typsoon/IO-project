package game.engine.entities.weapons;

import game.engine.IWorldView;
import game.engine.modules.IGeometryRepresentation;

public interface IWeapon {
    void attack(IWorldView view, IGeometryRepresentation representation);
}
