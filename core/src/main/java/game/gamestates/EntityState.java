package game.gamestates;

import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.geometry.GeometryConfigID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record EntityState(
        int entityId,
        GeometryConfigID geometryConfigID,
        Point2F position,
        Vector2F velocity,
        float rotation,
        EntityGroupID entityGroupId,
        EntityAction action,
        float actionProgress,
        EntityGroupID holdingItemGroupId,
        EntityAction itemAction,
        float itemActionProgress
        // there should be what entity is holding, for example
) implements IGameState {
}
