package game.gamestates;

import game.Utility.Point2F;
import game.actions.Action;
import game.engine.components.BodyType;

public record EntityState(
        BodyType bodyType,
        Point2F position,
        Action action
) {
}
