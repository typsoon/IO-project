package game.engine;

import game.engine.entities.GeometryConfigID;
import game.engine.entities.EntityGroupID;

public record PlayerConfig(
        GeometryConfigID geometryConfigID,
        EntityGroupID entityGroupID
){}
