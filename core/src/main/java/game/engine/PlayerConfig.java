package game.engine;

import game.engine.entities.EntityGroupID;
import game.engine.entities.geometry.GeometryConfigID;

public record PlayerConfig(
        GeometryConfigID geometryConfigID,
        EntityGroupID entityGroupID) {
    public PlayerConfig() {
        this(GeometryConfigID.HUMAN,
                EntityGroupID.HUMAN_BASIC);
    }

}
