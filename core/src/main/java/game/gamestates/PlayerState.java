package game.gamestates;

import game.engine.entities.GeometryConfigID;
import game.engine.entities.SpriteID;
import game.utility.Point2F;
import game.utility.Vector2F;

public record PlayerState(
        GeometryConfigID geometryConfigId,
        Point2F position,
        Vector2F velocity,
        SpriteID spriteConfigId

        //this will hold more information than entity state
) implements IGameState { }
