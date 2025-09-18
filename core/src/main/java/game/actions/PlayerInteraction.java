package game.actions;

import game.utility.Point2F;


public record PlayerInteraction(
        Point2F position,
        InteractionType interactionType
) implements IAction {
}
