package game.actions;

import game.utility.Vector2F;

public record PlayerSlotUse(
        UsageType usageType,
        Vector2F direction,
        int slot
) implements IAction {
}
