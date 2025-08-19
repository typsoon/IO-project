package game.gamestates;

import game.engine.entities.EntityAction;
import game.engine.entities.GeometryConfigID;
import game.engine.entities.EntityGroupID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record EntityState(
        int entityId,
        GeometryConfigID geometryConfigID,
        Point2F position,
        Vector2F velocity,
        EntityGroupID entityGroupId,
        EntityAction action
        // there should be what entity is holding, for example
) implements IGameState {
}
