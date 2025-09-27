package game.gamestates;

import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.geometry.GeometryConfigID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record PlayerState(
        int entityId,
        GeometryConfigID geometryConfigId,
        Point2F position,
        Vector2F velocity,
        float rotation,
        EntityGroupID entityGroupId,
        EntityAction action,
        float actionProgress,
        EntityGroupID holdingItemGroupId,
        EntityAction itemAction,
        float itemActionProgress,
        Vector2F sightRange,
        int maxHp,
        int currentHp
// this will hold more informatsion than entity state
) implements IGameState {
}
