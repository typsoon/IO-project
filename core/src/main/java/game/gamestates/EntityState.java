package game.gamestates;

import game.utility.Point2F;
import game.actions.Action;
import game.engine.components.BodyType;

public record EntityState(
        int entityId,
        BodyType bodyType,
        Point2F position,
        Action action
) {
}
