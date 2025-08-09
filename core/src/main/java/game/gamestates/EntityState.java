package game.gamestates;

import game.engine.entities.GeometryConfigID;
import game.engine.entities.SpriteID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record EntityState(
        int entityId,
        GeometryConfigID geometryConfigId,
        Point2F position,
        Vector2F velocity,
        SpriteID spriteConfigId
        // there should be what entity is holding, for example
) implements IGameState {
}
