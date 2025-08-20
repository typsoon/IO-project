package game.engine.entities.behaviours;

import game.engine.IWorldView;
import game.engine.modules.IMovingGeometryRepresentation;

public interface IBehaviour {
    void behave(IMovingGeometryRepresentation geometryRepresentation, IWorldView worldView);
}
