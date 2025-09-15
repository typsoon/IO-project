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
        Vector2F sightRange

        //waiting for messages
//        InventoryInfo inventory
// this will hold more information than entity state
) implements IGameState {
}
