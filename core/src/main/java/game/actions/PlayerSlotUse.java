package game.actions;

import game.Utility.Vector2F;

public record PlayerSlotUse (
        UsageType usageType,
        Vector2F direction
) implements Action {
}
