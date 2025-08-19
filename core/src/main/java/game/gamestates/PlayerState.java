package game.gamestates;

import game.engine.entities.EntityAction;
import game.engine.entities.GeometryConfigID;
import game.engine.entities.EntityGroupID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record PlayerState(
        int entityId,
        GeometryConfigID geometryConfigId,
        Point2F position,
        Vector2F velocity,
        float rotation,
        EntityGroupID entityGroupId,
        EntityAction action
        //this will hold more information than entity state
) implements IGameState { }
