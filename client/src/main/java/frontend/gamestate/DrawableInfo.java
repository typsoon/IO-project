package frontend.gamestate;

import game.engine.entities.SpriteID;

// TODO: Consider whether using the Enum's ordinal instead of the Enum itself would be better.
public record DrawableInfo<T extends Enum<T>>(
        SpriteID entityGroupID,
        Enum<T> state,
        // TODO: Modifiers modifiers,
        float x, float y, float width, float height,
        float originX, float originY,
        float scaleX, float scaleY,
        float rotation) {
}
