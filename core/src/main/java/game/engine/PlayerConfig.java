package game.engine;

import game.engine.entities.GeometryConfigID;
import game.engine.entities.SpriteID;

public record PlayerConfig(
        GeometryConfigID geometryConfigID,
        SpriteID spriteID
){}
