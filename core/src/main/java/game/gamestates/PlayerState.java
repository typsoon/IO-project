package game.gamestates;

import game.engine.entities.GeometryConfigID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record PlayerState(
        GeometryConfigID geometryConfigId,
        Point2F position,
        Vector2F velocity
        //this will hold more information that entity state
) implements IGameState { }
